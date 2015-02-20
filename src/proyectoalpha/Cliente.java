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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;


/**
 *
 * @author JGUTIERRGARC
 */
public class Cliente extends FrmCliente{
    
    public static Cliente c = new Cliente();
    
    public Cliente(){
      super();
    }
    
    /***
     * Separa las coordenadas, las convierte en enteros y pinta un monstruo en esa posición
     * @param pos Coordenadas enviadas por el servidor en formato <n,m>
     */
    public static void pintaMonstruo(String pos){
        pos = pos.trim();
        String coord[] = pos.split(",");
        Integer fila = Integer.parseInt(coord[0]);
        Integer columna = Integer.parseInt(coord[1]);
        c.putMonstruo(fila, columna);
        c.refresh();
    }
    
    public static void main(String args[]){
        
  	byte [] mensaje= new byte[1000];
        byte [] monstruo;
        boolean juegoFinalizado =false;
	MulticastSocket s =null;
        
        //iniciar el jFrame
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                c.setVisible(true);

            }
        });
        
   	try {              
            // Unirse al grupo Multicast
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group 
            s = new MulticastSocket(6789);
            s.joinGroup(group); 
            
            // Mientras no haya finalizado el juego
            while(!juegoFinalizado){
//                // Creacion del mensaje
//                String myMessage="Dame la hora";
               
//	    	DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
                DatagramPacket messageIn = new DatagramPacket(mensaje, mensaje.length);
                // Como es multicast, lo que esta enviando lo escuchan todos, es decir, se escucha a si mismo tambien al enviar un mensaje
                // Por eso ponemos que escuche su mismo mensaje (aunque no lo utilicemos).
//	    	s.send(messageOut);
//                s.receive(messageIn2);
                
                /* Escuchar y recibir un monstruo*/
                try {                    
                     s.receive(messageIn);
                     monstruo= (new String(messageIn.getData())).getBytes();
                     System.out.println("Recibi un monstruo en: "+ (new String(monstruo)));
                     
                    //Dibuja un monstruo en la posicion recibida por el servidor
                    String pos = new String(monstruo);
                    pintaMonstruo(pos);
                     
                     /* Mandar mensaje al servidor pegandole al monstruo */
                    
                     // HRG: El mensaje se debe mandar en el método onMonster Click de esta clase
                    
                     // Aqui modificar para que se mande mensaje de si le pego y donde le pego segun la interfaz de usuario(Si no dio tiempo de pegar mandar -1,-1.
                     
                     // Si el juego termino el servidor manda mensaje "Finalizo" y el ganador
                     if(new String(monstruo).compareTo("Finalizo")==0){
                         juegoFinalizado=true;
                         // Escuchar quien gano y desplegar el mensaje adecuado en la interfaz
                        s.receive(messageIn);
                        monstruo= (new String(messageIn.getData())).getBytes();
                        System.out.println("El ganador fue: "+ (new String(monstruo)));
                     }
                     
                     // Sino, pegarle al monstruo y mandar el mensaje adecuado al servidor VIA SOCKET, no Multicast
                     else{
                         
                     }
                     
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }   
            // Salir del grupo
            s.leaveGroup(group);		
 	}
        catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }
        // Cerrar el socket
        finally {
           if(s != null) s.close();
       }
    }		     

    
    @Override
    /***
     * Aquí tenemos que programar la respuesta al servidor TCP.
     */
    protected void onMonsterClick() {
        JOptionPane.showMessageDialog(null, "Te madreaste al monstruo" );
        System.out.println("Está super cool");
    }
}
