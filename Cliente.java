package chatcliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Cliente extends Thread {
    
    private Socket socketPrincipal;
       
    private ObjectOutputStream objectOutputStream;
       
    private ObjectInputStream objectInputStream;
           
    private final VentanaC ventanaCliente;    
    
    private String ID;
    
    private boolean boolEscucha;
    
    private final String host;
    
    private final int puerto;
   
    Cliente(VentanaC ventanaCliente, String host, Integer puerto, String nombre) {
        this.ventanaCliente=ventanaCliente;        
        this.host=host;
        this.puerto=puerto;
        this.ID=nombre;
        boolEscucha=true;
        this.start();
    }
    
    public void run(){
        try {
            socketPrincipal=new Socket(host, puerto);
            objectOutputStream=new ObjectOutputStream(socketPrincipal.getOutputStream());
            objectInputStream=new ObjectInputStream(socketPrincipal.getInputStream());
            System.out.println("Conexion exitosa!!!!");
            this.enviarSolicitudConexion(ID);
            this.escuchar();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventanaCliente, "Conexion imposible, servidor inexistente,\n");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventanaCliente, "Conexion imposible, puerto incorrecto");
            System.exit(0);
        }

    }
    
   
    public void desconectar(){
        try {
            objectOutputStream.close();
            objectInputStream.close();
            socketPrincipal.close();  
            boolEscucha=false;
        } catch (Exception e) {
            System.err.println("Error al cerrar cmunicacion del cliente.");
        }
    }
    
    public void enviarMensaje(String cliente_receptor, String mensaje){
        LinkedList<String> lista=new LinkedList<>();
        
        lista.add("MENSAJE");
        
        lista.add(ID);
        
        lista.add(cliente_receptor);
       
        lista.add(mensaje);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error I/O al enviar mensaje al servidor.");
        }
    }     
    public void escuchar() {
        try {
            while (boolEscucha) {
                Object aux = objectInputStream.readObject();
                if (aux != null) {
                    if (aux instanceof LinkedList) {                        
                        ejecutar((LinkedList<String>)aux);
                    } else {
                        System.err.println("Informacion por socket incorrecta");
                    }
                } else {
                    System.err.println("Elemento vacio recibido por socket");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventanaCliente, "Comunicacion con el servidor interrumpida");
            System.exit(0);
        }
    }
    
    public void ejecutar(LinkedList<String> lista){
        
        String tipo=lista.get(0);
        switch (tipo) {
            case "CONEXION_ACEPTADA":
                
                ID=lista.get(1);
                ventanaCliente.sesionIniciada(ID);
                for(int i=2;i<lista.size();i++){
                    ventanaCliente.addContacto(lista.get(i));
                }
                break;
            case "NUEVO_USUARIO_CONECTADO":
                
                ventanaCliente.addContacto(lista.get(1));
                break;
            case "USUARIO_DESCONECTADO":
              
                ventanaCliente.eliminarContacto(lista.get(1));
                break;                
            case "MENSAJE":
               
                ventanaCliente.addMensaje(lista.get(1), lista.get(3));
                break;
            default:
                break;
        }
    }
    
    private void enviarSolicitudConexion(String ID) {
        LinkedList<String> lista=new LinkedList<>();
        
        lista.add("SOLICITUD_CONEXION");
       
        lista.add(ID);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }
   
    void confirmarDesconexion() {
        LinkedList<String> lista=new LinkedList<>();
        
        lista.add("SOLICITUD_DESCONEXION");
        
        lista.add(ID);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }
    
    String getIdentificador() {
        return ID;
    }
}