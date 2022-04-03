package controller;

import agent.GamePlayingAgent;
import agent.RandomAgent;
import agent.mcts.MctsAgent;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiang Han
 */
public class Experimentor {
    public static void main(String[] args) {
        GameModel infModel = new GameModel(Integer.MAX_VALUE);
        
        GamePlayingAgent agent = new MctsAgent();
        GameState state = infModel.generateInitialState();
        
        String directoryName = "log";
        String fileName = "mcts-" + System.currentTimeMillis() + ".txt";
        
        Path dirPath = Paths.get(directoryName);
        Path filePath = Paths.get(directoryName, fileName);
        
        File directory = dirPath.toFile();
        if (!directory.exists())
        {
            directory.mkdir();
        }
        
        try {
            Files.write(
                    filePath, 
                    (state + "\n").getBytes(), 
                    StandardOpenOption.CREATE);
        } catch (IOException ex) {
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(100000));
                Files.write(
                        filePath, 
                        ("Action: " + chosenAction + "\n").getBytes(), 
                        StandardOpenOption.APPEND);
                state = infModel.applyAction(state, chosenAction);
                Files.write(
                        filePath, 
                        (state + "\n").getBytes(), 
                        StandardOpenOption.APPEND);
                
                System.out.println("Score: " + state.getScore());
            } while (!state.isTerminal());
        } catch (IOException ex){
            Logger.getLogger(Experimentor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
