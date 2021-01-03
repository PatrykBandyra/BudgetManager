package sample.controllers;

import com.jfoenix.controls.JFXButton;
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

import java.io.IOException;
import java.net.URL;
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

    public static Stage promptStage;
    public static DataRow selectedRow;

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
            promptStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/deletePromptIncome.fxml"));
            promptStage.setScene(new Scene(root));
            promptStage.initOwner(App.stage);
            promptStage.initModality(Modality.APPLICATION_MODAL);
            promptStage.initStyle(StageStyle.UNDECORATED);
            promptStage.setResizable(false);
            promptStage.showAndWait();
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
}
