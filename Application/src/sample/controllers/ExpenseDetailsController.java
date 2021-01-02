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
import sample.App;
import sample.DataRow;

import java.io.IOException;
import java.net.URL;
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
            Parent mainParent = FXMLLoader.load(getClass().getResource("/sample/resources/main.fxml"));
            Scene mainScene = new Scene(mainParent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(mainScene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
