package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    public static Stage stage;
    public static Boolean isSplashLoaded = false;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/main.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/resources/dollar_logo.png")));
        stage.setTitle("HomeBudgetApp");
        stage.setScene(new Scene(root));
        App.stage.setMinHeight(600);
        App.stage.setMinWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
