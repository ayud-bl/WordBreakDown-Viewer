package edu.curtin.app;


import java.util.*;
import java.util.function.Consumer;

public class SubTask implements Wbs {
    
    private String pId;
    private String id;
    private String description;

    private int effort;

    public SubTask(String pId, String id, String description, int effort){
        this.pId = pId;
        this.id = id;
        this.description = description;
        this.effort = effort;
  
    }
   //Standard getters setters
    @Override
    public List<Wbs> getTasks(){
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getPId(){
        return pId;
    }

    @Override
    public String getId(){
        return id;
    }

    @Override
    public int getEffort(){
        return effort;
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public void setEffort(int effort){
        this.effort = effort;
    }

    //print subtask info including effort if its more than 0
    @Override
    public void printWbs(String indent){
        if(this.effort == 0 ){
            System.out.println(indent + id + " " + description);
        } else {
            System.out.println(indent + id + " " + description + " " + effort);
        }
    }

    //return task if Taskid equals id in the file/list 
    @Override
    public Wbs findTask(String taskId){
        return this.id.equals(taskId) ? this : null;
    }

    @Override
    public void processSubTasks(Consumer<Wbs> action, Scanner scanner, String indent) {
    // subtasks do not have children so no implementation
    }
    
    @Override
    public boolean hasSubTasks() {
        return false;
    }

    @Override
    public String toFileString() {
        // format the task data
        String taskData = (pId.equals("WBS") ? "" : pId) + ";" + id + ";" + description;
        // append effort only if it's not zero
        if (effort > 0) {
            taskData += ";" + effort;
        } else {
            // append nothing for effort
            taskData += ";";
        }
        return taskData;
    }

    @Override
    public int countTasksWithoutEffort(){
        int count = 0;
        if(effort <= 0){
            count++;
        }
        return count;
    }
}
