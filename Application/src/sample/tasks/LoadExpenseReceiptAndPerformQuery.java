package sample.tasks;

import javafx.concurrent.Task;
import sample.DataRow;
import sample.ReceiptLoader;
import sample.customExceptions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LoadExpenseReceiptAndPerformQuery extends Task<Void> {

    private final String filepath;

    public LoadExpenseReceiptAndPerformQuery(String filepath) {
        this.filepath = filepath;
    }

    @Override
    protected Void call() {
        try {
            // first check for database connection + new exception
            ArrayList<DataRow> data = ReceiptLoader.loadReceipt(filepath);
            // database query + ui update + notification about success
            // ...




        } catch (FileNotFoundException exception) {
            PushUpLogging.logFileNotFoundException();
        } catch (IncorrectReceiptException | InvalidValueException | InvalidAmountException | InvalidUnitException |
                AmountNotMatchingUnitException | InvalidDateException exception) {
            PushUpLogging.logOtherExceptions(exception);
        }
        return null;
    }
}
