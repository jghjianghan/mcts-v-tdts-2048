/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        testVPlayoutValuesPatch();
    }
    private static void testVPlayoutValues(){
        System.out.println(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyyMMdd_HHmmss_SSS")));
        
        double[] vInits = {0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 8, 16, 32, 64, 128};
        int[] maxIters = {1000, 10000, 100000};
        
        for(int i = 0; i<maxIters.length; i++){
            for (int j = 0; j < vInits.length; j++) {
                ExperimentController.tdtsAverage(100, maxIters[i], Math.sqrt(2), 1, 1, true, true, vInits[j]);
            }
        }
        try {
            HttpHelper.pingMe();
        } catch (IOException ex) {
            Logger.getLogger(ExperimentDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void testVPlayoutValuesPatch(){
        System.out.println(LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("yyyyMMdd_HHmmss_SSS")));
        
        double[] vInits = {8, 16, 32, 64, 128};
        int[] maxIters = {1000, 10000, 100000};
        
        for(int i = 0; i<maxIters.length; i++){
            for (int j = 0; j < vInits.length; j++) {                
                ExperimentController.tdtsAverage(100, maxIters[i], Math.sqrt(2), 1, 1, true, true, vInits[j]);
            }
        }
        try {
            HttpHelper.pingMe();
        } catch (IOException ex) {
            Logger.getLogger(ExperimentDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
