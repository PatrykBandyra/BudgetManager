package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import sample.App;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IncomeScreenController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Changes scene from income scene to main scene after pressing return button
     */
    @FXML
    private void changeSceneToMainScene(ActionEvent event) {
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
     * Opens file chooser to choose a file (containing income document) after pressing a button
     */
    @FXML
    private void openFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File selectedFile = fc.showOpenDialog(App.stage);
        if (selectedFile != null){
            System.out.println("The file has been chosen!");
        } else {
            System.out.println("Failed to open the file!");
        }
    }
}
