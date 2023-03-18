package ru.sergeysemenov.trialcalculator.exceptions;

public class BadExpressionException extends RuntimeException {
    public BadExpressionException(String message){
        super(message);
    }
}
