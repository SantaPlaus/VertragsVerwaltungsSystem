package org.vertragsverwaltung.Services;

import org.json.simple.JSONObject;

import java.io.File;

public class ServiceDelete {
    public void deleteVertraegeVSNR(JSONObject jsonObject, int vsnr){
        // NICHT file.delete(), sondern nach vsnr suchen

        vsnr = (int)(long) jsonObject.get("vsnr");

        File delFile = new File("C:\\DEV\\workspace\\VertragsVerwaltungsSystemGit\\src\\main\\resources\\vertraege\\" + vsnr + ".json");

        if (delFile.delete()) {
            System.out.println("File deleted successfully");
        }
        else {
            System.out.println("Failed to delete the file");
        }
    }
}
