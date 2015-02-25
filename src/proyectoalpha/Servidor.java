/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoalpha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fer Bonnin, Hipolito
 */
public class Servidor {
   
      @SuppressWarnings("empty-statement")
    public static void main(String args[]){ 
        // Variables     	
	MulticastSocket s =null;
        String PosMonstruo=null;
        byte[] buffer = new byte[1000];
        // Para TCP
        int serverPort = 7896; 
        ServerSocketChannel ssc = null;
        
   	try {
            
            // Unirse al grupo Multicast.
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group 
            s = new MulticastSocket(6789);
            s.joinGroup(group); 
            buffer = new byte[1000];
           
            /*TCP*/
             ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(serverPort));
            ssc.configureBlocking(false);
            
            // Siempre esta en ejecucion.
            while(true) {
                System.out.println("Waiting for messages");   
                /*UDP*/        
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

                /* Mandar un nuevo monstruo. */
                // Obtener posicion aleatoria para el monstruo
                PosMonstruo= posicionAleatoria();
                byte [] monstruo = PosMonstruo.getBytes();
                DatagramPacket messageOut = new DatagramPacket(monstruo,monstruo.length, group, 6789);
                s.send(messageOut);
                // Como es multicast, lo que esta enviando lo escuchan todos, es decir, se escucha a si mismo tambien.
                // Por eso ponemos esto (aunque no se utilice el mensaje).
                s.receive(messageIn);
                System.out.println("Se envio un monstruo en la posicion: " + new String(messageOut.getData()));
                
                /*Escuchar las respuestas y esperar 30 segundos antes de mandar otro monstruo*/
                try {                   
                    // Para TCP : non blocking Socket  ( el visto en clase se bloquea hasta que hay una conexion)
                    SocketChannel sc = ssc.accept();
                    /*Escuchar respuestas*/
                    // Aqui modificar:
                    // 1) el while tiene que ser en un lapso de tiempo (no hasta que contesten todos) y cuando ya conteste alguien
                        // algo asi como while(sc!=null &&!contestoAlguien && tiempo <20seg)
                    
                    //dentro de la conexion c.start llama a una funcion Conexion que es multithread:
                        // 2) crear un arreglo que reciba de que cliente se recibió el primer mensaje para darle a el el puntaje
                            // un arreglo de Strings que busque ( o agregue) la ip del que le pego primero al monstruo
                        // 3) mandar feedback al cliente. 
                   while(sc!=null){
                        System.out.println("Received an incoming connection from " + sc.socket().getRemoteSocketAddress());                    
                        Connection c = new Connection(sc.socket());
                        c.start();
                        sc = ssc.accept();
                   }
                   Thread.sleep(3000);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
  	     } 	    
        }
         catch (SocketException e){
             System.out.println("Socket: " + e.getMessage());
	 }
         catch (IOException e){
             System.out.println("IO: " + e.getMessage());
         }
        // Cerrar el socket Multicast
	 finally {
            if(s != null) s.close();
        }
    }

    /* Funcion para obtener dos numeros aleatorios entre 0 y 3 con el formato a,b */
    public static String posicionAleatoria(){
        Random  rand = new Random();
        String numAleatorio;
        int num1=(int)(rand.nextDouble() * 4 + 0);
        int num2=(int)(rand.nextDouble() * 4 + 0);
        numAleatorio=""+num1+","+num2;
        return numAleatorio;
    }
}

     /*Funcion para hacer concurrente el servidor con threads*/
   // EL SERVIDOR ES CONCURRENTE si estableces hilos.
   // Sino solo tendrias un canal de entrada y salida para un solo cliente.
    class Connection extends Thread {
        
            DataInputStream in;
            DataOutputStream out;
            Socket clientSocket;
            // Conexion al socket
            public Connection (Socket aClientSocket) {
                try {
                    clientSocket = aClientSocket;
                    in = new DataInputStream(clientSocket.getInputStream());
                    out =new DataOutputStream(clientSocket.getOutputStream());
                } 
                catch(IOException e)  {System.out.println("Connection:"+e.getMessage());}
            }
            
            // Run para escuchar al cliente
            @Override
            public void run(){
                int pos= 0;
                try {			             
                    // Aqui falta decidir quien le pego primero al monstruo para decirles si le pegaron o no (feedback al usuario)
                    String data = in.readUTF();
                    // Llega el mensaje y lo imprime
                    System.out.println("Message received from: " + clientSocket.getRemoteSocketAddress()+ " message is "+ data);
                } 
                catch(EOFException e) {
                    System.out.println("EOF:"+e.getMessage());
                } 
                catch(IOException e) {
                    System.out.println("IO:"+e.getMessage());
                } 
                // Cerrar socket TCP
                finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e){
                        System.out.println(e);
                    }
                    }
                }
    }
   
