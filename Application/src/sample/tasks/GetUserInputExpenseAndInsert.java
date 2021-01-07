package sample.tasks;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ChoiceBox;
import sample.App;
import sample.InputValidation;
import sample.customExceptions.*;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class GetUserInputExpenseAndInsert extends Task<Void> {

    private final JFXTextField nameField;
    private final JFXTextField categoryField;
    private final JFXTextField valueField;
    private final JFXTextField amountField;
    private final ChoiceBox<String> unitBox;
    private final JFXTextField dayField;
    private final JFXTextField monthField;
    private final JFXTextField yearField;

    public GetUserInputExpenseAndInsert(JFXTextField nameField, JFXTextField categoryField,
                                               JFXTextField valueField, JFXTextField amountField,
                                               ChoiceBox<String> unitBox, JFXTextField dayField,
                                               JFXTextField monthField, JFXTextField yearField) {
        this.nameField = nameField;
        this.categoryField = categoryField;
        this.valueField = valueField;
        this.amountField = amountField;
        this.unitBox = unitBox;
        this.dayField = dayField;
        this.monthField = monthField;
        this.yearField = yearField;
    }

    @Override
    protected Void call() {
        try {
            // check if fields not empty
            if (nameField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty() ||
                    valueField.getText().trim().isEmpty() || amountField.getText().trim().isEmpty() ||
                    amountField.getText().trim().isEmpty() || unitBox.getValue() == null ||
                    dayField.getText().trim().isEmpty() || monthField.getText().trim().isEmpty() ||
                    yearField.getText().trim().isEmpty()) {
                throw new EmptyFieldException("Empty input fields.");
            }

            // validate input
            String name = InputValidation.doName(nameField.getText());  // throws exception
            String category = InputValidation.doCategory(categoryField.getText());  // throws exception
            double value = InputValidation.validateValue(valueField.getText()); // throws exception
            double amount = InputValidation.validateAmount(amountField.getText());  // throws exception
            String unit = unitBox.getValue();
            if (!InputValidation.ifAmountMatchesUnit(amount, unit)) {
                throw new AmountNotMatchingUnitException("Unit does not match the amount.");
            }
            int[] date = InputValidation.validateDate(dayField.getText(), monthField.getText(), yearField.getText());


            // database part

            if (App.databaseManager.connection == null) {   // connection was never established
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            } else {    // connection was established earlier - close and reconnect
                App.databaseManager.connection.close();
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            }
            // if connection established successfully - perform insert operation via stored procedure
            final String call = "{call INSERT_EXP(?,?,?,?,?,?,?,?)}";
            CallableStatement statement = App.databaseManager.connection.prepareCall(call);
            statement.setDouble(1, value);
            statement.setDouble(2, amount);
            statement.setString(3, unit);
            statement.setInt(4, date[2]);
            statement.setInt(5, date[1]);
            statement.setInt(6, date[0]);
            statement.setString(7, name);
            statement.setString(8, category);
            statement.execute();

            // log about success
            PushUpLogging.logSuccess("Data row inserted into expense table successfully", "Database Insertion Successful!");

            // clear text fields
            nameField.clear();
            categoryField.clear();
            valueField.clear();
            amountField.clear();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    unitBox.valueProperty().set(null);
                }
            });
            dayField.clear();
            monthField.clear();
            yearField.clear();
        } catch (EmptyFieldException | InvalidValueException | InvalidAmountException | InvalidDateException |
                AmountNotMatchingUnitException exception) {
            PushUpLogging.logOtherExceptions(exception, "Incorrect Input!");
        } catch (SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
