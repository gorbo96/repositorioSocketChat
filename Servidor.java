package chatservidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;


public class Servidor extends Thread{    
   
    private ServerSocket serverSocket;
    
    LinkedList<HiloCliente> clientes;
    
    private final VentanaS ventanaServidor;
    
    private final String puertoPrincipal;
    
    static int correlativo;
    
    public Servidor(String puertoPrincipal, VentanaS ventanaServidor) {
        correlativo=0;
        this.puertoPrincipal=puertoPrincipal;
        this.ventanaServidor=ventanaServidor;
        clientes=new LinkedList<>();
        this.start();
    }
      
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.valueOf(puertoPrincipal));
            ventanaServidor.addServidorIniciado();
            while (true) {
                HiloCliente h;
                Socket socket;
                socket = serverSocket.accept();
                System.out.println("Nueva conexion entrante: "+socket);
                h=new HiloCliente(socket, this);               
                h.start();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventanaServidor, "El servidor no se ha podido iniciar");
            System.exit(0);
        }                
    }        
    
    LinkedList<String> getUsuariosConectados() {
        LinkedList<String>usuariosConectados=new LinkedList<>();
        clientes.stream().forEach(c -> usuariosConectados.add(c.getIdentificador()));
        return usuariosConectados;
    }
    
    void agregarLog(String texto) {
        ventanaServidor.agregarLog(texto);
    }
}
