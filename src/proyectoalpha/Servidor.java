/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoalpha;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JGUTIERRGARC
 */
public class Servidor {
    public static void main(String args[]){ 
  	String currentDate = (new Date()).toString();
        byte [] tiempo;
	MulticastSocket s =null;
        String mensajeLeido=null;
        String PosMonstruo=null;
   	try {
            
            // Unirse al grupo Multicast.
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group 
            s = new MulticastSocket(6789);
            s.joinGroup(group); 

            byte[] buffer = new byte[1000];
            // Siempre esta en ejecucion.
            while(true) {
                System.out.println("Waiting for messages");
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
//                s.receive(messageIn);          
//                mensajeLeido= new String(messageIn.getData()).trim();
//                System.out.println("Message: " + mensajeLeido+ " from: "+ messageIn.getAddress());
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
                
//                if(mensajeLeido.equals("Dame la hora")){                     
//                        tiempo= currentDate.getBytes();                                     
//                        DatagramPacket messageOut = new DatagramPacket(tiempo, tiempo.length, group, 6789);
//                        s.send(messageOut);
//                        // Como es multicast, lo que esta enviando lo escuchan todos, es decir, se escucha a si mismo tambien.
//                        // Por eso ponemos esto (aunque no se utilice el mensaje).
//                        s.receive(messageIn);
//                        System.out.println("Se envio la hora: " + new String(messageOut.getData())+ " a: "+ messageIn.getAddress());
//                }
                // Esperar 30 seg antes de mandar otro monstruo.
                try {
                    Thread.sleep(30000);
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
        // Cerrar el socket.
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
