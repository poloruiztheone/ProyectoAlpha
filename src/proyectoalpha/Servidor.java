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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fer Bonnin, Hipolito
 */
public class Servidor {
   
    static Lock lock = new ReentrantLock();
    
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
            
            /**
             * Este bloque sirve para esperar un tiempo a que se 
             * conecten todos los jugadores que quieran
             */
            long tiempoInicio = System.currentTimeMillis();
            long tiempoActual = System.currentTimeMillis();
            int tiempoEsperaSegundos = 10;
            
            HiloConexionInicial hci = new HiloConexionInicial();
            hci.start();
            while((tiempoActual - tiempoInicio) < (tiempoEsperaSegundos * 1000)){
                //Este while bloquea la ejecucion principal 1 minuto para
                // dar tiempo a que se conecten los jugadores
                if (!hci.isAlive()){
                    Connection.jugadores.add(hci.jugador);
                    hci = new HiloConexionInicial();
                    hci.start();
                }
                System.out.println("Esperando jugadores, transcurrido: " + (tiempoActual - tiempoInicio));
                tiempoActual = System.currentTimeMillis();
                Thread.sleep(1000);
            }
            hci.join(100);
            hci.closeAll();
            System.out.println(Connection.jugadores.size());
            for (int i = 0; i < Connection.jugadores.size(); i++){
                Connection.puntuacion.add(0);
            }
            // ----------------------------------------------------
            
            // Unirse al grupo Multicast.
            InetAddress group = InetAddress.getByName("228.5.6.7"); // destination multicast group 
            s = new MulticastSocket(6789);
            s.joinGroup(group); 
            buffer = new byte[1000];
           
            
            /*TCP*/
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(serverPort));
            ssc.configureBlocking(false);
            
            
            SocketChannel sc = null;
            Connection c = null; 
            //Antes de instaciar Connection se tiene que llenar el arreglo de jugadores y de puntuacion
     
            final int CANTIDAD_MONSTRUOS = 5;
            int contador = 0;
            // Siempre esta en ejecucion.
            while(contador < CANTIDAD_MONSTRUOS) {
                //System.out.println("Waiting for messages");   
                /*UDP*/        
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

                /* Mandar un nuevo monstruo. */
                // Obtener posicion aleatoria para el monstruo
                PosMonstruo = posicionAleatoria();
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
                    
                    sc = ssc.accept();
                    /*Escuchar respuestas*/
                    // Aqui modificar:
                    // 1) el while tiene que ser en un lapso de tiempo (no hasta que contesten todos) y cuando ya conteste alguien
                        // algo asi como while(sc!=null &&!contestoAlguien && tiempo <20seg)
                    
                    //dentro de la conexion c.start llama a una funcion Conexion que es multithread:
                        // 2) crear un arreglo que reciba de que cliente se recibió el primer mensaje para darle a el el puntaje
                            // un arreglo de Strings que busque ( o agregue) la ip del que le pego primero al monstruo
                        // 3) mandar feedback al cliente. 
                    lock.lock();
                   Connection.isFirst = true; //para indicarle que es
                   lock.unlock();
                   while(sc!=null){
                        //System.out.println("Received an incoming connection from " + sc.socket().getRemoteSocketAddress());                    
                        c = new Connection(sc.socket());
                        c.start();
                        sc = ssc.accept();
                   }
                   Thread.sleep(3000);
                   //System.out.println("Aqui se debe enviar, quien le pego primero y el score");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                contador++;
  	     }
            //Cuando se sale del while se terminaba la ejecucion sin escuchar la ultima respuesta 
            //Por eso agregue de nuevo el escuchador
            lock.lock();
            Connection.isFirst = true; 
            lock.unlock();
            sc = ssc.accept();
            while(sc!=null){
                //System.out.println("Received an incoming connection from " + sc.socket().getRemoteSocketAddress());                    
                c = new Connection(sc.socket());
                c.start();
                sc = ssc.accept();
            }
            c.join();
            //----------------------
            int indice_ganador = 0;
            for (int i = 0; i < Connection.puntuacion.size(); i++){
                System.out.println(Connection.jugadores.get(i) + " le pego a " + Connection.puntuacion.get(i));
                if  (Connection.puntuacion.get(i) > Connection.puntuacion.get(indice_ganador))
                {
                    indice_ganador = i;
                }
            }
            
            
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            String enviar = "Finalizo: ";
            enviar += Connection.jugadores.get(indice_ganador);
            byte [] monstruo = enviar.getBytes();
            DatagramPacket messageOut = new DatagramPacket(monstruo, monstruo.length, group, 6789);
            s.send(messageOut);
            // Como es multicast, lo que esta enviando lo escuchan todos, es decir, se escucha a si mismo tambien.
            // Por eso ponemos esto (aunque no se utilice el mensaje).
            s.receive(messageIn);

            System.out.println(new String(messageOut.getData()));            

        }
         catch (SocketException e){
             System.out.println("Socket: " + e.getMessage());
	 }
         catch (IOException e){
             System.out.println("IO: " + e.getMessage());
         } catch (InterruptedException ex) { 
              Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
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

class HiloConexionInicial extends Thread {
        
            DataInputStream in;
            DataOutputStream out;
            Socket clientSocket = null;
            int serverPort = 7897; 
            ServerSocketChannel ssc = null;
            SocketChannel sc = null;
            public String jugador = null;
            
            // Run para escuchar al cliente
            @Override
            public void run(){
                int pos= 0;
                try {	
                    ssc = ServerSocketChannel.open();
                    ssc.socket().bind(new InetSocketAddress(serverPort));
                    ssc.configureBlocking(true); //Estamos esperando que se conecte un jugador
                    sc = ssc.accept();
                    
                    clientSocket = sc.socket();
                    
                    in = new DataInputStream(clientSocket.getInputStream());
                    out =new DataOutputStream(clientSocket.getOutputStream());
                    // Aqui falta decidir quien le pego primero al monstruo para decirles si le pegaron o no (feedback al usuario)
                    String data = in.readUTF();
                    // Llega el mensaje y lo imprime
                    System.out.println("Desde: " + clientSocket.getRemoteSocketAddress()+ " se conecto "+ data);
                    jugador = data;
                } 
                catch(EOFException e) {
                    System.out.println("EOF HiloConexionInicial:"+e.getMessage());
                } 
                catch(IOException e) {
                    System.out.println("IO HiloConexionInicial:"+e.getMessage());
                } 
                // Cerrar socket TCP
                finally {
                    closeAll();
                }
            }
            
            public void closeAll(){
                try {
                    if (clientSocket != null) clientSocket.close();
                    if (sc != null) sc.close();
                    if (ssc != null) ssc.close();
                } catch (IOException e){
                    System.out.println(e);
                }
            }
    }
   

     /*Funcion para hacer concurrente el servidor con threads*/
   // EL SERVIDOR ES CONCURRENTE si estableces hilos.
   // Sino solo tendrias un canal de entrada y salida para un solo cliente.
class Connection extends Thread {
        
            Lock lock = new ReentrantLock();
            public static ArrayList<Integer> puntuacion = new ArrayList<Integer>();
            public static ArrayList<String> jugadores = new ArrayList<String>();
            public static boolean isFirst;
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
                    if (isFirst){
                        lock.lock();
                        int indice = jugadores.indexOf(data);
                        if (indice != -1){
                            puntuacion.set(indice, puntuacion.get(indice)+1);
                            isFirst = false;
                        }
                        lock.unlock();
                        System.out.println(" Primero en pegar "+ data);
                    }
                    
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
   
