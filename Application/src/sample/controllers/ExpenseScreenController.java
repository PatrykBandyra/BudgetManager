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
import javafx.stage.FileChooser;
import sample.App;
import sample.tasks.GetConnectionAndLoadLatestData;
import sample.tasks.GetUserInputExpenseAndInsert;
import sample.tasks.LoadExpenseReceiptAndPerformQuery;
import sample.tasks.PushUpLogging;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class ExpenseScreenController implements Initializable {

    @FXML
    private JFXButton insertButton;
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
    }

    /**
     * Changes scene from expense scene to main scene after pressing return button
     */
    @FXML
    private void changeSceneToMainScene(ActionEvent event) {
        try {
            // load latest data
            new Thread(new GetConnectionAndLoadLatestData(App.summaryChecked)).start();

            // load main scene
            Parent mainParent = FXMLLoader.load(getClass().getResource("/sample/resources/main.fxml"));
            Scene mainScene = new Scene(mainParent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(mainScene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Opens file chooser to choose a file (containing receipt) after pressing a button
     */
    @FXML
    private void openFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File selectedFile = fc.showOpenDialog(App.stage);
        if (selectedFile != null) {
            // load and validate receipt and try to perform a query (in background)
            new Thread(new LoadExpenseReceiptAndPerformQuery(selectedFile.getPath())).start();
        } else {
            // notify about an error
            PushUpLogging.logFileNotFoundException();
        }
    }

    /**
     * Perform input check and database query after clicking insert button
     */
    @FXML
    private void onInsertButtonClicked(ActionEvent event) {
        new Thread(new GetUserInputExpenseAndInsert(nameField, categoryField, valueField, amountField,
                unitBox, dayField, monthField, yearField)).start();
    }
}
