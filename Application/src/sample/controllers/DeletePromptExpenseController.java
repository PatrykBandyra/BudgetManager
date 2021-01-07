package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.tasks.DeleteRowExpense;

import java.net.URL;
import java.util.ResourceBundle;

public class DeletePromptExpenseController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onNoClicked(ActionEvent event) {
        ExpenseDetailsController.promptStage.close();
    }

    @FXML
    private void onYesClicked(ActionEvent event) {
        new Thread(new DeleteRowExpense(ExpenseDetailsController.selectedRow.getId())).start();
        ExpenseDetailsController.promptStage.close();
    }
}
