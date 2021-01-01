package sample.tasks;

import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.scene.control.ChoiceBox;
import sample.InputValidation;
import sample.customExceptions.*;

public class GetUserInputExpenseAndPerformQuery extends Task<Void> {

    private final JFXTextField nameField;
    private final JFXTextField categoryField;
    private final JFXTextField valueField;
    private final JFXTextField amountField;
    private final ChoiceBox<String> unitBox;
    private final JFXTextField dayField;
    private final JFXTextField monthField;
    private final JFXTextField yearField;

    public GetUserInputExpenseAndPerformQuery(JFXTextField nameField, JFXTextField categoryField,
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

            // here we need to check for data base connection
            // ...

            String name = InputValidation.doName(nameField.getText());
            String category = InputValidation.doCategory(categoryField.getText());
            double value = InputValidation.validateValue(valueField.getText());
            double amount = InputValidation.validateAmount(amountField.getText());
            String unit = unitBox.getValue();
            if (!InputValidation.ifAmountMatchesUnit(amount, unit)) {
                throw new AmountNotMatchingUnitException("Unit does not match the amount.");
            }
            int[] date = InputValidation.validateDate(dayField.getText(), monthField.getText(), yearField.getText());

            // perform query
            // ...
            // success notification

        } catch (EmptyFieldException | InvalidValueException | InvalidAmountException | InvalidDateException |
                AmountNotMatchingUnitException exception) {
            PushUpLogging.logOtherExceptions(exception, "Incorrect input!");
        }
        return null;
    }
}
