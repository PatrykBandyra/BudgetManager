package sample.tasks;

import javafx.concurrent.Task;
import sample.App;
import sample.DataRow;
import sample.InputValidation;
import sample.controllers.IncomeDetailsController;
import sample.customExceptions.InvalidDateException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetConnectionAndLoadDesiredIncomeData extends Task<Void> {

    private final String monthString;
    private final String yearString;

    public GetConnectionAndLoadDesiredIncomeData(String monthString, String yearString) {
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
            IncomeDetailsController.monthValue = date[1];
            IncomeDetailsController.yearValue = date[2];

            if (App.databaseManager.connection == null) {   // connection was never established
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            } else {    // connection was established earlier - close and reconnect
                App.databaseManager.connection.close();
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            }
            // if connection established successfully - load incomes from desired month
            final Statement sqlGetDesiredIncomes = App.databaseManager.connection.createStatement();
            ResultSet resultDesiredIncomesSet = sqlGetDesiredIncomes.executeQuery("SELECT r.REV_ID, r.VALUE, r.AMOUNT, r.UNIT, r.YEAR, r.MONTH, r.DAY, r.NAME, rc.R_CAT_NAME AS CATEGORY FROM REVENUE r JOIN REV_CAT rc on r.R_CAT_ID = rc.R_CAT_ID WHERE r.YEAR = "+date[2]+" AND r.MONTH = "+date[1]+"");
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
        } catch (InvalidDateException exception) {
            PushUpLogging.logOtherExceptions(exception, "Incorrect Input!");
        } catch (SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
