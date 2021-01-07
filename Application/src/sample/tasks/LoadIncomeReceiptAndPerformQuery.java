package sample.tasks;

import javafx.concurrent.Task;
import sample.App;
import sample.DataRow;
import sample.ReceiptLoader;
import sample.customExceptions.*;

import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoadIncomeReceiptAndPerformQuery extends Task<Void> {

    private final String filepath;

    public LoadIncomeReceiptAndPerformQuery(String filepath) {
        this.filepath = filepath;
    }

    @Override
    protected Void call() {
        try {
            // get data from receipt and validate it
            ArrayList<DataRow> data = ReceiptLoader.loadReceipt(filepath);

            // database part

            if (App.databaseManager.connection == null) {   // connection was never established
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            } else {    // connection was established earlier - close and reconnect
                App.databaseManager.connection.close();
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            }
            // if connection established successfully - perform insert operation via stored procedure in loop
            for (var dataRow : data){
                final String call = "{call INSERT_REV(?,?,?,?,?,?,?,?)}";
                CallableStatement statement = App.databaseManager.connection.prepareCall(call);
                statement.setDouble(1, dataRow.getValue());
                statement.setDouble(2, dataRow.getAmount());
                statement.setString(3, dataRow.getUnit());
                statement.setInt(4, dataRow.getYear());
                statement.setInt(5, dataRow.getMonth());
                statement.setInt(6, dataRow.getDay());
                statement.setString(7, dataRow.getName());
                statement.setString(8, dataRow.getCategory());
                statement.execute();
            }

            // log about success
            PushUpLogging.logSuccess("Data from receipt inserted into income table successfully", "Database Insertion Successful!");

        } catch (FileNotFoundException exception) {
            PushUpLogging.logFileNotFoundException();
        } catch (IncorrectReceiptException | InvalidValueException | InvalidAmountException | InvalidUnitException |
                AmountNotMatchingUnitException | InvalidDateException exception) {
            PushUpLogging.logOtherExceptions(exception, "Incorrect Receipt!");
        }catch(SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
