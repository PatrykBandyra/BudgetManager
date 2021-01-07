package sample.tasks;

import javafx.concurrent.Task;
import sample.App;
import sample.DataRow;
import sample.controllers.ExpenseDetailsController;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteRowExpense extends Task<Void> {

    private final int id;

    public DeleteRowExpense(int id) {
        this.id = id;
    }

    @Override
    protected Void call() {
        try {
            if (App.databaseManager.connection == null) {   // connection was never established
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            } else {    // connection was established earlier - close and reconnect
                App.databaseManager.connection.close();
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            }
            // if connection established successfully - perform delete operation via stored procedure
            final String call = "{call DELETE_EXP(?)}";
            CallableStatement statement = App.databaseManager.connection.prepareCall(call);
            statement.setInt(1, id);
            statement.execute();

            // and reload specific expenses
            final Statement sqlGetDesiredExpenses = App.databaseManager.connection.createStatement();
            ResultSet resultDesiredExpensesSet = sqlGetDesiredExpenses.executeQuery("SELECT e.EXP_ID, e.VALUE, e.AMOUNT, e.UNIT, e.YEAR, e.MONTH, e.DAY, e.NAME, ec.E_CAT_NAME AS CATEGORY FROM EXPENSE e JOIN EXP_CAT ec on e.E_CAT_ID = ec.E_CAT_ID WHERE e.YEAR = "+ExpenseDetailsController.yearValue+" AND e.MONTH = "+ ExpenseDetailsController.monthValue+"");
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
            PushUpLogging.logSuccess("Data row in expenses table deleted successfully", "Database Deletion Successful!");
        } catch (SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
