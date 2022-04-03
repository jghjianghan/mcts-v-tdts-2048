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
 *
 * @author Jiang Han
 */
public class ExperimentLogger {
    Path filePath;
    
    public static void main(String[] args) {
    }
    
    public ExperimentLogger(String initialMessage) {
        String directoryName = "log";
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fileName = "mcts-" + now.format(formatter) + ".txt";
        
        Path dirPath = Paths.get(directoryName);
        filePath = Paths.get(directoryName, fileName);
        
        File directory = dirPath.toFile();
        if (!directory.exists())
        {
            directory.mkdir();
        }
        
        try {
            Files.write(
                    filePath, 
                    (initialMessage + now.format(DateTimeFormatter.ISO_DATE_TIME) + "\n\n").getBytes(), 
                    StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void log(GameState state){
        try {
            Files.write(
                    filePath, 
                    (state + "\n").getBytes(), 
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void log(GameAction action, GameState state){
        try {
            Files.write(
                    filePath, 
                    ("Action: " + action + "\n" + state + "\n").getBytes(), 
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void log(String str){
        try {
            Files.write(
                    filePath, 
                    (str + "\n").getBytes(), 
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
