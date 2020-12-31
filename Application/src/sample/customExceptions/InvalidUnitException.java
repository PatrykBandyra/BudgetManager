package sample.customExceptions;

public class InvalidUnitException extends Exception {

    public InvalidUnitException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidUnitException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
