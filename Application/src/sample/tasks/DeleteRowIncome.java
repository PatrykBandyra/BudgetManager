package sample.tasks;

import javafx.concurrent.Task;
import sample.App;
import sample.DataRow;
import sample.controllers.IncomeDetailsController;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteRowIncome extends Task<Void> {

    private final int id;

    public DeleteRowIncome(int id) {
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
            // if connection established successfully - perform update operation via stored procedure
            final String call = "{call DELETE_REV(?)}";
            CallableStatement statement = App.databaseManager.connection.prepareCall(call);
            statement.setInt(1, id);
            statement.execute();

            // and reload specific incomes
            final Statement sqlGetDesiredIncomes = App.databaseManager.connection.createStatement();
            ResultSet resultDesiredIncomesSet = sqlGetDesiredIncomes.executeQuery("SELECT r.REV_ID, r.VALUE, r.AMOUNT, r.UNIT, r.YEAR, r.MONTH, r.DAY, r.NAME, rc.R_CAT_NAME AS CATEGORY FROM REVENUE r JOIN REV_CAT rc on r.R_CAT_ID = rc.R_CAT_ID WHERE r.YEAR = "+ IncomeDetailsController.yearValue +" AND r.MONTH = "+IncomeDetailsController.monthValue+"");
            if (resultDesiredIncomesSet.next()) {
                App.incomes.clear();
                do {
                    App.incomes.add(new DataRow(resultDesiredIncomesSet.getInt("rev_id"), resultDesiredIncomesSet.getDouble("value"),
                            resultDesiredIncomesSet.getDouble("amount"), resultDesiredIncomesSet.getString("unit"),
                            resultDesiredIncomesSet.getInt("year"), resultDesiredIncomesSet.getInt("month"),
                            resultDesiredIncomesSet.getInt("day"), resultDesiredIncomesSet.getString("name"),
                            resultDesiredIncomesSet.getString("category")));
                } while (resultDesiredIncomesSet.next());
            } else {
                // result set empty - inform user (to not worry)
                PushUpLogging.logInfo("No income in database at desired month", "Database Info");
            }

            // log about success
            PushUpLogging.logSuccess("Data row in income table deleted successfully", "Database Deletion Successful!");
        } catch (SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
