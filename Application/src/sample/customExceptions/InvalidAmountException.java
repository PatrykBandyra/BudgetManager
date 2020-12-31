package sample.customExceptions;

public class InvalidAmountException extends Exception {

    public InvalidAmountException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidAmountException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
