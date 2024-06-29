package edu.curtin.app;


public class ReconContext {
    private Reconciliation reconMethod;


    public ReconContext(Reconciliation reconMethod){
        this.reconMethod = reconMethod;
    }

    public int doReconMethod(int[] estimate){
        return reconMethod.reconciliationMethod(estimate);
    }
}
