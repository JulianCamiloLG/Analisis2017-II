/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import analisis.modelos.Deposito;
import analisis.modelos.Mina;
import java.util.LinkedList;

/**
 *
 * @author JulianCamilo
 */
public class LogicaMinas {
    
    
    private LinkedList<Mina> minas;
            
    public LogicaMinas() {
        this.minas = new LinkedList<>();

    }
    
    
     /**
     *Metodo para crear la mina inicial y vacia
     * @param matrizpaneles
     * @param filas_columnas
     * @param mineral
     * @param maxMineros
     * @param nombreMina
     * @param x
     * @param y
     */
    public void crearMinaIniciale(int[][] matrizpaneles, int filas_columnas, String mineral, int maxMineros,String nombreMina,int x, int y){
        Mina mina = new Mina(matrizpaneles, filas_columnas, maxMineros, null, null, x, y, mineral, 0, 0, nombreMina);
        minas.add(mina);
    }
    
    /**
     *Metodo para cambiar el valor de una casilla en la mina
     * 1= camino 2=deposito
     * @param posicionMina la posicion en la lista que tiene la mina
     * @param columna la columna donde se insertara el cambio
     * @param fila la fila donde se insertara el cambio
     * @param deposito el valor nuevo en la casilla
     * @param cantidadMineral La cantidad de mienral que posee el deposito
     * @return si hubo o no cambio
     */
    public boolean modificarmina(int posicionMina, int columna, int fila, int deposito, int cantidadMineral){
        Mina modificar = this.minas.get(posicionMina);
        boolean result=false;
        int[][] matriz = modificar.getMatrizdepaneles();
        if(deposito!=0){
            matriz[fila][columna]= deposito;
            this.minas.get(posicionMina).setMatrizdepaneles(matriz);
            crearNuevoDeposito(deposito,posicionMina, cantidadMineral);
            result=true;
        }
        return result;
    }
    
    /**
     *
     * @param deposito
     * @param posicion
     * @param cantidad
     * @return 
     */
    public boolean crearNuevoDeposito(int deposito, int posicion, int cantidad){
        boolean result=false;
        int cantidadMaterialActual=0;
        for (Deposito dep : minas.get(posicion).getDepositos()) {
            cantidadMaterialActual+= dep.getCantidadMineral();
        }
        if((cantidadMaterialActual+cantidad)<=minas.get(posicion).getValorTotal()){//Preguntar si la mina tiene maximo de material
            Deposito nuevo = new Deposito(this.minas.get(posicion).getMetal(), cantidad);
            minas.get(posicion).getDepositos().add(nuevo);
            result = true;
        }
        return result;
    }
    
    /**
     * Metodo para asignarle a una mina sus mineros
     * @param posicionMina la posicion en la lista que tiene la mina
     * @param mineros la lista con los mineros que se desean ingresar a esa mina
     * @return si se lograron ingresar o no
     */
    public boolean ingresarMineros(int posicionMina, LinkedList mineros){
       boolean result=false;
       if(this.minas.get(posicionMina).getMaxmineros()==mineros.size()){
           this.minas.get(posicionMina).setMineros(mineros);
           result = true;
       }
       return result;
    }
    
    
    
}
