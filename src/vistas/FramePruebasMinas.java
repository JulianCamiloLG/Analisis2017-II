/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Keymap;
import logica.LogicaJSON;
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
        this.esEntrada=false;
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
            String[] datos = ArrayDatosPanel(panel);
            boolean esFactibleEntrada = this.logicaMinas.isEntrada(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), datos[0]);
            if (esFactibleEntrada) {
                JButton botonEntrada = new JButton("convertir en entrada");
                botonEntrada.addActionListener((ActionEvent e) -> {
                    convertirEntrada(panel);
                    popup.setVisible(false);
                });
                popup.add(botonEntrada);
            }

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

        botonMinas = new javax.swing.JButton();
        botonMineros = new javax.swing.JButton();
        fondo2 = new vistas.Fondo();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        JSON = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minas El Álamo");

        botonMinas.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 11)); // NOI18N
        botonMinas.setText("Crear Mina");
        botonMinas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMinasActionPerformed(evt);
            }
        });

        botonMineros.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 11)); // NOI18N
        botonMineros.setText("Contratar mineros");
        botonMineros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMinerosActionPerformed(evt);
            }
        });

        fondo2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N

        javax.swing.GroupLayout fondo2Layout = new javax.swing.GroupLayout(fondo2);
        fondo2.setLayout(fondo2Layout);
        fondo2Layout.setHorizontalGroup(
            fondo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        fondo2Layout.setVerticalGroup(
            fondo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        JSON.setText("Cargar JSON");
        JSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JSONActionPerformed(evt);
            }
        });
        jMenu1.add(JSON);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonMineros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonMinas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(18, 18, 18)
                        .addComponent(botonMinas)
                        .addGap(18, 18, 18)
                        .addComponent(botonMineros)
                        .addGap(0, 134, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMinasActionPerformed
        // TODO add your handling code here:
        String input =JOptionPane.showInputDialog("Digite el tamaño de la mina");
        int tamaño=0;
        if (input ==null) {
            System.out.println("Cancelo");
            
        }
        else{
        tamaño=Integer.parseInt(input);
        String material =JOptionPane.showInputDialog("Digite el material de la mina");
        int mineros =Integer.parseInt(JOptionPane.showInputDialog("Ingrese el maximo de mineros para esta mina"));
        crearMina(tamaño,material, mineros);
        this.repaint();
        }
    }//GEN-LAST:event_botonMinasActionPerformed

    private void botonMinerosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMinerosActionPerformed
        // TODO add your handling code here:
        JLabel mensajeentrada = new JLabel("Para contratar de clic en el boton o enter en el campo de texto");
        mensajeentrada.setHorizontalAlignment(JLabel.CENTER);
        mensajeentrada.setFont(new Font("Yu Gothic UI Semilight", 1, 10));
        
        JPopupMenu popup = new JPopupMenu();
        int minerosMinimos=0;
        int minerosMaximos=100;
        int inicio=25;
        
        JTextField setValor= new JTextField("25");
        setValor.setHorizontalAlignment(JTextField.CENTER);
        setValor.setFont(new Font("Yu Gothic UI Semilight", 1, 14));
        
        
        JSlider seleccionMineros = new JSlider(JSlider.HORIZONTAL, minerosMinimos, minerosMaximos, inicio);
        seleccionMineros.setMajorTickSpacing(25);
        seleccionMineros.setMinorTickSpacing(5);
        seleccionMineros.setPaintLabels(true);
        seleccionMineros.setPaintTicks(true);
        seleccionMineros.setFont(new Font("Yu Gothic UI Semilight", 1, 14));
        seleccionMineros.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider fuente =(JSlider) e.getSource();
                String valor =fuente.getValue()+"";
                setValor.setText(valor);
                
            }
        });
        
        setValor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalMineros=Integer.parseInt(setValor.getText());
                seleccionMineros.setValue(totalMineros);
                System.out.println(totalMineros);
                popup.setVisible(false);
                JOptionPane.showMessageDialog(rootPane, "Usted a contratado: "+totalMineros+" mineros");
                botonMineros.setEnabled(false);
            }
        });
        
        JButton botonSeleccionar= new JButton("Contratar");
        botonSeleccionar.setFont(new Font("Yu Gothic UI Semilight", 1, 14));
        botonSeleccionar.setHorizontalAlignment(JButton.CENTER);
        botonSeleccionar.addActionListener((ActionEvent e) -> {
            int totalMineros=Integer.parseInt(setValor.getText());
            System.out.println(totalMineros);
            popup.setVisible(false);
            JOptionPane.showMessageDialog(rootPane, "Usted a contratado: "+totalMineros+" mineros");
            botonMineros.setEnabled(false);
        });
        
        popup.add(mensajeentrada);
        popup.add(seleccionMineros);
        popup.add(setValor);
        popup.add(botonSeleccionar);
        popup.show(botonMineros, 0,40);
    }//GEN-LAST:event_botonMinerosActionPerformed

    private void JSONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JSONActionPerformed
        // TODO add your handling code here:
        String ruta="";
        JFileChooser escogerRuta= new JFileChooser();
        escogerRuta.setFileSelectionMode(JFileChooser.FILES_ONLY);
        escogerRuta.addChoosableFileFilter(new FileNameExtensionFilter("*.json", "json"));
        int returnVal;
        Object datos[]= new Object[3];
        returnVal = escogerRuta.showOpenDialog(escogerRuta);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            ruta=escogerRuta.getSelectedFile().getAbsolutePath();
            LogicaJSON archivoJSON = new LogicaJSON(ruta);
            try {
                 datos=archivoJSON.AbrirJSON();
                 System.out.println(datos[0]+"\n"+datos[1]+"\n"+datos[2]);
            } catch (FileNotFoundException ex) {
                System.out.println("error json");
            }
        }
    }//GEN-LAST:event_JSONActionPerformed

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
    private javax.swing.JMenuItem JSON;
    private javax.swing.JButton botonMinas;
    private javax.swing.JButton botonMineros;
    private vistas.Fondo fondo2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables

    

    
}
