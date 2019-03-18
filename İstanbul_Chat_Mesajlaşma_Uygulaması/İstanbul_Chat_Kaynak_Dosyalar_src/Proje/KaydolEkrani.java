package Proje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class KaydolEkrani extends javax.swing.JFrame {

    public KaydolEkrani() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfkadi = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        kaydol = new javax.swing.JButton();
        tfeposta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pfsifre = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Kullanıcı Adı");

        jLabel2.setText("Şifre");

        kaydol.setText("Kaydol");
        kaydol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kaydolActionPerformed(evt);
            }
        });

        jLabel3.setText("Eposta");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kaydol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfeposta, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                            .addComponent(tfkadi)
                            .addComponent(pfsifre))))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfkadi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pfsifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfeposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(kaydol)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    boolean boslukKontrol(String... alan) {
        for (String s : alan) {
            if (s.equals("")) {
                return false;
            }
        }
        return true;
    }

    String createJsonString(String userName, String pass, String email) {
        return Json.createObjectBuilder()
                .add("username", userName)
                .add("password", pass)
                .add("email", email)
                .build().toString();
    }

    String PrepareNewUserJson(String userName, String password, String email) {
        return Json.createObjectBuilder().add("type", "command")
                .add("content", "newuser")
                .add("username", userName)
                .add("password", password)
                .add("email", email)
                .build().toString();
    }

    boolean SendNewUserRequest(String userName, String password, String email) {
        String newjs = PrepareNewUserJson(userName, password, email);
        Socket cs = null;
        try {
            cs = new Socket("127.0.0.1", 8085);
            PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);
            pw.println(newjs);

        } catch (IOException ex) {
            Logger.getLogger(KaydolEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getNewUserServerResponse(cs);
    }

    boolean getNewUserServerResponse(Socket cs) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()))) {
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

    boolean isMailValid(String email) {
        return email.contains("@") && email.length() > 10;
    }

    private void kaydolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kaydolActionPerformed
        String username = tfkadi.getText();
        String password = sifreToString(pfsifre.getPassword());
        String email = tfeposta.getText();

        if (!boslukKontrol(username, password, email)) {
            JOptionPane.showMessageDialog(this, "Tüm Alanlar Doldurulmalıdır");
            return;
        }
        if (username.length() < 5) {
            JOptionPane.showMessageDialog(this, "Kullanıcı Adı En Az 5 Haneli Olmalıdır");
            return;
        }
        if (username.matches("[^A-Za-z0-9 ]")) {
            JOptionPane.showMessageDialog(this, "Özel Karakter Kullanılamaz");
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Şifre En az 6 Haneli Olmalıdır");
            return;
        }
        if (!isMailValid(email)) {
            JOptionPane.showMessageDialog(this, "Lütfen Geçerli Bir Eposta Adresi Giriniz");
            return;
        }
        if (!SendNewUserRequest(username, password, email)) {
            JOptionPane.showMessageDialog(this, "Bu Kullanıcı Adı veya Eposta Alındı Lütfen Tekrar Deneyiniz");
            return;
        }

        JOptionPane.showMessageDialog(this, "Kayıt Başarılı");
        this.dispose();
        new GirisEkrani().setVisible(true);
    }//GEN-LAST:event_kaydolActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(KaydolEkrani.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton kaydol;
    private javax.swing.JPasswordField pfsifre;
    private javax.swing.JTextField tfeposta;
    private javax.swing.JTextField tfkadi;
    // End of variables declaration//GEN-END:variables
}
