package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import sample.App;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UpdateExpenseController implements Initializable {

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField categoryField;
    @FXML
    private JFXTextField valueField;
    @FXML
    private JFXTextField amountField;
    @FXML
    private ChoiceBox<String> unitBox;
    @FXML
    private JFXTextField dayField;
    @FXML
    private JFXTextField monthField;
    @FXML
    private JFXTextField yearField;

    @FXML
    private JFXButton updateButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize unit values
        ArrayList<String> units = new ArrayList<>(
                Arrays.asList("pc",
                        "kg",
                        "l"));
        ObservableList<String> obsUnits = FXCollections.observableList(units);
        unitBox.getItems().clear();
        unitBox.setItems(obsUnits);

        // initialize text field with values from selected row
        nameField.setText(ExpenseDetailsController.selectedRow.getName());
        categoryField.setText(ExpenseDetailsController.selectedRow.getCategory());
        valueField.setText(String.valueOf(ExpenseDetailsController.selectedRow.getValue()));
        amountField.setText(String.valueOf(ExpenseDetailsController.selectedRow.getAmount()));
        unitBox.setValue(ExpenseDetailsController.selectedRow.getUnit());
        dayField.setText(String.valueOf(ExpenseDetailsController.selectedRow.getDay()));
        monthField.setText(String.valueOf(ExpenseDetailsController.selectedRow.getMonth()));
        yearField.setText(String.valueOf(ExpenseDetailsController.selectedRow.getYear()));
    }

    @FXML
    private void onUpdateButtonClicked(ActionEvent event) {
    }

    /**
     * Changes scene from update expense to expense details
     */
    @FXML
    private void changeSceneToExpenseDetails(ActionEvent event) {
        try {
            Parent Parent = FXMLLoader.load(getClass().getResource("/sample/resources/expenseDetails.fxml"));
            Scene scene = new Scene(Parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
