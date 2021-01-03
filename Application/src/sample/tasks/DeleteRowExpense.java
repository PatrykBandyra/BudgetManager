package sample.tasks;

import javafx.concurrent.Task;
import sample.DataRow;

public class DeleteRowExpense extends Task<Void> {

    private final DataRow row;

    DeleteRowExpense(DataRow row) {
        this.row = row;
    }

    @Override
    protected Void call() {
        // perform delete query on selected row
        // ...
        // and refresh data

        return null;
    }
}
