package edu.curtin.app;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static void main(String[] args)  {
        //command line argument
        if(args.length == 1){
            String fileName = args[0];

            //create instance of taskFileIO
            TaskFileIO taskFile = new TaskFileIO();
            try{
            //run menu
            logger.info("Opening menu");
            new App().menu(taskFile, fileName);
            } catch (MenuException e){
                System.err.println("Error in menu " + e.getMessage());
            }
        } else {
            System.err.println("usage: java YourProgramName <fileName>");
            System.exit(0);
            logger.severe("invalid command line");
        }
    }

    public void menu (TaskFileIO taskFile, String filename) throws MenuException {
        int n = 3;
        //create instance of reconcilation method and set default
        ReconContext reconContext = new ReconContext(new ReconUser());
        try{
            logger.info(() -> "Reading wbs file");
            //read file and set roottask
            Wbs rootTask = taskFile.readFile(filename);

            System.out.println("Wbs manager \n ----------------");
            Scanner sc = new Scanner(System.in);

            boolean done = false;
            try{
                //Display menu to user until they quit
                while(!done){
                    rootTask.printWbs("");

                    //creating new effort conntext instance, setting Totaleffortcalc as default
                    EffortContext effortContext = new EffortContext(new TotalEffortCalc());
                    System.out.println("Total Known effort = " + effortContext.doEffortCalc(rootTask));

                    //Swapping context to TotalUnknonwnEffort
                    effortContext = new EffortContext(new TotalUnknownEffort());
                    System.out.println("Unknown tasks = " + effortContext.doEffortCalc(rootTask));
                    
                    System.out.println("\nChoose: "
                        + "\n (e) Estimate effort: " +
                        "\n (c) Configure: " + 
                        "\n (x) Exit ");
                    String opt = sc.nextLine();
                    switch (opt) {
                        case "e":
                            logger.info(() -> "Ask for new estiamte and update wbs file");
                            updateTaskEstimate(rootTask, sc, n, reconContext);
                            taskFile.writeFile(filename, rootTask);
                            break;
                        case "c":
                            logger.info(() -> "configure N estimators and reconcilation method");
                            n = configure(sc);
                            reconContext = new ReconContext(configureRecon(sc));
                            break;
                        case "x":
                            done = true;
                            break;
                
                        default:
                            System.out.println("Unknown option " + opt);
                        }
                    }
                } catch (IllegalArgumentException | InputMismatchException e){
                    logger.log(Level.SEVERE, "User inputted invalid data" , e);
                    throw new MenuException("input error ", e);
                }
        } 
        catch(IOException e)
        {
            logger.log(Level.SEVERE, "Failed to read file" , e);
            System.err.println("Error in reading");
        } 
        catch(TaskFormatException e){
            logger.log(Level.SEVERE, "Failed to format file" , e);
            System.err.println("Error in task format");
        }
       
    }

    private static Reconciliation configureRecon(Scanner scanner){
         System.out.println("Please choose a new reconciliation method: \n" + 
                             "\n 1. Take highest estimate " + 
                             "\n 2. Take median estimate " + 
                             "\n 3. Discuss amongst yourselves and decide");  
        int choice = scanner.nextInt();

        Reconciliation reconciliation = new ReconUser();
        switch (choice) {
            case 1:
            reconciliation = new ReconHighest();
          
                break;
            
            case 2:
            reconciliation = new ReconMedian();
            
                break;

            case 3:
            reconciliation = new ReconUser();

                break;
            default:
            return new ReconUser(); 
        }
        return reconciliation;
    }

    /*simple method that asks for new N estimators */
    private static int configure (Scanner scanner) throws IllegalArgumentException{
        System.out.println("Please enter a new value of N");
        int newN = scanner.nextInt();

        if(newN <= 0){
            throw new IllegalArgumentException();
        }
        return newN;
    }
    /* This method updates the task estimate and recursivly calls request estimates for subtasks */
    private static void updateTaskEstimate(Wbs wbsRoot, Scanner scanner, int n, ReconContext reconMethod) {
            //ask for id 
            System.out.print("Enter task ID: ");
            String taskId = scanner.nextLine();
            Wbs foundTask = wbsRoot.findTask(taskId);
            if (foundTask != null) {
                // Check if the found task has subtasks 
                if (foundTask.hasSubTasks()) {
                    // Process only the subtasks, skip the main task with lambda 
                    foundTask.processSubTasks(subtask -> requestEstimates(subtask, scanner, n, reconMethod), scanner, "");
                } else {
                    foundTask.printWbs("");  // Print the task and its hierarchical structure
                     requestEstimates(foundTask, scanner, n, reconMethod);  // handle estimates
                }
            } else {
                System.out.println("Task ID " + taskId + " not found.");
            }
    }

    /*Method to request estimates for tasks */
    private static void requestEstimates(Wbs task, Scanner scanner, int n, ReconContext reconMethod) throws IllegalArgumentException {
        System.out.println("Please enter " + n + " estimates for the task: " + task.getId());
        int[] estimates = new int[n];
    
        //Assign all estimates into array 
        for (int i = 0; i < estimates.length; i++) {
            System.out.print("Enter estimate " + (i + 1) + ": ");
            int num;
            
                num = Integer.parseInt(scanner.nextLine()); // converts string input to int to deal with residual new line if i was to use .nextInt()
                if (num <= 0) {
                    throw new IllegalArgumentException("Estimate must be greater than zero.");
                }
          
            estimates[i] = num;
        }
    
        int reconEstimate;
        boolean foundDifferent = false; 
    
        // Check if there are at least two different estimates
        for (int i = 0; i < estimates.length; i++) {
            for (int j = i + 1; j < estimates.length; j++) {
                if (estimates[i] != estimates[j]) {
                    foundDifferent = true;
                    break;
                }
            }
            if (foundDifferent) {
                break;
            }
        }
    
        System.out.println("All estimates entered: ");
        for (int estimate : estimates) {
            System.out.println(estimate);
        }
       
        if (foundDifferent) {
            reconEstimate = reconMethod.doReconMethod(estimates);
            task.setEffort(reconEstimate);
            System.out.println("The new effort for task ID " + task.getId() + " is: " + task.getEffort());
        } else {
            //set effort to just any index in the array since they are all the same
            task.setEffort(estimates[0]);
            System.out.println("All estimates are the same. No need to apply reconstruction method.");
        }
    
        // If the task is a composite, recursively request estimates for each subtask
        task.processSubTasks(subtask -> requestEstimates(subtask, scanner, n, reconMethod), scanner, "    ");
    }
}
