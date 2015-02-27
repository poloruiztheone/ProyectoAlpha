/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoalpha;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Fer Bonnin, Hipolito
 */
public class Cliente extends FrmCliente {

    public static Cliente c = new Cliente();
    public static String user;

    public Cliente() {
        super();
    }

    /**
     * *
     * Separa las coordenadas, las convierte en enteros y pinta un monstruo en
     * esa posición
     *
     * @param pos Coordenadas enviadas por el servidor en formato <n,m>
     */
    public static void pintaMonstruo(String pos) {
        pos = pos.trim();
        String coord[] = pos.split(",");
        Integer fila = Integer.parseInt(coord[0]);
        Integer columna = Integer.parseInt(coord[1]);
        c.putMonstruo(fila, columna);
        c.refresh();
    }

    public static void main(String args[]) throws IOException {

        // Variables 
        byte[] mensaje = new byte[1000];
        byte[] monstruo;
        boolean juegoFinalizado = false;
        int portMulticast = 6788;

        // Variables Multicast
        MulticastSocket s = null;
        Scanner lee = new Scanner(System.in);
        System.out.println("Nombre de jugador:");
        String userName = lee.next();
        buscarJuego(userName);
        jLabel1.setText(userName);
        user = userName;

        //iniciar el jFrame
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                c.setVisible(true);

            }
        });

        try {
            /*UDP*/
            // Unirse al grupo Multicast
            InetAddress group = InetAddress.getByName("228.5.6.10"); // destination multicast group 
            s = new MulticastSocket(portMulticast);
            s.joinGroup(group);

            // Mientras no haya finalizado el juego
            while (true) {
                mensaje = new byte[1000];
//	    	DatagramPacket messageOut = new DatagramPacket(m, m.length, group, portMulticast);
                DatagramPacket messageIn = new DatagramPacket(mensaje, mensaje.length);

                /* Escuchar y recibir un monstruo*/
                try {
                    s.receive(messageIn);
                    monstruo = (new String(messageIn.getData())).getBytes();
                    System.out.println("Recibi un monstruo en: " + (new String(monstruo)));

                     // Aqui modificar para que se mande mensaje de si le pego y donde le pego segun la interfaz de usuario(Si no dio tiempo de pegar mandar -1,-1.
                     /*UDP*/
                    // Si el juego termino el servidor manda mensaje "Finalizo" y el ganador
                    
                    if (new String(monstruo).contains("Finalizo:")) {
                        //juegoFinalizado = true;
                        // Escuchar quien gano y desplegar el mensaje adecuado en la interfaz
                        //s.receive(messageIn);
                        //monstruo= (new String(messageIn.getData())).getBytes();
                        System.out.println("El ganador fue: " + new String(monstruo).replace("Finalizo: ", ""));
                        JOptionPane.showMessageDialog(null, "El ganador fue: " + new String(monstruo).replace("Finalizo: ", "").trim());
                        JOptionPane.showMessageDialog(null, "Comienza un nuevo juego ");
                        //System.exit(0);
                    } else {

                        //Dibuja un monstruo en la posicion recibida por el servidor
                        String pos = new String(monstruo);
                        pintaMonstruo(pos);
                    }
                     // HRG: El mensaje se debe mandar en el método onMonster Click de esta clase

                } catch (IOException e) {
                    System.out.println("Error Lec: " + e.getMessage());
                }
            }
            // Salir del grupo
            //s.leaveGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } // Cerrar el socket
        finally {
            if (s != null) {
                s.close();
            }

        }
    }

    private static void buscarJuego(String userName) {
        Socket stcp = null;
        int serverPort = 7897;

        try {
            stcp = new Socket("localhost", serverPort);
            DataOutputStream out = new DataOutputStream(stcp.getOutputStream());
            out.writeUTF(userName);

            if (stcp != null) {
                stcp.close();
            }
            //JOptionPane.showMessageDialog(null, "Le pegaste al monstruo" );
            System.out.println("Te conectaste al juego");
        } catch (Exception ex) {
            System.out.println("No se pudo conectar al juego");
            System.exit(0);
        }
    }

    /**
     * *
     * Pegarle al monstruo y mandar el mensaje adecuado al servidor VIA SOCKET
     */
    /*TCP*/
    @Override
    protected void onMonsterClick() {
        // Variables TCP
        Socket stcp = null;
        int serverPort = 7896;

        try {
            stcp = new Socket("localhost", serverPort);
            DataOutputStream out = new DataOutputStream(stcp.getOutputStream());
            out.writeUTF(user);
            if (stcp != null) {
                stcp.close();
            }
            //JOptionPane.showMessageDialog(null, "Le pegaste al monstruo" );
            System.out.println("Le pegaste al monstruo");
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
