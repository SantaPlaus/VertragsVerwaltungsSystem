/*package org.vertragsverwaltung;

//import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.io.IOException;


public class ExampleWriting
{
    public static void main(String[] args)
    {
        String path = "C:\\DEV\\workspace\\VertragsVerwaltungsSystem\\src\\main\\java\\org\\vertragsverwaltung\\vertraege\\example.json";

        JSONObject json = new JSONObject();
        try {
            json.put("name", "Google");
            json.put("names", List.of("Paul", "Paula"));
            System.out.println(json);
            json.put("employees", 140000);
            System.out.println(json);
            json.put("offices", List.of("Mountain View", "Los Angeles", "New York"));
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            out.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
