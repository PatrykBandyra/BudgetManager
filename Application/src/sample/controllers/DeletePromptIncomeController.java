package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.tasks.DeleteRowIncome;

import java.net.URL;
import java.util.ResourceBundle;

public class DeletePromptIncomeController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onNoClicked(ActionEvent event) {
        IncomeDetailsController.promptStage.close();
    }

    @FXML
    private void onYesClicked(ActionEvent event) {
        new Thread(new DeleteRowIncome(IncomeDetailsController.selectedRow.getId())).start();
        IncomeDetailsController.promptStage.close();
    }
}
