package sample.customExceptions;

public class InvalidValueException extends Exception {

    public InvalidValueException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidValueException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
