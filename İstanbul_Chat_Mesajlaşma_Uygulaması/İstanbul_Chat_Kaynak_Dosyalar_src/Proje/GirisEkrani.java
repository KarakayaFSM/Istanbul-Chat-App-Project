package Proje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GirisEkrani extends javax.swing.JFrame {

    Socket cs;

    public GirisEkrani() {
        super("Giriş");
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfkadi = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        giris = new javax.swing.JButton();
        kaydol = new javax.swing.JButton();
        pfsifre = new javax.swing.JPasswordField();
        forgotPassword = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Kullanıcı Adı");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("Şifre");

        giris.setText("Giriş");
        giris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                girisActionPerformed(evt);
            }
        });

        kaydol.setText("Kaydol");
        kaydol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kaydolActionPerformed(evt);
            }
        });

        pfsifre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfsifreActionPerformed(evt);
            }
        });

        forgotPassword.setText("Şifre Sıfırla");
        forgotPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgotPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kaydol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(giris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfkadi)
                            .addComponent(pfsifre, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                    .addComponent(forgotPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfkadi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pfsifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addComponent(giris)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kaydol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(forgotPassword)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
      boolean verifyUserInfo(String userName, char[] pass) {
        try {
            cs = new Socket("127.0.0.1", 8085);
            PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
            out.println(PrepareLoginJson(userName, sifreToString(pass)));
        } catch (IOException ex) {
            Logger.getLogger(GirisEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getServerResponse(cs);
    }

    boolean verifyUserInfo(String data, boolean mode) {
        try {
            String CommandType = mode ? "forgotpassword" : "forgotpassword1"; //forgotpassword1 = verifyToken
            cs = new Socket("127.0.0.1", 8085);
            PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
            out.println(PrepareCommandJson(CommandType, data));
        } catch (IOException ex) {
            Logger.getLogger(GirisEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getServerResponse(cs);
    }

    boolean verifyUserInfo(Socket cs, String key, String value, boolean mode) {
        try {
            if (cs.isClosed()) {
                cs = new Socket("127.0.0.1", 8085);
            }
            String CommandType = mode ? "forgotpassword" : "forgotpassword1"; //forgotpassword1 = verifyToken
            PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
            out.println(PrepareCommandJson(CommandType, key, value));
        } catch (IOException ex) {
            Logger.getLogger(GirisEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getServerResponse(cs);
    }

    void sendCommandToServer(Socket cs, String commandType, String key, String value) {
        try {
            if (cs.isClosed()) {
                cs = new Socket("127.0.0.1", 8085);
            }
            PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
            out.println(PrepareCommandJson(commandType, key, value));
        } catch (IOException ex) {
            Logger.getLogger(GirisEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean getServerResponse(Socket cs) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String line;
            if ((line = br.readLine()) != null) {
                JsonReader rd = Json.createReader(new StringReader(line));
                JsonObject o = rd.readObject();
                return o.getBoolean("result");
            }
        } catch (IOException ex) {
            Logger.getLogger(KaydolEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    String sifreToString(char[] sifre) {
        String parola = "";
        for (char c : sifre) {
            parola += c;
        }
        return parola;
    }

    void onInvalidInfo() {
        JOptionPane.showMessageDialog(this, "Böyle bir kullanıcı bulunamadı");
        try {
            cs.close();
        } catch (IOException ex) {
            Logger.getLogger(GirisEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void girisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_girisActionPerformed
        String userName = tfkadi.getText();
        if (!verifyUserInfo(userName, pfsifre.getPassword())) {
            onInvalidInfo();
            return;
        }
        new OnlineUsersGui(cs, userName).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_girisActionPerformed
    String PrepareLoginJson(String username, String password) {
        return Json.createObjectBuilder().add("type", "login")
                .add("username", username)
                .add("password", password)
                .build().toString();
    }

    String PrepareCommandJson(String commandType, String data) {
        return Json.createObjectBuilder()
                .add("type", "command")
                .add("content", commandType)
                .add("key", data)
                .build().toString();
    }

    String PrepareCommandJson(String commandType, String key, String value) {
        return Json.createObjectBuilder()
                .add("type", "command")
                .add("content", commandType)
                .add("key", key)
                .add("value", value)
                .build().toString();
    }

    boolean isValid(String input) {
        return input != null && !input.isEmpty();
    }

    boolean isMailValid(String email) {
        try {
            if (!(email.contains("@") && email.length() > 10)) {
                return false;
            }
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                    + "[a-zA-Z0-9_+&*-]+)*@"
                    + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";
            Pattern pat = Pattern.compile(emailRegex);
            return pat.matcher(email).matches();
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void kaydolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kaydolActionPerformed
        new KaydolEkrani().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_kaydolActionPerformed

    private void pfsifreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfsifreActionPerformed
        giris.doClick();
    }//GEN-LAST:event_pfsifreActionPerformed

    private void forgotPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgotPasswordActionPerformed
        String eposta = JOptionPane.showInputDialog(this, "Eposta Adresi: ");
        if (!isMailValid(eposta)) {
            JOptionPane.showMessageDialog(this, "Geçersiz Giriş");
            return;     //boş yada null değer kontrolü
        }
        if (!verifyUserInfo(eposta, true)) { //epostayı sorgu için sunucuya gönder ve gelen cevabı döndür
            JOptionPane.showMessageDialog(this, "Böyle Bir Kullanıcı Bulunamadı");
            return;
        }
        //Eposta kayıtlarda varsa sunucu bu adrese bir şifre sıfırlama kodu gönderir
        String kod = JOptionPane.showInputDialog(this, "Epostanıza Gelen Şifre Sıfırlama Kodunu Giriniz");
        if (!isValid(kod)) {
            return; //boş yada null değer kontrolü
        }
        if (!verifyUserInfo(cs, eposta, kod, false)) {  // girilen şifre sıfırlama kodunun doğruluğunu sorgula
            JOptionPane.showMessageDialog(this, "Girilen Şifre Sıfırlama Kodu Hatalı");
            return;
        }
        String yeniSifre = JOptionPane.showInputDialog(this, "Yeni Şifre: ");
        if (!isValid(yeniSifre)) {
            return; //boş yada null değer kontrolü
        }
        sendCommandToServer(cs,"updateuserdata", eposta, yeniSifre);
        JOptionPane.showMessageDialog(this, "Şifreniz Yenilendi");
    }//GEN-LAST:event_forgotPasswordActionPerformed
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GirisEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
        new GirisEkrani().setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton forgotPassword;
    private javax.swing.JButton giris;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton kaydol;
    private javax.swing.JPasswordField pfsifre;
    private javax.swing.JTextField tfkadi;
    // End of variables declaration//GEN-END:variables
}
