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
import sample.tasks.GetConnectionAndLoadDesiredIncomeData;
import sample.tasks.GetConnectionAndLoadLatestData;
import sample.tasks.GetUserInputAndUpdateIncome;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UpdateIncomeController implements Initializable {

    private int selectedRowId;

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

        // get id of selected row
        selectedRowId = IncomeDetailsController.selectedRow.getId();

        // initialize text field with values from selected row
        nameField.setText(IncomeDetailsController.selectedRow.getName());
        categoryField.setText(IncomeDetailsController.selectedRow.getCategory());
        valueField.setText(String.valueOf(IncomeDetailsController.selectedRow.getValue()));
        amountField.setText(String.valueOf(IncomeDetailsController.selectedRow.getAmount()));
        unitBox.setValue(IncomeDetailsController.selectedRow.getUnit());
        dayField.setText(String.valueOf(IncomeDetailsController.selectedRow.getDay()));
        monthField.setText(String.valueOf(IncomeDetailsController.selectedRow.getMonth()));
        yearField.setText(String.valueOf(IncomeDetailsController.selectedRow.getYear()));
    }

    /**
     * Performs input check and updates income data row
     */
    @FXML
    private void onUpdateButtonClicked(ActionEvent event) {
        new Thread(new GetUserInputAndUpdateIncome(selectedRowId, nameField, categoryField, valueField, amountField,
                unitBox, dayField, monthField, yearField)).start();
    }

    /**
     * Changes scene from update income to income details
     */
    @FXML
    private void changeSceneToIncomeDetails(ActionEvent event) {
        try {
            // load specific data
            new Thread(new GetConnectionAndLoadDesiredIncomeData(String.valueOf(IncomeDetailsController.monthValue), String.valueOf(IncomeDetailsController.yearValue))).start();

            // load income details scene
            Parent Parent = FXMLLoader.load(getClass().getResource("/sample/resources/incomeDetails.fxml"));
            Scene scene = new Scene(Parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
