package edu.curtin.app;


import java.util.*;
import java.util.function.Consumer;

public class Task implements Wbs {
    
    private String pId;
    private String id;
    private String description;

    private int effort;

    private List<Wbs> tasks;

    public Task(String pId, String id, String description){
        this.pId = pId;
        this.id = id;
        this.description = description;
        this.tasks = new ArrayList<>();
    }
    //standard getters and setters
    @Override
    public List<Wbs> getTasks(){
        return tasks;
    }

    public void addTask(Wbs task){
        tasks.add(task);
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
    public void setEffort(int effort){
        this.effort = effort;
    }


    @Override
    public String getDescription(){
        return description;
    }

    //recursively add effort through task tree structure
    @Override
    public int getEffort() {
        int totalEffort = 0; 
        for (Wbs task : tasks) {
            totalEffort += task.getEffort();  // Add the effort of each subtask
        }
        return totalEffort;
    }
    

    @Override
    public void printWbs(String indent) {
        String effortDisplay = (effort > 0) ? ", effort = " + effort : "";
        // print current task details
        System.out.println(indent + id + ": " + description + effortDisplay);
    
        //indentation for subtasks
        String childIndent = indent + "  ";  
    
        // recursively print tasks
        for (Wbs task : tasks) {
            task.printWbs(childIndent);
        }
    }

    //find task based on id
    @Override
    public Wbs findTask(String id) {
        if(this.id.equals(id)){
            return this;
        }
        //iterate through entire wbs structure
        for (Wbs task : tasks) {
            Wbs found = task.findTask(id);
            if (found != null) {
                return found;
            }
        }
        return null; 
    }
  
    @Override
    //processing subtasks with the use of Consumer which "simulates" for each and interating through structure 
    public void processSubTasks(Consumer<Wbs> action, Scanner scanner, String indent) {
    for (Wbs subtask : tasks) {
        //learnt Consumer through mkyong. (2020, February 14). Java 8 Consumer Examples - Mkyong.com. Mkyong.com. https://mkyong.com/java8/java-8-consumer-examples/
        action.accept(subtask); 
        subtask.processSubTasks(action, scanner, indent + "    ");  // recursively process subtasks
        }
    }

    //checking if tasks is empty or not and returning if false
    @Override
    public boolean hasSubTasks() {
        return !tasks.isEmpty();
    }

    //tostring method for task object 
    @Override
    public String toFileString() {
        String taskData = (pId.equals("WBS") ? "" : pId) + ";" + id + ";" + description;
        if (tasks.isEmpty()) { // Only add effort if no subtasks
            taskData += ";" + effort;
        }
        return taskData;
    }

    //recursively counting tasks without effort through structure
    @Override 
    public int countTasksWithoutEffort() {
        int count = 0;  
      if(hasSubTasks()){
        for(Wbs task : tasks){
            count += task.countTasksWithoutEffort();
        }
      }
    
        return count;  
    }
}
