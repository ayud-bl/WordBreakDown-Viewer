package edu.curtin.app;


public class ReconHighest implements Reconciliation {

    /*Method to find the highest estimate by iterating through the whole array, checking if 
     * its current index is more than its previous
     */
    @Override
    public int reconciliationMethod(int[] array){
        int n = array.length;
        int estimate = 0;

        for(int i = 0; i < n; i++){
            if(array[i] > estimate){
                estimate = array[i];
            }
        }
        return estimate;
    }
}
