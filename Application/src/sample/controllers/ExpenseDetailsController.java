package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpenseDetailsController implements Initializable {

    @FXML
    private TableView<Void> expenseTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
