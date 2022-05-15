/*
 * Kelas tambahan untuk melakukan mengotomasi batch experiment. 
 */
package util;

import controller.ExperimentController;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiang Han
 */
public class ExperimentDirector {
    public static void main(String[] args) {
        testChildPolicy();
    }
    
    private static void testChildPolicy(){
        System.out.println(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyyMMdd_HHmmss_SSS")));
        
        boolean[] isRobustChild = {false, true};
        int[] maxIters = {
            100, 200, 300, 400, 500, 600, 700, 800, 900, 
            1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000,
            10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 
            100000}; //pending
        int numOfGames = 50;
        boolean isSpaceLocal = true;
                
        //MCTS
        for(int i = 0; i<maxIters.length; i++){
            for (int j = 0; j < isRobustChild.length; j++) {
                ExperimentController.mctsAverage(numOfGames, maxIters[i], Math.sqrt(2), isRobustChild[j], isSpaceLocal);
            }
        }
      
        //TDTS
        for(int i = 0; i<maxIters.length; i++){
            for (int j = 0; j < isRobustChild.length; j++) {
                ExperimentController.tdtsAverage(numOfGames, maxIters[i], Math.sqrt(2), 1, 1, isRobustChild[j], isSpaceLocal);
            }
        }
        
        try {
            HttpHelper.pingMe();
        } catch (IOException ex) {
            Logger.getLogger(ExperimentDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void testNormMethods(){
        System.out.println(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyyMMdd_HHmmss_SSS")));
        
        boolean[] isSpaceLocal = {false, true};
        int[] maxIters = {1000, 10000, 100000};
        
        //MCTS
        for(int i = 0; i<maxIters.length; i++){
            for (int j = 0; j < isSpaceLocal.length; j++) {
                ExperimentController.mctsAverage(100, maxIters[i], Math.sqrt(2), true, isSpaceLocal[j]);
            }
        }
      
        //TDTS
        for(int i = 0; i<maxIters.length; i++){
            for (int j = 0; j < isSpaceLocal.length; j++) {
                ExperimentController.tdtsAverage(100, maxIters[i], Math.sqrt(2), 1, 1, true, isSpaceLocal[j]);
            }
        }
      
        try {
            HttpHelper.pingMe();
        } catch (IOException ex) {
            Logger.getLogger(ExperimentDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
