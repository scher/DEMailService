package com.tsystems.exception;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 18.03.13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class DEMailException extends Exception {
    public final String message;

    public DEMailException(String message) {
        this.message = message;
    }


}
