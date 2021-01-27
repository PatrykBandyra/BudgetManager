package sample.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sample.App;
import sample.DataRow;

import java.sql.*;


public class GetConnectionAndLoadLatestData extends Task<Void> {

    private final boolean summaryChecked;

    public GetConnectionAndLoadLatestData(boolean summaryChecked) {
        this.summaryChecked = summaryChecked;
    }

    /**
     * Gets connection and loads data from current month
     */
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
            // if connection established successfully - load data from current month
            // quick summary check
            if (!summaryChecked){
                final String call = "{call INSERT_NEW()}";
                CallableStatement statement = App.databaseManager.connection.prepareCall(call);
                statement.execute();
                App.summaryChecked = true;
            }

            // get current date
            Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
            int year = currentTimeStamp.getYear() + 1900;
            int month = currentTimeStamp.getMonth() + 1;

            // load balance
            final Statement sqlGetBalance = App.databaseManager.connection.createStatement();
            ResultSet resultBalanceSet = sqlGetBalance.executeQuery("SELECT balance FROM SUMMARY WHERE YEAR="+year+" AND MONTH="+month+"");
            if (resultBalanceSet.next()) {
                // update ui
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            App.balance.set(resultBalanceSet.getDouble("balance"));
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    }
                });

            } else {
                // result set empty - inform user (to not worry)
                PushUpLogging.logInfo("No summary in database at current month", "Database Info");
            }

            // load incomes
            final Statement sqlGetIncomes = App.databaseManager.connection.createStatement();
            ResultSet resultIncomesSet = sqlGetIncomes.executeQuery("SELECT r.REV_ID, r.VALUE, r.AMOUNT, r.UNIT, r.YEAR, r.MONTH, r.DAY, r.NAME, rc.R_CAT_NAME AS CATEGORY FROM REVENUE r JOIN REV_CAT rc on r.R_CAT_ID = rc.R_CAT_ID WHERE r.YEAR = "+year+" AND r.MONTH = "+month+"");
            if (resultIncomesSet.next()) {
                System.out.println(resultIncomesSet.getString("name"));
                App.incomes.clear();
                do {
                    App.incomes.add(new DataRow(resultIncomesSet.getInt("rev_id"), resultIncomesSet.getDouble("value"),
                            resultIncomesSet.getDouble("amount"), resultIncomesSet.getString("unit"),
                            resultIncomesSet.getInt("year"), resultIncomesSet.getInt("month"),
                            resultIncomesSet.getInt("day"), resultIncomesSet.getString("name"),
                            resultIncomesSet.getString("category")));
                } while (resultIncomesSet.next());
            } else {
                PushUpLogging.logInfo("No income this month", "Database Info");
            }

            // load expenses
            final Statement sqlGetExpenses = App.databaseManager.connection.createStatement();
            ResultSet resultExpensesSet = sqlGetIncomes.executeQuery("SELECT e.EXP_ID, e.VALUE, e.AMOUNT, e.UNIT, e.YEAR, e.MONTH, e.DAY, e.NAME, ec.E_CAT_NAME AS CATEGORY FROM EXPENSE e JOIN EXP_CAT ec on e.E_CAT_ID = ec.E_CAT_ID WHERE e.YEAR = "+year+" AND e.MONTH = "+month+"");

            if (resultExpensesSet.next()) {
                App.expenses.clear();
                do {
                    App.expenses.add(new DataRow(resultIncomesSet.getInt("exp_id"), resultIncomesSet.getDouble("value"),
                            resultIncomesSet.getDouble("amount"), resultIncomesSet.getString("unit"),
                            resultIncomesSet.getInt("year"), resultIncomesSet.getInt("month"),
                            resultIncomesSet.getInt("day"), resultIncomesSet.getString("name"),
                            resultIncomesSet.getString("category")));
                } while (resultIncomesSet.next());
            } else {
                PushUpLogging.logInfo("No expense this month", "Database Info");
            }

            // log about success
            PushUpLogging.logConnectionSuccess();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            PushUpLogging.logConnectionError();
        }
        return null;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                "BD1_Z07", "jibcuk");
        final Statement statement = connection.createStatement();
        final ResultSet set = statement.executeQuery("SELECT e.EXP_ID, e.VALUE, e.AMOUNT, e.UNIT, e.YEAR, e.MONTH, e.DAY, e.NAME, ec.E_CAT_NAME AS CATEGORY FROM EXPENSE e JOIN EXP_CAT ec on e.E_CAT_ID = ec.E_CAT_ID");
        if (set.next()) {
            do{
                System.out.println(set.getString("name"));
            } while (set.next());
        }
    }
}
