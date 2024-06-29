package edu.curtin.app;


public class TaskFormatException extends Exception{
    public TaskFormatException(String msg){
        super(msg);
    }

    public TaskFormatException(String msg, Throwable cause){
        super(msg, cause);
    }
}