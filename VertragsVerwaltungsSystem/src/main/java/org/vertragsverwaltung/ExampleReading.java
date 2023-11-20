/*package org.vertragsverwaltung;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.Reader;

public class ExampleReading {

    public static void main(String[] args) {

        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\java\\org\\vertragsverwaltung\\vertraege\\post.json";

        JSONParser parser = new JSONParser();
        try{
            Reader reader = new FileReader(path);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            String methode = (String) jsonObject.get("methode");
            System.out.println(methode);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}*/
