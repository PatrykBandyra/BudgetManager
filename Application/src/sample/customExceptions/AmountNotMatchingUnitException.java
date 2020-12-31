package sample.customExceptions;

public class AmountNotMatchingUnitException extends Exception {

    public AmountNotMatchingUnitException(String errorMessage) {
        super(errorMessage);
    }

    public AmountNotMatchingUnitException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
