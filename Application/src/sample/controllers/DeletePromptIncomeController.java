package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
        // start delete task

        IncomeDetailsController.promptStage.close();
    }
}
