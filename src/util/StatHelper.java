package util;

import java.util.Arrays;

/**
 * Helper class for statistic calculations
 * 
 * @author Jiang Han
 */
public class StatHelper {
    public static int idOfMinimum(int[] data) {
        int minId = 0;
        
        for(int i = 1; i<data.length; i++){
            if (data[minId] > data[i]){
                minId = i;
            }
        }
        
        return minId;
    }
    
    public static int idOfMaximum(int[] data) {
        int maxId = 0;
        
        for(int i = 1; i<data.length; i++){
            if (data[maxId] < data[i]){
                maxId = i;
            }
        }
        
        return maxId;
    }
    
    public static double average(int[] data){
        return Arrays.stream(data).average().getAsDouble();
    }
}
