package sample.customExceptions;

public class EmptyFieldException extends Exception {

    public EmptyFieldException(String errorMessage) {
        super(errorMessage);
    }

    public EmptyFieldException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
