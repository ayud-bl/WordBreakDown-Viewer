package edu.curtin.app;


import java.util.*;

public class ReconMedian implements Reconciliation{
    
    /* Method to calculate the median effort, Note : i chose to use an int (which would result in numbers rounding down) 
    rather than a double since estimate seemed to take on an int rather than double in the file. I also assumed that to find to median, 
    you should sort the array first or else it would not really make sense to have a median 5 in the estimates [50 5 900]
    */
    @Override
    public int reconciliationMethod(int[] estimates) {
        Arrays.sort(estimates);  // Sort the array first
        int length = estimates.length;
        if (length % 2 == 1) {
          
            return estimates[length / 2];
        } else {
            return ((estimates[(length / 2) -1] + estimates[length / 2])) / 2;  
        }
    }
    
}
