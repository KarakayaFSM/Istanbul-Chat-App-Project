package Proje;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.dbutils.DbUtils;

public class OnlineUsersGui extends javax.swing.JFrame {

    ExecutorService exc;
    Socket cs;
    DefaultListModel dlm = new DefaultListModel();
    String userName;
    Set<String> OnlineUnames = Collections.synchronizedSet(new HashSet());
    String hostName = "127.0.0.1";
    int portNum = 8085;
    final short MessageHistoryLimit = 20;
    boolean shouldCleanDatabase = false;
    PrintWriter pw;
    BufferedReader br;
    JButton componentCloseButton;

    ActionListener closeButtonListener;
    Vector<JsonObject> Messages = new Vector();
    HashMap<String, JTextArea> componentMap = new HashMap();
    String path = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\ClientDB.db";
    int oldMessagesCount = 0;

    public OnlineUsersGui(Socket cs, String userName) {
        super(userName);
        initComponents();
        jlusers.setModel(dlm);
        this.userName = userName;
        this.cs = cs;
        exc = Executors.newFixedThreadPool(2);
        pw = CreateWriter(cs);
        getOldMessagesFromDb();
        startListeningWithExecutor();
    }

    PrintWriter CreateWriter(Socket cs) {
        try {
            return new PrintWriter(cs.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtp = new javax.swing.JTabbedPane();
        jtpScroll = new javax.swing.JScrollPane();
        jlusers = new javax.swing.JList<>();
        tf = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setText("Çevrimiçi Kullanıcılar");

        jtp.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jtp.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jtpStateChanged(evt);
            }
        });
        jtp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtpMouseClicked(evt);
            }
        });

        jtpScroll.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jlusers.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jlusers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jlusers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlusersMouseClicked(evt);
            }
        });
        jtpScroll.setViewportView(jlusers);

        jtp.addTab("Onlines", jtpScroll);

        tf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(tf, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jtp, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jtp, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(tf, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void CheckMesagesTable(Connection con) throws SQLException {
        con.setAutoCommit(false);
        String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT, \n"
                + " message text NOT NULL, \n"
                + " fromuser text NOT NULL, \n"
                + " touser text NOT NULL, \n"
                + " timesent text NOT NULL\n"
                + ");";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
        con.commit();
    }

    //Check if a table exists: 
    //SELECT name FROM sqlite_master WHERE type='table' AND name='{table_name}';
    void TransferMessagesToDb() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(path);
            con.setAutoCommit(false);
            shouldCleanDatabase = shouldCleanDatabase ? deleteMessagesTable() : false;
            CheckMesagesTable(con);
            String sql = "INSERT INTO messages(message,fromuser,touser,timesent) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            for (int i = oldMessagesCount; i < Messages.size(); i++) {
                JsonObject mesaj = Messages.get(i);
                ps.setString(1, mesaj.getString("content"));
                ps.setString(2, mesaj.getString("from"));
                ps.setString(3, mesaj.getString("to"));
                ps.setLong(4, mesaj.getJsonNumber("thetime").longValueExact());
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    void getOldMessagesFromDb() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(path);
            CheckMesagesTable(con);
            String sql = "SELECT * FROM messages ORDER BY timesent";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Messages.addElement(Json.createObjectBuilder()
                        .add("type", "message")
                        .add("content", rs.getString("message"))
                        .add("from", rs.getString("fromuser"))
                        .add("to", rs.getString("touser"))
                        .add("thetime", rs.getLong("timesent"))
                        .build());
            }
            oldMessagesCount = Messages.size();
        } catch (SQLException ex) {
            Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con);
        }
    }

    void addJsonArrToSet(JsonArray arr) {
        synchronized (OnlineUnames) {
            for (int i = 0; i < arr.size(); i++) {
                OnlineUnames.add(arr.getString(i));
            }
        }
    }

    BufferedReader CreateReader(Socket cs) {
        BufferedReader bro = null;
        try {
            bro = new BufferedReader(new InputStreamReader(cs.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bro;
    }

    void startListeningWithExecutor() {
        br = CreateReader(cs);
        exc.submit(() -> {
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    JsonReader rd = Json.createReader(new StringReader(line));
                    JsonObject js = rd.readObject();
                    String type = js.getString("type");
                    if (type.equals("usernameresponse")) {
                        if (js.get("content").getValueType().toString().equals("STRING")) {
                            synchronized (OnlineUnames) {
                                OnlineUnames.add(js.getString("content"));
                            }
                            updateModel(js.getString("content"), true);
                        } else if (js.get("content").getValueType().toString().equals("ARRAY")) {
                            JsonArray arr = getContent(js);//değiştirildi
                            addJsonArrToSet(arr);
                            updateModel(arr);
                        }
                    } else if (type.equals("message")) {                 
                        int index = jtp.indexOfTab(js.getString("from"));
                        boolean isPresent = (index == -1) ? newTabAddRcvdMessage(js) : addReceivedMessage(js);
                        addToMessages(js);
                    } else if (type.equals("closingresponse")) {
                        String toRemove = js.getString("content");
                        synchronized (OnlineUnames) {
                            OnlineUnames.remove(toRemove);
                        }
                        if (jtp.indexOfTab(toRemove) != -1) {
                            JOptionPane.showMessageDialog(this, toRemove + " Kapattı");
                            jtp.removeTabAt(jtp.indexOfTab(toRemove));
                        }
                        updateModel(toRemove, false);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    void updateModel(JsonArray arr) {
        java.awt.EventQueue.invokeLater(() -> {
            synchronized (dlm) {
                for (int i = 0; i < arr.size(); i++) {
                    dlm.addElement(arr.getString(i));
                }
            }
        });
    }

    void updateModel(String str, boolean ekle) {
        java.awt.EventQueue.invokeLater(() -> {
            synchronized (dlm) {
                if (ekle) {
                    dlm.addElement(str);
                } else {
                    dlm.removeElement(str);
                }
            }
        });
    }

    JsonObject prepareMessage(String message, String from, String to) {
        return Json.createObjectBuilder()
                .add("type", "message")
                .add("content", message)
                .add("from", from)
                .add("to", to)
                .add("thetime", System.currentTimeMillis())
                .build();
    }

    void addToMessages(JsonObject message) {
        if (Messages.size() >= MessageHistoryLimit) {
            Messages.removeAllElements();
            oldMessagesCount = 0;
            shouldCleanDatabase = true;
        }
        Messages.addElement(message);
    }

    boolean deleteMessagesTable() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(path);
            con.setAutoCommit(false);
            String sql = "DELETE FROM messages";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DbUtils.closeQuietly(con);
        }
        return false;
    }

    void sendMessage(String messageToSend, String to) {
        JsonObject message = prepareMessage(messageToSend, userName, to);
        exc.submit(() -> {
            pw.println(message);
        });
        addToMessages(message);
    }

    void prepareMessageView(String myFriend) {
        JScrollPane jsp = new JScrollPane();
        jsp.setCursor(Cursor.getDefaultCursor());
        jsp.setViewportView(createTextArea(myFriend, getMessageHistory(myFriend)));
        jtp.addTab(myFriend, jsp);
    }

    boolean newTabAddRcvdMessage(JsonObject js) {
        String from = js.getString("from"), message = js.getString("content");
        prepareMessageView(from);
        java.awt.EventQueue.invokeLater(() -> {
            findComponentByName(from).append(from + ": " + message + "\n");
        });
        return false;
    }

    boolean addReceivedMessage(JsonObject js) {
        String from = js.getString("from"), message = js.getString("content");
        java.awt.EventQueue.invokeLater(() -> {
            findComponentByName(from).append(from + ": " + message + "\n");
        });
        return true;
    }

    JTextArea findComponentByName(String name) {
        return componentMap.containsKey(name) ? componentMap.get(name) : null;
    }

    void addSentMessage(String message, String title) {
        java.awt.EventQueue.invokeLater(() -> {
            findComponentByName(title).append(userName + ": " + message + "\n");
        });
    }

    JsonArray getContent(JsonObject obj) {
        return Json.createArrayBuilder(obj.getJsonArray("content").asJsonArray()).build();
    }

    void sendCommandToServer(String type, String commandType, String from) {
        String UserJson = Json.createObjectBuilder()
                .add("type", type)
                .add("content", commandType)
                .add("from", from)
                .build().toString();
        pw.println(UserJson);
    }

    String getMessageHistory(String myFriend) {
        if (Messages.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Messages.size(); i++) {
            String from = Messages.get(i).getString("from");
            if (from.equals(userName) || from.equals(myFriend)) {
                sb.append(from).append(": ").append(Messages.get(i).getString("content")).append("\n");
            }
        }
        return sb.toString();
    }

    JTextArea createTextArea(String myFriend, String messageHistory) {
        JTextArea ta = new JTextArea(messageHistory);
        ta.setName(myFriend);
        ta.setEditable(false);
        ta.setFont(new Font("Dialog", 1, 18));
        ta.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        componentMap.put(myFriend, ta);
        return ta;
    }

    private void jlusersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlusersMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            String myFriend = dlm.getElementAt(jlusers.getSelectedIndex()).toString();
            JScrollPane jsp = new JScrollPane();
            jsp.setCursor(Cursor.getDefaultCursor());
            jsp.setViewportView(createTextArea(myFriend, getMessageHistory(myFriend)));
            jtp.addTab(myFriend, jsp);
        }
    }//GEN-LAST:event_jlusersMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //bu event sadece çarpı tuşuna basma vs. gibi kesin kapatmalarda tetiklenir dispose'dan etkilenmez        
        sendCommandToServer("command", "closing", userName);
        if (!Messages.isEmpty() && oldMessagesCount != Messages.size()) {
            TransferMessagesToDb();
        }
        new GirisEkrani().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void jtpStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jtpStateChanged
        tf.setEnabled(jtp.getSelectedIndex() != 0);
    }//GEN-LAST:event_jtpStateChanged

    private void jtpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtpMouseClicked
        int indexAtLocation = jtp.indexAtLocation(evt.getX(), evt.getY());
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2 && !(indexAtLocation <= 0)) {
            jtp.removeTabAt(indexAtLocation);
        }
        //MessagePopup ile Ekranın sağ-alt Köşesinde Bir Yeni mesajınız Var Uyarısı Verilebilir
    }//GEN-LAST:event_jtpMouseClicked

    private void tfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfActionPerformed
        int index = jtp.getSelectedIndex();
        String message = tf.getText(), title = jtp.getTitleAt(index);
        tf.setText("");
        addSentMessage(message, title);
        sendMessage(message, title);
    }//GEN-LAST:event_tfActionPerformed
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(OnlineUsersGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jlusers;
    private javax.swing.JTabbedPane jtp;
    private javax.swing.JScrollPane jtpScroll;
    private javax.swing.JTextField tf;
    // End of variables declaration//GEN-END:variables
}