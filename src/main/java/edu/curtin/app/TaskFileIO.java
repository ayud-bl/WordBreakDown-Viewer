package edu.curtin.app;


import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class TaskFileIO {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    public Task readFile(String filePath) throws IOException, TaskFormatException {
        Task root = new Task("WBS", "WBS", ""); // create root task
        logger.info("initialise temp hashmap");
        Map<String, Task> allTasks = new HashMap<>();
        allTasks.put("WBS", root);  // add root task

        logger.info(() -> "reading file contents");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //checking to see if there is a correct number of semicolons
                String[] parts = line.split(";", -1);
                if (parts.length < 3 || parts.length > 4) {
                    throw new TaskFormatException(" Incorrect number of fields in line: " + line);
                }

                String pId = parts[0].trim();
                if (pId.isEmpty()) {
                    pId = "WBS";  // Default to the root task if no parent ID is specified.
                }
                //reaading / parsing each field 
                String id = parts[1].trim();
                String description = parts[2].trim();
                Task pTask = allTasks.get(pId);

                if (pTask == null) {
                    throw new TaskFormatException(" Parent task with ID " + pId + " not found for " + id);
                }
                if(parts.length == 3){
                    //add new Parent task 
                    logger.info(() -> "Adding new task " + id);
                    Task task = new Task(pId, id, description);
                    pTask.addTask(task);
                    allTasks.put(id, task);
                }
                else if (parts.length == 4) { // It's a SubTask
                    int effort = 0;
                    if (!parts[3].isEmpty()) {
                            //error checking for effort 
                            effort = Integer.parseInt(parts[3].trim());
                            if(effort < 0){
                                throw new TaskFormatException(" Invalid integer format for effort in line: " + parts[3]);
                            }
                    }
                       //add new subtask
                    logger.info(() -> "Adding new subtask " + id);
                    SubTask task = new SubTask(pId, id, description, effort);

                    pTask.addTask(task);
                }
            }
        } 
        return root;
    }
    
public void writeFile(String filePath, Wbs root) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        // start with the roots subtasks rather than the root itself
        for (Wbs subtask : ((Task) root).getTasks()) {
            writeTask(writer, subtask);  // recursively write subtask data
        }
    }
}

private void writeTask(BufferedWriter writer, Wbs task) throws IOException {
    if (!task.getId().equals("WBS")) {  //skip writing if it equals root task we created 
        writer.write(task.toFileString() + "\n");  //writing task 
    }
    if (task.hasSubTasks()) {
        for (Wbs subtask : ((Task) task).getTasks()) {
            writeTask(writer, subtask);  // recursively write subtask data
        }
    }
}

    
}
