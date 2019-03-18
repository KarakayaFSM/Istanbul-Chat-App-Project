package yeni;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class fizik extends JFrame {

    private Thread thread1;
    private Trd1 runnable1;
    final String DENEYCESIDI;
    final double TSABITI;
    double saniyeDeger;
    final double SANIYESINIR;
    final double VPIL;
    int id = 1;
    Vector<Double> gerilimVeri = new Vector();

    public fizik(double Vpil, double Rdirenc, double Cfarad, String DeneyCesidi) {
        this.VPIL = Vpil;
        this.DENEYCESIDI = DeneyCesidi;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        TSABITI = (Rdirenc * 1000) * (Cfarad / 1000000);
        SANIYESINIR = 5 * TSABITI;
        initComponents();
        JOptionPane.showMessageDialog(this, "Hoşgeldiniz\n"
                + "Anahtar Kapatıldıktan Sonra Ölçüm Verileri Kaydedilecek.\n"
                + "Ölçüm Bittiğinde Kondansatör Voltaj-Zaman Grafiğini Görmek İçin\n "
                + "Grafik Çiz Butonuna Basınız");
    }

    public void stop() {
        if (thread1 != null) {
            if (!Thread.currentThread().isInterrupted()) {
                runnable1.terminate();
                return;
            }
            try {
                thread1.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    double[] VectorToArray(Vector<Double> v) {
        double[] result = new double[v.size()];
        for (int i = 0; i < v.size(); i++) {
            result[i] = v.get(i);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar2 = new javax.swing.JProgressBar();
        jProgressBar4 = new javax.swing.JProgressBar();
        jProgressBar8 = new javax.swing.JProgressBar();
        jProgressBar9 = new javax.swing.JProgressBar();
        jProgressBar10 = new javax.swing.JProgressBar();
        jProgressBar11 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        anahtar = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar3 = new javax.swing.JProgressBar();
        jProgressBar5 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        lblvolt = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tf1 = new javax.swing.JLabel();
        lblsaniye = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tf2 = new javax.swing.JLabel();
        GrafikCiz = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jProgressBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 30, 90));
        getContentPane().add(jProgressBar4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 150, 30));
        getContentPane().add(jProgressBar8, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, 130, 30));
        getContentPane().add(jProgressBar9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 250, 30));
        getContentPane().add(jProgressBar10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, 210, 30));
        getContentPane().add(jProgressBar11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 230, 30));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/direnc2.PNG"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 130, 30));

        anahtar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/anahtar-acik.PNG"))); // NOI18N
        anahtar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anahtarActionPerformed(evt);
            }
        });
        getContentPane().add(anahtar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 320, 100, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pil4.PNG"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 20, 110));
        getContentPane().add(jProgressBar3, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, 30, 270));
        getContentPane().add(jProgressBar5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 30, 150));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/kds.PNG"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 100, 30));

        lblvolt.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        lblvolt.setText("0");
        getContentPane().add(lblvolt, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 70, 140, 70));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/multimetre.PNG"))); // NOI18N
        jLabel4.setText("jLabel4");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 10, 200, 390));

        tf1.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        tf1.setText("0");
        getContentPane().add(tf1, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 70, 140, 70));

        lblsaniye.setBackground(new java.awt.Color(255, 255, 255));
        lblsaniye.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        lblsaniye.setForeground(new java.awt.Color(0, 0, 0));
        lblsaniye.setText("0");
        getContentPane().add(lblsaniye, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 100, 130, 80));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/kronometre.PNG"))); // NOI18N
        jLabel6.setText("jLabel6");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 220, -1));

        tf2.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        tf2.setText("0");
        getContentPane().add(tf2, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 70, 140, 70));

        GrafikCiz.setText("Grafik Çiz");
        GrafikCiz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GrafikCizActionPerformed(evt);
            }
        });
        getContentPane().add(GrafikCiz, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 250, 230, -1));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Start/Stop/Continue");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, 190, 30));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Başlat/Duraklat/Devam Et");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 360, 190, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void anahtarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anahtarActionPerformed
        if (anahtar.isSelected()) {
            anahtar.setIcon(new ImageIcon(getClass().getResource("/images/anahtar-kapali.PNG")));
            runnable1 = new Trd1();
            thread1 = new Thread(runnable1);
            thread1.start();
        } else {
            anahtar.setIcon(new ImageIcon(getClass().getResource("/images/anahtar-acik.PNG")));
            stop();
        }
    }//GEN-LAST:event_anahtarActionPerformed

    private void GrafikCizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GrafikCizActionPerformed
        if (gerilimVeri.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Yeterli Veri Yok");
            return;
        }

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        f.add(new GrafikCizici(VectorToArray(gerilimVeri)));
        f.setSize(400, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
    }//GEN-LAST:event_GrafikCizActionPerformed

    void cikisDosyaKaydet() {
        int decision = JOptionPane.showConfirmDialog(this, "Verileri Dosyaya Kaydetmek İstiyormusunuz ?");
        if (decision == JOptionPane.OK_OPTION) {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Dosyanın Kaydedileceği Yeri Seçin: ");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = jfc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selected = jfc.getCurrentDirectory();
                Path p = Paths.get(selected.getAbsolutePath() + "/OlcumSonuclar_" + new Random().nextInt(100) + ".txt");
                try {
                    Path p1 = Files.createFile(p);
                    PrintWriter pw = new PrintWriter(p1.toFile());
                    pw.println(gerilimVeri.toString());
                    pw.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Sistem Bu Klasöre Erişemiyor\nLütfen Tekrar Deneyiniz");
                    cikisDosyaKaydet();
                }
            }
        }
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cikisDosyaKaydet();
    }//GEN-LAST:event_formWindowClosing

    double DgerilimHesapla(double Vpil) {
        return Vpil * (1 - Math.pow(Math.E, -saniyeDeger / TSABITI));
    }

    double BgerilimHesapla(double volt) {
        return volt * (Math.pow(Math.E, -saniyeDeger / TSABITI));
    }

    public class Trd1 implements Runnable {

        private volatile boolean running = true;

        //double trsaniyeKalınanYer;
        public void terminate() {
            running = false;
        }

        void LblGuncelle(String text) {
            java.awt.EventQueue.invokeLater(() -> {
                lblvolt.setText(text);
                lblsaniye.setText(saniyeDeger + "");
            });
        }

        boolean onCharging() throws InterruptedException {
            while (running && saniyeDeger <= SANIYESINIR) {
                Thread.sleep(1000);
                saniyeDeger++;
                double sonuc = DgerilimHesapla(VPIL);
                LblGuncelle(sonuc + "");
                gerilimVeri.addElement(sonuc);
            }
            return true;
        }

        boolean onDisCharging() throws InterruptedException {
            while (running && saniyeDeger <= SANIYESINIR) {
                Thread.sleep(1000);
                saniyeDeger++;
                double sonuc = BgerilimHesapla(VPIL);
                LblGuncelle(sonuc + "");
                gerilimVeri.addElement(sonuc);
            }
            return true;
        }

        boolean GerilimHseapla() {
            try {
                return DENEYCESIDI.equals("Dolma Gerilimi") ? onCharging() : onDisCharging();
            } catch (InterruptedException e) {
                running = false;
            }
            return false;
        }

        @Override
        public void run() {
            GerilimHseapla();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton GrafikCiz;
    private javax.swing.JToggleButton anahtar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JProgressBar jProgressBar10;
    private javax.swing.JProgressBar jProgressBar11;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JProgressBar jProgressBar4;
    private javax.swing.JProgressBar jProgressBar5;
    private javax.swing.JProgressBar jProgressBar8;
    private javax.swing.JProgressBar jProgressBar9;
    private javax.swing.JLabel lblsaniye;
    private javax.swing.JLabel lblvolt;
    private javax.swing.JLabel tf1;
    private javax.swing.JLabel tf2;
    // End of variables declaration//GEN-END:variables
}
