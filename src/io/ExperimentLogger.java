package io;

import controller.Experimentor;
import game.GameAction;
import game.GameModel.GameState;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class bantuan untuk menuliskan informaasi ke file log.
 *
 * @author Jiang Han
 */
public class ExperimentLogger {

    Path filePath;
    String openingPattern = "#*#*#*#*"; 
    String closingPattern = "*#*#*#*#"; 

    /**
     * Konstruktor ini menginisialisasi nama file, menciptakan file baru, lalu
     * menuliskan pesan pembuka file jika ada.
     *
     * @param codeName Nama dari experiment, akan dipakai sebagai awalan nama file/
     * @param initialMessage Pesan yang akan ditulis di awal file.
     */
    public ExperimentLogger(String codeName, String initialMessage) {
        String directoryName = "log";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fileName = codeName + "-" + now.format(formatter) + ".txt";

        Path dirPath = Paths.get(directoryName);
        filePath = Paths.get(directoryName, fileName);

        File directory = dirPath.toFile();
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        try {
            Files.write(filePath,
                    String.format("%s%n%s%n%s%n", 
                            now.format(DateTimeFormatter.ISO_DATE_TIME),
                            initialMessage,
                            openingPattern
                    ).getBytes(),
                    StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Menuliskan state permaianan dalam bentuk teks ke file log.
     *
     * @param state State yang ingin dicatat
     */
    public void log(GameState state) {
        try {
            Files.write(
                    filePath,
                    (state + "\n").getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Mencatat sebuah aksi dan state yang dihasilkan dari aksi tersebut ke file
     * log.
     *
     * @param action Aksi yang ingin dicatat
     * @param state State yang ingin dicatat
     */
    public void log(GameAction action, GameState state) {
        try {
            Files.write(
                    filePath,
                    ("Action: " + action + "\n" + state + "\n").getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Menulis sembarang data ke file log.
     * 
     * @param str Teks yang ingin dituliskan.
     */
    public void log(String str) {
        try {
            Files.write(
                    filePath,
                    String.format("%s%n", str).getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Prints a closing pattern to the log file
     */
    public void logClosingPattern(){
        log(closingPattern);
    }
}
