/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import javax.json.*;
import javax.json.stream.JsonParser;


/**
 *
 * @author JulianCamilo
 */
public class LogicaJSON{
    
    private String route;

    public LogicaJSON() {
       
    }

    public LogicaJSON(String route) {
        this.route = route;
    }
    
    public LinkedHashMap AbrirJSON() throws FileNotFoundException{
        LinkedHashMap<String, LinkedList<String[]>> infoMinasCompleta= new LinkedHashMap<>();
        File archivo = new File(route);
        InputStream lector = new FileInputStream(archivo);
        JsonReader lectorJson = Json.createReader(lector);
        JsonArray arregloCompleto =  lectorJson.readArray();
        extraerInfoMineros(arregloCompleto.getJsonObject(0));
        infoMinasCompleta=extraerMinas(arregloCompleto.getJsonObject(1));
        return infoMinasCompleta;
    }

    private void extraerInfoMineros(JsonObject mineros) {
        System.out.println(mineros);
        JsonObject minerosCompleto=mineros.getJsonObject("infomineros");
        System.out.println(minerosCompleto);
    }

    private LinkedHashMap<String, LinkedList<String[]>> extraerMinas(JsonObject jsonObject) {
        return null;
    }
    
}
