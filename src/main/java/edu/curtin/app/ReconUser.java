package edu.curtin.app;


import java.util.InputMismatchException;
import java.util.Scanner;

public class ReconUser implements Reconciliation {

    /*Method to ask users to decide the estimate amongst themselves */
    @Override
    public int reconciliationMethod(int[] estimates) {
        Scanner sc = new Scanner(System.in);
        int n = estimates.length;
    
        //print out all estimates for users to decide
        System.out.println("Please choose an estimate amongst yourselves:");
        int newEstimate = sc.nextInt();
        boolean exists = false;
        for (int i = 0; i < n; i++) {
            if (estimates[i] == newEstimate) {
                exists = true;
                break;  // break loop if estimate exists
            }
        }
        if (exists) {
            return newEstimate;  
        } else {
            throw new InputMismatchException("The chosen estimate " + newEstimate + "does not match any of the provided options.");
        }
    }
}   
