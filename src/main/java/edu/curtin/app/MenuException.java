package edu.curtin.app;

public class MenuException extends Exception {
    public MenuException(String msg){
        super(msg);
    }

    public MenuException(String msg, Throwable cause){
        super(msg, cause);
    }
}
