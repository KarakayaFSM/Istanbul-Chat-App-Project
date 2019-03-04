package Proje;


import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.Timer;
import org.apache.commons.dbutils.DbUtils;

public class Server {

    private final int PORTNUM = 8085;
    HashMap<String, Socket> Onlines = new HashMap();
    HashMap<String, String> ResetTokens = new HashMap();
    ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

    public void DirectInput(BufferedReader in, Socket cs) {
        try {
            while (true) {
                String line;
                if ((line = in.readLine()) != null) {
                    JsonObject js = Json.createReader(new StringReader(line)).readObject();
                    String type = js.getString("type");
                    if (type.equals("message")) {
                        processMessage(js);
                    } else if (type.equals("login")) {
                        String username = js.getString("username");
                        boolean result = isUserInfoValid(username, js.getString("password"));
                        sendResult(cs, result);
                        if (result) {
                            Thread.currentThread().setName(username);
                            WriteUserName(username, cs);
                            continue; // if user is allowed to log in keep the thread alive
                        }
                        break;  //if user not allowed to log in by providing invalid info end the loop
                    } else if (type.equals("command")) {
                        processCommand(js, cs);
                    }
                }
            }
        } catch (SocketException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException | JsonException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    boolean checkEmailSendToken(String email) {
        return checkEmail(email) ? sendPasswordResetToken(email) : false;
    }

    boolean checkToken(String key, String token) {
        boolean res = ResetTokens.containsValue(token); //key: email
        ResetTokens.remove(key);
        return res;
    }

    boolean addNewUserToDb(String username, String password, String email) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:D:\\Belgeler\\ServerChatAppFiles\\test.db");
            con.setAutoCommit(false);
            String sql = "INSERT INTO users(username,password,email) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con);
        }
        return true;
    }

