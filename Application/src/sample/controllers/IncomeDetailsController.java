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
import sample.tasks.GetConnectionAndLoadDesiredIncomeData;
import sample.tasks.GetConnectionAndLoadLatestData;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class IncomeDetailsController implements Initializable {

    @FXML
    private TableView<DataRow> incomeTable;
    @FXML
    private TableColumn<DataRow, Double> incomeValue;
    @FXML
    private TableColumn<DataRow, Double> incomeAmount;
    @FXML
    private TableColumn<DataRow, String> incomeUnit;
    @FXML
    private TableColumn<DataRow, String> incomeCategory;
    @FXML
    private TableColumn<DataRow, String> incomeName;
    @FXML
    private TableColumn<DataRow, String> incomeDate;

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
        incomeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // initialize table columns
        incomeValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        incomeAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        incomeUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        incomeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        incomeCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        incomeDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        incomeTable.setItems(App.incomes);
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
    private void showDeletePromptIncome(ActionEvent event) {
        try {
            if (selectedRow != null) {
                promptStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/deletePromptIncome.fxml"));
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
     * Changes scene from income details to update income
     */
    @FXML
    private void showUpdateIncomeScene(ActionEvent event) {
        try {
            selectedRow = incomeTable.getSelectionModel().getSelectedItem();
            Parent Parent = FXMLLoader.load(getClass().getResource("/sample/resources/updateIncome.fxml"));
            Scene scene = new Scene(Parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Perform search for incomes from a specific month
     */
    @FXML
    private void onSearchButtonPressed(ActionEvent event) {
        new Thread(new GetConnectionAndLoadDesiredIncomeData(monthField.getText(), yearField.getText())).start();
    }
}
