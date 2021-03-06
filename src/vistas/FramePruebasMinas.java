
package vistas;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private Font fuente;
    private UIManager ui;
    private Image icon;
    private LinkedList<Prueba> paneles;
    
      /**
     * Creates new form FramePruebasMinas
     */
    public FramePruebasMinas() {
        initComponents();
        this.esEntrada=false;
        this.paneles=new LinkedList();
        this.logicaMinas= new LogicaMinas();
        this.cantMinas=0;
        this.xMina=40;
        this.yMina=40;
        this.detalles= new LinkedList<>();
        this.ui= new UIManager();
        this.fuente=new Font("Yu Gothic UI Semilight", 1, 14);
        this.ui.put("OptionPane.messageFont",fuente );
        this.ui.put("OptionPane.buttonFont",fuente );
        seticon();
        
    }

    
    public void crearMina(int tamaño,int tamaño1, String material, int minerosMaximos, int capacidadDeposito) {
        this.esEntrada=false;
        String numeroPanel;
        int mina[][] = new int[tamaño][tamaño1];
        int xaux = xMina;
        int yaux = yMina;
        int xtotal = xMina;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño1; j++) {
                numeroPanel = "" + i + "-" + j + "-" + "Mina " + cantMinas;
                mina[i][j] = 0;
                crearPanel(xaux, yaux, numeroPanel);
                xaux += 40;
            }
            xtotal += 40;
            xaux = xMina;
            yaux += 40;
        }
        logicaMinas.crearMinaIniciale(mina, tamaño, material, minerosMaximos, "Mina " + cantMinas,capacidadDeposito);
        xMina = xtotal + 40;
        crearDetalleMina(material,xtotal,yaux);
    }
    
    public void crearPanel(int x, int y, String nombre){
        Prueba panel = new Prueba();
        panel.setBounds(x, y, 40, 40);
        panel.setName(nombre);
        panel.setFocusable(true);
        configurarEventosPanel(panel);
        this.paneles.add(panel);
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
        botonDeposito.setFont(fuente);
        JButton botonCamino = new JButton("convertir en camino");
        botonCamino.setFont(fuente);
        JButton botonReset = new JButton("Cancelar");
        botonReset.setFont(fuente);

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
                botonEntrada.setFont(fuente);
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
        
        String[] datos=ArrayDatosPanel(panel);
        boolean result=logicaMinas.crearNuevoDeposito(Integer.parseInt(datos[1]),Integer.parseInt(datos[2]),datos[0]);
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
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        JSON = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
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

        fondo2.setAutoscrolls(true);
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

        jButton1.setText("Crear caminos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 12)); // NOI18N

        JSON.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 12)); // NOI18N
        JSON.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/upload.png"))); // NOI18N
        JSON.setText("Cargar JSON");
        JSON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JSONActionPerformed(evt);
            }
        });
        jMenu1.add(JSON);

        jMenuItem1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 12)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download.png"))); // NOI18N
        jMenuItem1.setText("Guardar JSON");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenu2.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 12)); // NOI18N
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
                    .addComponent(botonMinas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonMineros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(0, 112, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonMinasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMinasActionPerformed
        // TODO add your handling code here:
        Object seleccion[] = new Object[3];
        seleccion[0] = "Oro";
        seleccion[1] = "Plata";
        seleccion[2] = "Cobre";
        String mensaje = "Digite el tamaño de la mina";
        String input = (String) JOptionPane.showInputDialog(null, mensaje, "tamaño de la mina", JOptionPane.PLAIN_MESSAGE, null, null, null);
        int tamaño = 0;
        if (input != null) {
            tamaño = Integer.parseInt(input);
            String material = (String) JOptionPane.showInputDialog(null, "Digite el material de la mina", "materiales", JOptionPane.INFORMATION_MESSAGE, null, seleccion, null);
            if (material != null) {
                String min = JOptionPane.showInputDialog("Ingrese el maximo de mineros para esta mina", 30);
                if(min != null){
                    String dep = JOptionPane.showInputDialog("Ingrese la capacidad maxima de deposito de esta mina", 500);
                    if (dep != null) {
                        int mineros = Integer.parseInt(dep);
                        int deposito = Integer.parseInt(min);
                        crearMina(tamaño,tamaño, material, mineros, deposito);
                        this.repaint();
                    }
                }
            }
        }
    }//GEN-LAST:event_botonMinasActionPerformed

    private void botonMinerosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMinerosActionPerformed
        // TODO add your handling code here:
        
        
        JLabel mensajeentrada = new JLabel("Para contratar de clic en el boton o enter en el campo de texto");
        mensajeentrada.setHorizontalAlignment(JLabel.CENTER);
        mensajeentrada.setFont(fuente);
        
        JPopupMenu popup = new JPopupMenu();
        int minerosMinimos=0;
        int minerosMaximos=100;
        int inicio=25;
        
        JTextField setValor= new JTextField("25");
        setValor.setHorizontalAlignment(JTextField.CENTER);
        setValor.setFont(fuente);
        
        
        JSlider seleccionMineros = new JSlider(JSlider.HORIZONTAL, minerosMinimos, minerosMaximos, inicio);
        seleccionMineros.setMajorTickSpacing(25);
        seleccionMineros.setMinorTickSpacing(5);
        seleccionMineros.setPaintLabels(true);
        seleccionMineros.setPaintTicks(true);
        seleccionMineros.setFont(fuente);
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
        botonSeleccionar.setFont(fuente);
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
                 crearMinasJSON(datos);
                 //System.out.println(datos[0]+"\n"+datos[1]+"\n"+datos[2]);
            } catch (FileNotFoundException ex) {
                System.out.println("error json");
            }
        }
    }//GEN-LAST:event_JSONActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        logicaMinas.minasPath();
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables

    private void seticon() {
        try {
            this.icon=ImageIO.read(new File("src/img/mine.png"));
            setIconImage(icon);
        } catch (IOException ex) {
            System.out.println("no se pudo cargar la imagen");
        }
    }

    private void crearMinasJSON(Object[] datos) {
        //To change body of generated methods, choose Tools | Templates.
        LinkedHashMap<String,String[]> minas=(LinkedHashMap) datos[2];
        int cont=0;
        for (Map.Entry<String, String[]> mina : minas.entrySet()) {
            String key = mina.getKey();
            String[] value= mina.getValue();
            crearMinaJSON(value, key);
        }
    }

    private void crearMinaJSON(String[] value, String key) {
         //To change body of generated methods, choose Tools | Templates.
         String material=value[0];
         int mineros=Integer.parseInt(value[1]);
         int deposito=Integer.parseInt(value[2]);
         int width=Integer.parseInt(value[8]);
         int height=Integer.parseInt(value[9]);
         String entrada=value[10];
         crearMina(height,width, material, mineros, deposito);
         this.repaint();
         String detallesMina=value[11];
         detallarMina(detallesMina, key,deposito, entrada);
    }

    private void detallarMina(String detallesMina,String mina,int deposito, String entrada ) {
         //To change body of generated methods, choose Tools | Templates.
         String datos[]=detallesMina.split("\\{|\\}|\\[|\\]");
         
         int cont=0;
         for (String dato : datos) {
             if(!dato.isEmpty() && !dato.equals(",")){
                 cont++;
             }
        }
         String datosSplit[]= new String[cont];
         cont=0;
         for (String dato : datos) {
             if(!dato.isEmpty() && !dato.equals(",")){
                 datosSplit[cont]=dato;
                 cont++;
             }
        }
         detallarPanel(mina,datosSplit,deposito,entrada);
    }

    private void detallarPanel(String mina, String[] datosSplit,int deposito, String entrada) {
       //To change body of generated methods, choose Tools | Templates.
        for (Prueba panele : paneles) {
            if (panele.getName().contains(mina)) {
                String xEntrada=entrada.substring(5,6);
                String yEntrada=entrada.substring(11,12);
                if (panele.getName().equals(xEntrada+"-"+yEntrada+"-"+mina)) {
                    convertirEntrada(panele);
                }
                for (int i = 1; i < datosSplit.length; i+=2) {
                    String type=datosSplit[i].substring(8, 9);
                    String x=datosSplit[i].substring(15, 16);
                    String y=datosSplit[i].substring(21, 22);
                    if (panele.getName().equals(x+"-"+y+"-"+mina)) {
                        if (type.equals("d")) {
                            convertirCamino(panele);
                        }
                        else{
                            convertirDeposito(panele);
                        }
                    }
                }
                
            }
        }
    }
   
}
