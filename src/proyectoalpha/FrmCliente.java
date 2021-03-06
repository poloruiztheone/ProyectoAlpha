/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoalpha;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Polo
 */
public abstract class FrmCliente extends javax.swing.JFrame {

    //Matriz para controlar la aparicion del monstruo
    protected static javax.swing.JLabel[][] image_matrix;
    //Para saber donde esta el monstruo sin recorrer toda la matriz
    private int filaMonstruo = -1; 
    private int columnaMonstruo = -1;
    
    /**
     * Creates new form FrmCliente
     */
    public FrmCliente() {
        initComponents();
        initImageMatrix();
    }
    
    //Dibuja un monstruo y quita el monstruo anterior, lo ocupamos cuando se recibe el dato desde el servidor
    protected void putMonstruo(int fila, int columna)
    {
        if (filaMonstruo != -1 && columnaMonstruo != -1)
        {
            image_matrix[filaMonstruo][columnaMonstruo].setToolTipText("hoyo");
            image_matrix[filaMonstruo][columnaMonstruo].setIcon((new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))));
        }
        image_matrix[fila][columna].setToolTipText("monstruo");
        image_matrix[fila][columna].setIcon((new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/monstruos-sa-1 2copia.gif"))));
        filaMonstruo = fila;
        columnaMonstruo = columna;
    }
    
    //Se utiliza cuando se pone una nueva imagen para refrescar la ventana
    protected void refresh()
    {
        for (int i = 0; i < image_matrix.length; i++)
        {
            for (int j = 0; j < image_matrix[i].length; j++)
            {
                
                image_matrix[i][j].revalidate();
                image_matrix[i][j].repaint();
            }
        }

        
    }
    
    protected abstract void onMonsterClick();
    
    //La propiedad icon de todos los jlabel ya tienen asignada la imagen del agujero
    private void initImageMatrix(){
        //Ponemos todos los jLabels en una matriz
        image_matrix = new javax.swing.JLabel[][]{
            { im_0_0, im_0_1, im_0_2, im_0_3 },
            { im_1_0, im_1_1, im_1_2, im_1_3 },
            { im_2_0, im_2_1, im_2_2, im_2_3 },
            { im_3_0, im_3_1, im_3_2, im_3_3 }
        };
        //Agremamos escuchadores de click para todas las imagenes en la matriz
        for (int i = 0; i < image_matrix.length; i++)
        {
            for (int j = 0; j < image_matrix[i].length; j++)
            {
                image_matrix[i][j].addMouseListener(new MouseAdapter() {
                    
                    public void mouseClicked(MouseEvent me) {
                    //System.out.println(me.getSource().toString());
                    JLabel clicked = (JLabel) me.getSource();
                        if (clicked.getToolTipText().equals("monstruo"))
                        {
                            clicked.setIcon((new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/monstruos-sa-1 2rojo.gif"))));
                            onMonsterClick();
                        }
                    }
                });
            }
        }
    }
            


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        im_0_0 = new javax.swing.JLabel();
        im_0_1 = new javax.swing.JLabel();
        im_0_3 = new javax.swing.JLabel();
        im_0_2 = new javax.swing.JLabel();
        im_1_3 = new javax.swing.JLabel();
        im_1_1 = new javax.swing.JLabel();
        im_1_0 = new javax.swing.JLabel();
        im_1_2 = new javax.swing.JLabel();
        im_3_1 = new javax.swing.JLabel();
        im_2_3 = new javax.swing.JLabel();
        im_2_1 = new javax.swing.JLabel();
        im_2_0 = new javax.swing.JLabel();
        im_3_3 = new javax.swing.JLabel();
        im_2_2 = new javax.swing.JLabel();
        im_3_0 = new javax.swing.JLabel();
        im_3_2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        im_0_0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_0_0.setToolTipText("hoyo");
        im_0_0.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        im_0_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_0_1.setToolTipText("hoyo");

        im_0_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_0_3.setToolTipText("hoyo");

        im_0_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_0_2.setToolTipText("hoyo");

        im_1_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_1_3.setToolTipText("hoyo");

        im_1_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_1_1.setToolTipText("hoyo");

        im_1_0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_1_0.setToolTipText("hoyo");

        im_1_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_1_2.setToolTipText("hoyo");

        im_3_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_3_1.setToolTipText("hoyo");

        im_2_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_2_3.setToolTipText("hoyo");

        im_2_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_2_1.setToolTipText("hoyo");

        im_2_0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_2_0.setToolTipText("hoyo");

        im_3_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_3_3.setToolTipText("hoyo");

        im_2_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_2_2.setToolTipText("hoyo");

        im_3_0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_3_0.setToolTipText("hoyo");

        im_3_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoalpha/hole-in-the-wall.png.625x385_q100.png"))); // NOI18N
        im_3_2.setToolTipText("hoyo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(im_3_0)
                                .addGap(18, 18, 18)
                                .addComponent(im_3_1)
                                .addGap(18, 18, 18)
                                .addComponent(im_3_2)
                                .addGap(18, 18, 18)
                                .addComponent(im_3_3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(im_2_0)
                                .addGap(18, 18, 18)
                                .addComponent(im_2_1)
                                .addGap(18, 18, 18)
                                .addComponent(im_2_2)
                                .addGap(18, 18, 18)
                                .addComponent(im_2_3)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(im_1_0)
                                .addGap(18, 18, 18)
                                .addComponent(im_1_1)
                                .addGap(18, 18, 18)
                                .addComponent(im_1_2)
                                .addGap(18, 18, 18)
                                .addComponent(im_1_3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(im_0_0)
                                .addGap(18, 18, 18)
                                .addComponent(im_0_1)
                                .addGap(18, 18, 18)
                                .addComponent(im_0_2)
                                .addGap(18, 18, 18)
                                .addComponent(im_0_3)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(im_0_3)
                    .addComponent(im_0_1)
                    .addComponent(im_0_0)
                    .addComponent(im_0_2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(im_1_3)
                    .addComponent(im_1_1)
                    .addComponent(im_1_0)
                    .addComponent(im_1_2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(im_2_3)
                    .addComponent(im_2_1)
                    .addComponent(im_2_0)
                    .addComponent(im_2_2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(im_3_3)
                    .addComponent(im_3_1)
                    .addComponent(im_3_0)
                    .addComponent(im_3_2))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected static javax.swing.JLabel im_0_0;
    protected static javax.swing.JLabel im_0_1;
    protected static javax.swing.JLabel im_0_2;
    protected static javax.swing.JLabel im_0_3;
    protected static javax.swing.JLabel im_1_0;
    protected static javax.swing.JLabel im_1_1;
    protected static javax.swing.JLabel im_1_2;
    protected static javax.swing.JLabel im_1_3;
    protected static javax.swing.JLabel im_2_0;
    protected static javax.swing.JLabel im_2_1;
    protected static javax.swing.JLabel im_2_2;
    protected static javax.swing.JLabel im_2_3;
    protected static javax.swing.JLabel im_3_0;
    protected static javax.swing.JLabel im_3_1;
    protected static javax.swing.JLabel im_3_2;
    protected static javax.swing.JLabel im_3_3;
    protected static javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
