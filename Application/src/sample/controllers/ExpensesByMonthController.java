package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sample.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExpensesByMonthController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Changes scene from expenses by month to main
     */
    public void onReturnButtonClicked(ActionEvent event) {
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
