package sample.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sample.App;
import sample.tasks.LoadExpenseReceiptAndPerformQuery;
import sample.tasks.PushUpLogging;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ExpenseScreenController implements Initializable {

    @FXML
    private JFXButton insertButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Changes scene from expense scene to main scene after pressing return button
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
     * Opens file chooser to choose a file (containing receipt) after pressing a button
     */
    @FXML
    private void openFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File selectedFile = fc.showOpenDialog(App.stage);
        if (selectedFile != null){
            // load and validate receipt and try to perform a query (in background)
            new Thread(new LoadExpenseReceiptAndPerformQuery(selectedFile.getPath())).start();
        } else {
            // notify about an error
            PushUpLogging.logFileNotFoundException();
        }
    }

    /**
     * Perform action after pressing insert button [TEST]
     */
    @FXML
    private void onInsertButtonClicked(ActionEvent event) {
        // here we need to check for data base connection and success of our operation
        // if okay:
        Notifications notificationBuilder = Notifications.create()
                .title("Data Inserted Successfully")
                .text("The database has been updated")
                .graphic(new ImageView(new Image("/sample/resources/check_mark.png")))
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Clicked on notification.");
                    }
                });
        notificationBuilder.darkStyle();
        notificationBuilder.show();
    }
}
