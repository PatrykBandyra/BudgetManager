package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.App;
import sample.DataRow;
import sample.tasks.GetConnectionAndLoadDesiredExpenseData;
import sample.tasks.GetConnectionAndLoadLatestData;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ExpenseDetailsController implements Initializable {

    @FXML
    private TableView<DataRow> expenseTable;
    @FXML
    private TableColumn<DataRow, Double> expenseValue;
    @FXML
    private TableColumn<DataRow, Double> expenseAmount;
    @FXML
    private TableColumn<DataRow, String> expenseUnit;
    @FXML
    private TableColumn<DataRow, String> expenseCategory;
    @FXML
    private TableColumn<DataRow, String> expenseName;
    @FXML
    private TableColumn<DataRow, String> expenseDate;

    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXTextField yearField;
    @FXML
    private JFXTextField monthField;

    public static Stage promptStage;
    public static DataRow selectedRow;

    public static int monthValue = new Timestamp(System.currentTimeMillis()).getMonth() + 1;
    public static int yearValue = new Timestamp(System.currentTimeMillis()).getYear() + 1900;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // initialize table columns
        expenseValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        expenseAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        expenseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        expenseCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        expenseDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        expenseTable.setItems(App.expenses);
    }

    /**
     * Changes scene from expense details to main scene
     */
    @FXML
    private void onReturnButtonClicked(ActionEvent event) {
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
     * Shows new modal window with delete prompt
     */
    @FXML
    private void showDeletePromptExpense(ActionEvent event) {
        try {
            if (selectedRow != null){
                promptStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/deletePromptExpense.fxml"));
                promptStage.setScene(new Scene(root));
                promptStage.initOwner(App.stage);
                promptStage.initModality(Modality.APPLICATION_MODAL);
                promptStage.initStyle(StageStyle.UNDECORATED);
                promptStage.setResizable(false);
                promptStage.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes scene from expense details to update expense
     */
    @FXML
    private void showUpdateExpenseScene(ActionEvent event) {
        try {
            if (selectedRow != null){
                selectedRow = expenseTable.getSelectionModel().getSelectedItem();
                Parent Parent = FXMLLoader.load(getClass().getResource("/sample/resources/updateExpense.fxml"));
                Scene scene = new Scene(Parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
                App.stage.setScene(scene);
                App.stage.show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Perform search for expenses from a specific month
     */
    @FXML
    private void onSearchButtonPressed(ActionEvent event) {
        new Thread(new GetConnectionAndLoadDesiredExpenseData(monthField.getText(), yearField.getText())).start();
    }
}
