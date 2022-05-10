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
        testNormMethods();
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
