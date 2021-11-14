package chatservidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;


public class HiloCliente extends Thread{
    
    private final Socket socketPrincipal;    
     
    private ObjectOutputStream objectOutputStream;
    
    private ObjectInputStream objectInputStream;            
           
    private final Servidor server;
   
    private String ID;
    
    private boolean boolEscucha;
   
    public HiloCliente(Socket socketPrincipal,Servidor server) {
        this.server=server;
        this.socketPrincipal = socketPrincipal;
        try {
            objectOutputStream = new ObjectOutputStream(socketPrincipal.getOutputStream());
            objectInputStream = new ObjectInputStream(socketPrincipal.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error ObjectOutputStream y el ObjectInputStream");
        }
    }
        
    public void desconnectar() {
        try {
            socketPrincipal.close();
            boolEscucha=false;
        } catch (IOException ex) {
            System.err.println("Error con el socket del cliente.");
        }
    }
            
    public void run() {
        try{
            escuchar();
        } catch (Exception ex) {
            System.err.println("Error I/O del hilo del cliente.");
        }
        desconnectar();
    }
        
          
    public void escuchar(){        
        boolEscucha=true;
        while(boolEscucha){
            try {
                Object aux=objectInputStream.readObject();
                if(aux instanceof LinkedList){
                    ejecutar((LinkedList<String>)aux);
                }
            } catch (Exception e) {                    
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }
          
    public void ejecutar(LinkedList<String> lista){
        
        String tipo=lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
               
                confirmarConexion(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                
                confirmarDesConexion();
                break;                
            case "MENSAJE":
                
                String destinatario=lista.get(2);
                server.clientes
                        .stream()
                        .filter(h -> (destinatario.equals(h.getIdentificador())))
                        .forEach((h) -> h.enviarMensaje(lista));
                break;
            default:
                break;
        }
    }
        
    private void enviarMensaje(LinkedList<String> lista){
        try {
            objectOutputStream.writeObject(lista);            
        } catch (Exception e) {
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }    
    
    private void confirmarConexion(String ID) {
        Servidor.correlativo++;
        this.ID=Servidor.correlativo+" - "+ID;
        LinkedList<String> lista=new LinkedList<>();
        lista.add("CONEXION EXITOSA!");
        lista.add(this.ID);
        lista.addAll(server.getUsuariosConectados());
        enviarMensaje(lista);
        server.agregarLog("\nNuevo cliente: "+this.ID);
       
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("USUARIO NUEVO CONECTADO");
        auxLista.add(this.ID);
        server.clientes
                .stream()
                .forEach(cliente -> cliente.enviarMensaje(auxLista));
        server.clientes.add(this);
    }
    
    public String getIdentificador() {
        return ID;
    }
    
    private void confirmarDesConexion() {
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("USUARIO DESCONECTADO!");
        auxLista.add(this.ID);
        server.agregarLog("\n"+this.ID+"\" se ha desconectado.");
        this.desconnectar();
        for(int i=0;i<server.clientes.size();i++){
            if(server.clientes.get(i).equals(this)){
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes
                .stream()
                .forEach(h -> h.enviarMensaje(auxLista));        
    }
}