package edu.curtin.app;

public class TotalUnknownEffort implements EffortCalc{
    //calls countTasksWithoutEffort and checks if count is less than 0 
    @Override
    public int calcEffort(Wbs task) throws IllegalArgumentException{
        int count = task.countTasksWithoutEffort();
        if(count < 0){
            throw new IllegalArgumentException();
        }
        return count;
    }
}
