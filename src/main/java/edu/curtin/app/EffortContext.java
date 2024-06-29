package edu.curtin.app;

public class EffortContext {
    private EffortCalc effortCalc;


    public EffortContext(EffortCalc effortCalc){
        this.effortCalc = effortCalc;
    }

    public int doEffortCalc(Wbs task){
        return effortCalc.calcEffort(task);
    }
}
