package sample.tasks;

import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.scene.control.ChoiceBox;
import sample.App;
import sample.DataRow;
import sample.InputValidation;
import sample.customExceptions.*;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class GetUserInputAndUpdateIncome extends Task<Void> {

    private final int id;

    private final JFXTextField nameField;
    private final JFXTextField categoryField;
    private final JFXTextField valueField;
    private final JFXTextField amountField;
    private final ChoiceBox<String> unitBox;
    private final JFXTextField dayField;
    private final JFXTextField monthField;
    private final JFXTextField yearField;

    public GetUserInputAndUpdateIncome(int id, JFXTextField nameField, JFXTextField categoryField,
                                        JFXTextField valueField, JFXTextField amountField,
                                        ChoiceBox<String> unitBox, JFXTextField dayField,
                                        JFXTextField monthField, JFXTextField yearField) {

        this.id = id;

        this.nameField = nameField;
        this.categoryField = categoryField;
        this.valueField = valueField;
        this.amountField = amountField;
        this.unitBox = unitBox;
        this.dayField = dayField;
        this.monthField = monthField;
        this.yearField = yearField;
    }

    /**
     * Gets connection from database and updates a specific row in income table
     */
    @Override
    protected Void call() {
        try {
            // input validation part

            // check if fields not empty
            if (nameField.getText().trim().isEmpty() || categoryField.getText().trim().isEmpty() ||
                    valueField.getText().trim().isEmpty() || amountField.getText().trim().isEmpty() ||
                    amountField.getText().trim().isEmpty() || unitBox.getValue() == null ||
                    dayField.getText().trim().isEmpty() || monthField.getText().trim().isEmpty() ||
                    yearField.getText().trim().isEmpty()) {
                throw new EmptyFieldException("Empty input fields.");
            }

            String name = InputValidation.doName(nameField.getText());  // throws exception
            String category = InputValidation.doCategory(categoryField.getText());  // throws exception
            double value = InputValidation.validateValue(valueField.getText()); // throws exception
            double amount = InputValidation.validateAmount(amountField.getText());  // throws exception
            String unit = unitBox.getValue();
            if (!InputValidation.ifAmountMatchesUnit(amount, unit)) {
                throw new AmountNotMatchingUnitException("Unit does not match the amount.");
            }
            int[] date = InputValidation.validateDate(dayField.getText(), monthField.getText(), yearField.getText());   // throws exception


            // database part

            if (App.databaseManager.connection == null) {   // connection was never established
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            } else {    // connection was established earlier - close and reconnect
                App.databaseManager.connection.close();
                App.databaseManager.getConnection("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl",
                        "BD1_Z07", "jibcuk");
            }
            // if connection established successfully - perform update operation via stored procedure
            final String call = "{call UPDATE_REV(?,?,?,?,?,?,?,?,?)}";
            CallableStatement statement = App.databaseManager.connection.prepareCall(call);
            statement.setInt(1, id);
            statement.setDouble(2, value);
            statement.setDouble(3, amount);
            statement.setString(4, unit);
            statement.setInt(5, date[2]);
            statement.setInt(6, date[1]);
            statement.setInt(7, date[0]);
            statement.setString(8, name);
            statement.setString(9, category);
            statement.execute();

            // log about success
            PushUpLogging.logSuccess("Data row in incomes table updated successfully", "Database Update Successful!");
        } catch (EmptyFieldException | InvalidValueException | InvalidAmountException | InvalidDateException |
                AmountNotMatchingUnitException exception) {
            PushUpLogging.logOtherExceptions(exception, "Incorrect Input!");
        } catch (SQLException exception) {
            PushUpLogging.logConnectionError();
        }
        return null;
    }
}
