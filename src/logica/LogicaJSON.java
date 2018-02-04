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
        System.out.println(reader);
        JsonArray objetojson =  reader.readArray();
        System.out.println(objetojson);
        JsonObject objetito = objetojson.getJsonObject(1);
        JsonObject objetito2 =objetojson.getJsonObject(2);
        JsonArray objeto2 = objetito2.getJsonArray("minas");
        JsonObject objeto3= objeto2.getJsonObject(0);
        JsonObject objeto4 = objeto3.getJsonObject("0");
        System.out.println(objetito);
        for (JsonValue string : objeto4.values()) {
            System.out.println(string);
        }
        System.out.println(objeto3.values());
        System.out.println(objeto4.getJsonArray("seccionesMina"));
    }
    
}
