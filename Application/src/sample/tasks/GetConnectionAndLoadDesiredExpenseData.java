package sample.tasks;

import javafx.concurrent.Task;
import sample.App;
import sample.DataRow;
import sample.InputValidation;
import sample.controllers.ExpenseDetailsController;
import sample.customExceptions.InvalidDateException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetConnectionAndLoadDesiredExpenseData extends Task<Void> {

    private final String monthString;
    private final String yearString;

    public GetConnectionAndLoadDesiredExpenseData(String monthString, String yearString) {
        this.monthString = monthString;
        this.yearString = yearString;
    }

    /**
     * Gets connection and loads expenses from desired month
     */
    @Override
    protected Void call() {
        try {
            // validate input date
            int[] date = InputValidation.validateDate("0", monthString, yearString);

            // if date is valid - save to static variables
            ExpenseDetailsController.monthValue = date[1];
            ExpenseDetailsController.yearValue = date[2];

            if (App.databaseManager.connection == null) {   // connection was never established
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            } else {    // connection was established earlier - close and reconnect
                App.databaseManager.connection.close();
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            }
            // if connection established successfully - load expenses from desired month
            final Statement sqlGetDesiredExpenses = App.databaseManager.connection.createStatement();
            ResultSet resultDesiredExpensesSet = sqlGetDesiredExpenses.executeQuery("SELECT e.EXP_ID, e.VALUE, e.AMOUNT, e.UNIT, e.YEAR, e.MONTH, e.DAY, e.NAME, ec.E_CAT_NAME AS CATEGORY FROM EXPENSE e JOIN EXP_CAT ec on e.E_CAT_ID = ec.E_CAT_ID WHERE e.YEAR = "+date[2]+" AND e.MONTH = "+date[1]+"");
            if (resultDesiredExpensesSet.next()) {
                App.expenses.clear();
                do {
                    App.expenses.add(new DataRow(resultDesiredExpensesSet.getInt("exp_id"), resultDesiredExpensesSet.getDouble("value"),
                            resultDesiredExpensesSet.getDouble("amount"), resultDesiredExpensesSet.getString("unit"),
                            resultDesiredExpensesSet.getInt("year"), resultDesiredExpensesSet.getInt("month"),
                            resultDesiredExpensesSet.getInt("day"), resultDesiredExpensesSet.getString("name"),
                            resultDesiredExpensesSet.getString("category")));
                } while (resultDesiredExpensesSet.next());
            } else {
                // result set empty - inform user (to not worry)
                PushUpLogging.logInfo("No expense in database at desired month", "Database Info");
            }

            // log about success
            PushUpLogging.logConnectionSuccess();

        } catch (InvalidDateException exception) {
            PushUpLogging.logOtherExceptions(exception, "Incorrect Input!");
        } catch (SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
