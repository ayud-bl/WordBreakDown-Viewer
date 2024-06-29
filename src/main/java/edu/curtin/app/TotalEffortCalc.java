package edu.curtin.app;

public class TotalEffortCalc implements EffortCalc{
    //calls getEffort and returns totaleffort if > 0
    @Override
    public int calcEffort(Wbs task) throws IllegalArgumentException{
        int totalEffort = task.getEffort();
        if(totalEffort <= 0){
            throw new IllegalArgumentException();
        }
        return totalEffort;
    }
}
