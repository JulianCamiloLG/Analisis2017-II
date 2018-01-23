/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;
import logica.LogicaMinas;

/**
 *
 * @author JulianCamilo
 */
public class FramePruebasMinas extends javax.swing.JFrame {
    
    private boolean esEntrada;
    private int cantMinas;
    private LogicaMinas logicaMinas;
    private int xMina;
    private int yMina;
    private LinkedList<String> detalles;
    
      /**
     * Creates new form FramePruebasMinas
     */
    public FramePruebasMinas() {
        initComponents();
        this.esEntrada=false;
        this.logicaMinas= new LogicaMinas();
        this.cantMinas=1;
        this.xMina=40;
        this.yMina=40;
        this.detalles= new LinkedList<>();
    }

    
    public void crearMina(int tamaño, String material, int minerosMaximos) {

        String numeroPanel;
        int mina[][] = new int[tamaño][tamaño];
        int xaux = xMina;
        int yaux = yMina;
        int xtotal = xMina;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                numeroPanel = "" + i + "-" + j + "-" + "Mina " + cantMinas;
                mina[i][j] = 0;
                crearPanel(xaux, yaux, numeroPanel);
                xaux += 40;
            }
            xtotal += 40;
            xaux = xMina;
            yaux += 40;
        }
        logicaMinas.crearMinaIniciale(mina, tamaño, material, minerosMaximos, "Mina " + cantMinas);
        xMina = xtotal + 40;
        crearDetalleMina(material,xtotal,yaux);
    }
    
    public void crearPanel(int x, int y, String nombre){
        Prueba panel = new Prueba();
        panel.setBounds(x, y, 40, 40);
        panel.setName(nombre);
        panel.setFocusable(true);
        configurarEventosPanel(panel);
        this.fondo2.add(panel);
    }
    
    public void configurarEventosPanel(Prueba panel) {
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3){
                    mostrarOpciones(panel);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
    
    public void mostrarOpciones(Prueba panel) {
        JPopupMenu popup = new JPopupMenu();
        JButton botonDeposito = new JButton("convertir en deposito");
        JButton botonCamino = new JButton("convertir en camino");
        JButton botonReset = new JButton("Cancelar");

        botonDeposito.addActionListener((ActionEvent e) -> {
            convertirDeposito(panel);
            popup.setVisible(false);
        });
        botonCamino.addActionListener((ActionEvent e) -> {
            convertirCamino(panel);
            popup.setVisible(false);
        });
        botonReset.addActionListener((ActionEvent e) -> {
            resetPanel(panel);
            popup.setVisible(false);
        });
        popup.add(botonDeposito);
        popup.add(botonCamino);
        popup.add(botonReset);
        if (!esEntrada) {
            JButton botonEntrada = new JButton("convertir en entrada");
            botonEntrada.addActionListener((ActionEvent e) -> {
                convertirEntrada(panel);
                popup.setVisible(false);
            });
            popup.add(botonEntrada);
        }

        popup.show(panel, 20, 0);

    }
    
    private void convertirDeposito(Prueba panel) {
        int cantidadDeposito = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el total de material en este deposito"));
        String[] datos=ArrayDatosPanel(panel);
        boolean result=logicaMinas.crearNuevoDeposito(cantidadDeposito,Integer.parseInt(datos[1]),Integer.parseInt(datos[2]),datos[0]);
        if (result) {
            cambiarfondo(2, panel);
        } else {
            JOptionPane.showMessageDialog(rootPane, "ERROR: Exceso en la cantidad de mineral en el deposito");
        }

    }
    
    private void convertirCamino(Prueba panel){
        String[] datos=ArrayDatosPanel(panel);
        boolean result=logicaMinas.crearCamino(datos[0],Integer.parseInt(datos[1]),Integer.parseInt(datos[2]));
        if (result) {
            cambiarfondo(1,panel);
        } else {
            JOptionPane.showMessageDialog(rootPane, "ERROR: No se pudo crear el camino");
        }
        
    }
    
    private void convertirEntrada(Prueba panel){
        String[] datos=ArrayDatosPanel(panel);
        boolean result=logicaMinas.crearEntrada(datos[0],Integer.parseInt(datos[1]),Integer.parseInt(datos[2]));
        if (result) {
            cambiarfondo(3,panel);
        } else {
            JOptionPane.showMessageDialog(rootPane, "ERROR: No se pudo crear la entrada");
        }
        
    }
    
    private void resetPanel(Prueba panel){
        String[] datos=ArrayDatosPanel(panel);
        boolean result=logicaMinas.cancelarCambio(datos[0],Integer.parseInt(datos[1]),Integer.parseInt(datos[2]));
        if (result) {
            cambiarfondo(0,panel);
        } else {
            JOptionPane.showMessageDialog(rootPane, "ERROR: No se pudo revertir el cambio");
        }
        
    }
    
    private String[] ArrayDatosPanel(Prueba panel) {
        String[] datos = new String[3];
        String[] indices = panel.getName().split("-");
        String posicion_i_matriz = indices[0];
        String posicion_j_matriz = indices[1];
        String nombreMina = indices[2];

        datos[0] = nombreMina;
        datos[1] = posicion_i_matriz;
        datos[2] = posicion_j_matriz;

        return datos;
    }
    
    
    private void cambiarfondo(int fondo, Prueba panel){
        if(fondo==3){
            esEntrada=true;
        }
        panel.setFondo(fondo);
        panel.repaint();
    }
    
    private void crearDetalleMina(String material, int xtotal, int yaux){
        this.detalles.add("Mina "+cantMinas+" de "+material+"-"+xtotal+"-"+yaux);
        this.fondo2.setDetalle(detalles);
        this.fondo2.setDetalleMina(true);
        this.repaint();
        this.fondo2.setDetalleMina(false);
        cantMinas++;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        fondo2 = new vistas.Fondo();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minas El Álamo");

        jButton1.setText("Crear Mina");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fondo2Layout = new javax.swing.GroupLayout(fondo2);
        fondo2.setLayout(fondo2Layout);
        fondo2Layout.setHorizontalGroup(
            fondo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );
        fondo2Layout.setVerticalGroup(
            fondo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGap(18, 18, 18)
                .addComponent(fondo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fondo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(36, 36, 36)
                        .addComponent(jButton2)
                        .addGap(0, 134, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Minas El Álamo");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int tamaño=Integer.parseInt(JOptionPane.showInputDialog("Digite el tamaño de la mina"));
        String material =JOptionPane.showInputDialog("Digite el material de la mina");
        int mineros =Integer.parseInt(JOptionPane.showInputDialog("Ingrese el maximo de mineros para esta mina"));
        crearMina(tamaño,material, mineros);
        this.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        JPopupMenu popup = new JPopupMenu();
        JPanel itemdeprueba = new JPanel();
        //itemdeprueba.setBorder(BorderFactory.createEtchedBorder(0, Color.DARK_GRAY, Color.BLUE));
        try {
            BufferedImage mimagen = ImageIO.read(new File("src/img/entrada.jpg"));
            
            Image reescalada = mimagen.getScaledInstance(40, 40, 0);
            JLabel picLabel = new JLabel(new ImageIcon(reescalada));
            picLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //System.out.println("clicked");
                    if (e.getButton() == MouseEvent.BUTTON1)
                        System.out.println(picLabel.getBounds());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    JComponent jc = (JComponent)e.getSource();
                    TransferHandler th = jc.getTransferHandler();
                    th.exportAsDrag(jc, e, TransferHandler.COPY);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    //System.out.println("realesed");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //System.out.println("entered");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //System.out.println("exit"); //To change body of generated methods, choose Tools | Templates.
                }
            });
            picLabel.setTransferHandler(new TransferHandler("icon"));
            itemdeprueba.add(picLabel);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "No se pudo cargar la imagen");
        }
        JPanel itemdeprueba2 = new JPanel();
        
        BufferedImage mimagen;
        try {
            mimagen = ImageIO.read(new File("C:\\Users\\JulianCamilo\\Desktop\\uno.png"));
            Image reescalada = mimagen.getScaledInstance(40, 40, 0);
            JLabel otro = new JLabel(new ImageIcon(reescalada));
            otro.setTransferHandler(new TransferHandler("icon"));
             itemdeprueba2.add(otro);
        } catch (IOException ex) {
            Logger.getLogger(FramePruebasMinas.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        
       
        popup.add(itemdeprueba);
        popup.add(itemdeprueba2);
        popup.show(jButton2, 70, 0);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePruebasMinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePruebasMinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePruebasMinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePruebasMinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePruebasMinas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.Fondo fondo2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables

    

    
}
