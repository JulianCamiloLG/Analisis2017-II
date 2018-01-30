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
    
    public void AbrirJSON() throws FileNotFoundException{
        File archivo = new File(route);
        InputStream lector = new FileInputStream(archivo);
        JsonReader reader = Json.createReader(lector);
        JsonArray objetojson = (JsonArray) reader.read();
        JsonObject objetito = objetojson.getJsonObject(0);
        System.out.println(objetito.keySet());
    }
    
}
