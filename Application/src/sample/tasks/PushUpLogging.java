package sample.tasks;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class PushUpLogging {

    /**
     * Creates push-up notification about exception from working thread
     */
    public static void logFileNotFoundException() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here
                Notifications notificationBuilder = Notifications.create()
                        .title("Failed to open the file!")
                        .text("Check to ensure that the filename and path are valid.")
                        .graphic(new ImageView(new Image("/sample/resources/img/error_mark.png")))
                        .hideAfter(Duration.seconds(10))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            }
                        });
                notificationBuilder.darkStyle();
                notificationBuilder.show();
            }
        });
    }

    /**
     * Creates push-up notification about exception from working thread
     * @param t Exception
     */
    public static void logOtherExceptions(final Throwable t, String title) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here
                Notifications notificationBuilder = Notifications.create()
                        .title(title)
                        .text(t.getMessage())
                        .graphic(new ImageView(new Image("/sample/resources/img/error_mark.png")))
                        .hideAfter(Duration.seconds(10))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            }
                        });
                notificationBuilder.darkStyle();
                notificationBuilder.show();
            }
        });
    }


    /**
     * Creates push-up notification about successful data insertion
     */
    public static void logSuccess() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here
                Notifications notificationBuilder = Notifications.create()
                        .title("Data inserted successfully!")
                        .text("The database has been updated")
                        .graphic(new ImageView(new Image("/sample/resources/img/check_mark.png")))
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                            }
                        });
                notificationBuilder.darkStyle();
                notificationBuilder.show();
            }
        });
    }
}
