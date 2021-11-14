package chatcliente;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaC extends javax.swing.JFrame {

    public VentanaC() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String ip_puerto_nombre[]=getIP_Puerto_Nombre();
        String ip=ip_puerto_nombre[0];
        String puerto=ip_puerto_nombre[1];
        String nombre=ip_puerto_nombre[2];
        cliente=new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtHistorial = new javax.swing.JTextArea();
        txtMensaje = new javax.swing.JTextField();
        cmbContactos = new javax.swing.JComboBox();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtHistorial.setEditable(false);
        txtHistorial.setColumns(20);
        txtHistorial.setRows(5);
        jScrollPane1.setViewportView(txtHistorial);

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel1.setText("Destinatario:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMensaje)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbContactos, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbContactos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnviar))
                .addContainerGap())
        );

        pack();
    }
    
    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        
        if(cmbContactos.getSelectedItem()==null){
            JOptionPane.showMessageDialog(this, "Escoja un destinatario valido o espere la conexion de uno nuevo.");        
            return;
        }
        String cliente_receptor=cmbContactos.getSelectedItem().toString();
        String mensaje=txtMensaje.getText();
        cliente.enviarMensaje(cliente_receptor, mensaje);
        
        txtHistorial.append("## Yo -> "+cliente_receptor+ " ## : \n" + mensaje+"\n");
        txtMensaje.setText("");
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cliente.confirmarDesconexion();
    }
    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JComboBox cmbContactos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtHistorial;
    private javax.swing.JTextField txtMensaje;
    
    private final String DEFAULT_PORT="10101";
       
    private final String DEFAULT_IP="127.0.0.1";
   
    private final Cliente cliente;
    
    void addContacto(String contacto) {
        cmbContactos.addItem(contacto);
    }
    
    void addMensaje(String emisor, String mensaje) {
        txtHistorial.append("##### "+emisor + " ##### : \n" + mensaje+"\n");
    }
    
    void sesionIniciada(String identificador) {
        this.setTitle(" --- "+identificador+" --- ");
    }
    
    private String[] getIP_Puerto_Nombre() {
        String s[]=new String[3];
        s[0]=DEFAULT_IP;
        s[1]=DEFAULT_PORT;
        JTextField ip = new JTextField(20);
        JTextField puerto = new JTextField(20);
        JTextField usuario = new JTextField(20);
        ip.setText(DEFAULT_IP);
        puerto.setText(DEFAULT_PORT);
        usuario.setText("Usuario");
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(3, 2));
        myPanel.add(new JLabel("IP del Servidor:"));
        myPanel.add(ip);
        myPanel.add(new JLabel("Puerto de la conexion:"));
        myPanel.add(puerto);
        myPanel.add(new JLabel("Escriba su nombre:"));
        myPanel.add(usuario);        
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Configuraciones de la comunicacion", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
                s[0]=ip.getText();
                s[1]=puerto.getText();
                s[2]=usuario.getText();
        }else{
            System.exit(0);
        }
        return s;
    }    
    
    void eliminarContacto(String identificador) {
        for (int i = 0; i < cmbContactos.getItemCount(); i++) {
            if(cmbContactos.getItemAt(i).toString().equals(identificador)){
                cmbContactos.removeItemAt(i);
                return;
            }
        }
    }
}