    boolean isUserInfoValid(String username, String password) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:D:\\Belgeler\\ServerChatAppFiles\\test.db");
            String sql = "SELECT username,password FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con);
        }
        return false;
    }

    boolean sendResult(Socket cs, boolean res) throws IOException {
        PrintWriter wr = new PrintWriter(cs.getOutputStream(), true);
        JsonObject ob = Json.createObjectBuilder()
                .add("result", res)
                .build();
        wr.println(ob);
        return res;
    }

    boolean isNewUserInfoValid(String username, String email) {
        String sql = "SELECT * FROM users WHERE username = ? AND email = ?";
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:D:\\Belgeler\\ServerChatAppFiles\\test.db");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DbUtils.closeQuietly(con);
        }
        return true;
    }

    boolean checkEmail(String email) {
        String sql = "SELECT username,email FROM users WHERE email = ?";
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:D:\\Belgeler\\ServerChatAppFiles\\test.db");
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            DbUtils.closeQuietly(con);
        }
        return false;
    }

    public void processMessage(JsonObject messageJson) {
        Socket socket = findSocketByName(messageJson.getString("to"));
        if (!socket.isClosed()) {
            sendSpecific(socket, messageJson.toString());
        }
    }

    void startTokenExpirationCountDown(String email, String token) {
        int delay = 180000;
        Timer t = new Timer(delay, (ActionEvent e) -> {
            if (ResetTokens.containsKey(email)) {
                ResetTokens.remove(email, token);
            }
        });
        t.setRepeats(false);
        t.start();
    }

    void processCommand(JsonObject cmdJs, Socket cs) throws IOException {
        String commandType = cmdJs.getString("content");
        if (commandType.equals("closing")) {
            onClosing(cmdJs.getString("from"));
        } else if (commandType.contains("password")) {
            String email = cmdJs.getString("key");
            boolean result = commandType.endsWith("1") ? checkToken(email, cmdJs.getString("value")) : checkEmailSendToken(email);
            sendResult(cs, result);
        } else if (commandType.equals("newuser")) {
            sendResult(cs, onNewUser(cs, cmdJs));
        } else if (commandType.equals("updateuserdata")) {
            updatePassword(cmdJs.getString("key"), cmdJs.getString("value")); //key: email value: newpassword
        }
    }

    boolean onNewUser(Socket cs, JsonObject js) throws IOException {
        String username = js.getString("username");
        String email = js.getString("email");
        return isNewUserInfoValid(username, email) ? addNewUserToDb(username, js.getString("password"), email) : false;
    }

    void updatePassword(String email, String newpassword) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:D:\\Belgeler\\ServerChatAppFiles\\test.db");
            con.setAutoCommit(false);
            String sql = "UPDATE users SET password = ? where email = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newpassword);
            ps.setString(2, email);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    int sendToken(String email) {
        int token;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("EmailAddress", "Password"); //Eposta ve Şifre Kısımları Güvenlik Nedeniyle Boş Bırakıldı
            }																  // Bu bölüme gönderici olarak kullanılmak istenen eposta adresinin bilgileri yazılır
        });

        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress("ChatAppDestek@noreply.com", "ChatAppDestek"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "Kullanici"));
            msg.setSubject("ChatApp Şifremi Unuttum Başvurunuz");
            token = new Random().nextInt(10000);
            msg.setText("Şifre Yenileme Kodunuz: " + token);
            Transport.send(msg);
        } catch (UnsupportedEncodingException | MessagingException exc) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, exc);
            return -1;
        }
        return token;
    }

    boolean sendPasswordResetToken(String email) {
        int token = sendToken(email);
        if (token != -1) {
            ResetTokens.put(email, token + "");
            startTokenExpirationCountDown(email, token + "");
            return true;
        }
        return false;
    }

    public void sendSpecific(Socket cs, String type, String content) {
        if (cs.isConnected() && !cs.isClosed()) {
            try {
                PrintWriter writer = new PrintWriter(cs.getOutputStream(), true);
                JsonObject o = Json.createObjectBuilder().add("type", type)
                        .add("content", content).build();
                writer.println(o.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendSpecific(Socket socket, String objToSend) {
        if (socket.isConnected() && !socket.isClosed()) {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(objToSend);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendSpecific(Socket socket, String type, JsonArray content) {
        if (socket.isConnected() && !socket.isClosed()) {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                JsonObject o = Json.createObjectBuilder().add("type", type)
                        .add("content", content).build();
                writer.println(o.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendEveryone(String type, String content) {
        try {
            for (Map.Entry<String, Socket> item : Onlines.entrySet()) {
                Socket socket = item.getValue();
                if (!socket.isClosed()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    JsonObject o = Json.createObjectBuilder()
                            .add("type", type)
                            .add("content", content)
                            .build();
                    out.println(o);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClosing(String userName) {
        try {
            synchronized (Onlines) {
                Onlines.get(userName).close();
                Onlines.remove(userName);
            }
            sendEveryone("closingresponse", userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket findSocketByName(String username) {
        return Onlines.containsKey(username) ? Onlines.get(username) : null;
    }

    boolean addSocket(String username, Socket socket) {
        sendEveryone("usernameresponse", username);
        sendSpecific(socket, "usernameresponse", PrepareOnlines());
        synchronized (Onlines) {
            Onlines.put(username, socket);
        }
        return true;
    }

    public boolean WriteUserName(String username, Socket cs) {
        return Onlines.isEmpty() ? whenOnlinesEmpty(username, cs) : addSocket(username, cs);
    }

    boolean whenOnlinesEmpty(String username, Socket cs) {
        Onlines.put(username, cs);
        return true;
    }

    public JsonArray PrepareOnlines() {
        return Json.createArrayBuilder(Onlines.keySet()).build();
    }

    void submitTask(Socket cs) {
        executor.submit(() -> {
            try {
                DirectInput(new BufferedReader(new InputStreamReader(cs.getInputStream())), cs);
            } catch (IOException exc) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, exc);
            }
        });
    }

    public static void main(String[] args) {
        new Server().init();
    }

    void init() {
        executor.submit(() -> {
            try {
                ServerSocket ss = new ServerSocket(PORTNUM);
                while (true) {
                    Socket cs = ss.accept();
                    submitTask(cs);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
