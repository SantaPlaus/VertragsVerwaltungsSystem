package org.vertragsverwaltung.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class FileRepository {
    public JSONObject getJsonObject(String path) {

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(path);
            return (JSONObject) jsonParser.parse((reader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return null;
        }
    }

    public String getFahrzeugHersteller(JSONObject jsonObject) {
        return jsonObject.get("fahrzeug_hersteller").toString().toLowerCase();
    }

    public boolean writeFile(String path, JSONObject jsonObject) {

        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean deleteFile(JSONObject jsonObject) {


        File delFile = null;
        try {
            int vsnr = (int) (long) jsonObject.get("vsnr");
            delFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\resources\\vertraege\\" + vsnr + ".json");
        } catch (NullPointerException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
        } catch (ClassCastException e) {
            System.out.println("Bitte validiere, ob du alle Werte korrekt eingegeben hast.");
            return false;
        }

        return delFile.delete();
    }
}
