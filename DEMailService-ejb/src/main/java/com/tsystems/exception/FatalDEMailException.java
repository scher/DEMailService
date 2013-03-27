package com.tsystems.exception;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 20.03.13
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class FatalDEMailException extends Exception {
    public final String message;

    public FatalDEMailException(String message) {
        this.message = message;
    }
}
