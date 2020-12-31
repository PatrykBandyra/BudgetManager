package sample.customExceptions;

public class IncorrectReceiptException extends Exception {
    public IncorrectReceiptException(String errorMessage) {
        super(errorMessage);
    }

    public IncorrectReceiptException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
