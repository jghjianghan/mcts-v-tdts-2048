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

    private Path dirPath;
    private Path summaryPath;
    private Path filePath;
    private final String dirName;
    private int fileCounter = 0;

    /**
     * Konstruktor ini menginisialisasi nama file, menciptakan file baru, lalu
     * menuliskan pesan pembuka file jika ada.
     *
     * @param codeName Nama dari experiment, akan dipakai sebagai awalan nama file/
     * @param initialMessage Pesan yang akan ditulis di awal file.
     */
    public ExperimentLogger(String codeName, String... initialMessage) {
        String baseDirectory = "log";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        dirName = codeName + "-" + now.format(formatter);

        Path baseDirPath = Paths.get(baseDirectory);

        //Buat root folder kalau belum ada (./log)
        File directoryFile = baseDirPath.toFile();
        if (!directoryFile.exists()) {
            directoryFile.mkdir();
        }
        
        //Buat folder untuk eksperimen (./log/[codename]-[date]_[time])
        dirPath = Paths.get(baseDirectory, dirName);
        File innerDirectory = dirPath.toFile();
        if (!innerDirectory.exists()) {
            innerDirectory.mkdir();
        }
             
        summaryPath = dirPath.resolve("_SUMMARY.txt");
        try {
            Files.createFile(summaryPath);
        } catch (IOException ex) {
            Logger.getLogger(ExperimentLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //menulis konfigurasi eksperimen
        try {
            Files.write(summaryPath,
                    String.format("%s%n%s%n", 
                            now.format(DateTimeFormatter.ISO_DATE_TIME),
                            String.join(System.lineSeparator(), initialMessage)
                    ).getBytes(),
                    StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        nextFile();
    }

    /**
     * Menuliskan state permaianan dalam bentuk teks ke file log.
     *
     * @param state State yang ingin dicatat
     */
    public void log(GameState state) {
        log(state.toString());
    }

    /**
     * Mencatat sebuah aksi dan state yang dihasilkan dari aksi tersebut ke file
     * log.
     *
     * @param action Aksi yang ingin dicatat
     * @param state State yang ingin dicatat
     */
    public void log(GameAction action, GameState state) {
        log(String.format("Action: %s%n%s", action, state));
    }

    /**
     * Menulis sembarang data ke file log.
     * 
     * @param str Teks yang ingin dituliskan.
     */
    public void log(String str) {
        this.write(filePath, str);
    }
    
    public void logSummary(String str) {
        this.write(summaryPath, str);
    }
    
    private void write(Path path, String content){
        try {
            Files.write(
                    path,
                    String.format("%s%n", content).getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void nextFile(){
        try {
            fileCounter++;
            filePath = dirPath.resolve(dirName + "-" + fileCounter);
            Files.createFile(filePath);
        } catch (IOException ex) {
            Logger.getLogger(ExperimentLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
