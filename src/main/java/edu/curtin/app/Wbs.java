package edu.curtin.app;


import java.util.*;
import java.util.function.Consumer;

public interface Wbs {

    void printWbs(String indent);
    String getId();
    String getPId();
    String getDescription();

    int getEffort();
    void setEffort(int effort);

    List<Wbs> getTasks();
    
    void processSubTasks(Consumer<Wbs> action, Scanner scanner, String indent);

    Wbs findTask(String taskId);
    
    boolean hasSubTasks(); 
    String toFileString(); 
    int countTasksWithoutEffort();
}
