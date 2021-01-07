package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.tasks.GetConnectionAndLoadLatestData;

public class App extends Application {

    public static boolean summaryChecked = false;

    public static Stage stage;
    public static Boolean isSplashLoaded = false;

    public static DatabaseManager databaseManager;

    public static ObservableList<DataRow> expenses = FXCollections.observableArrayList();
    public static ObservableList<DataRow> incomes = FXCollections.observableArrayList();

    public static SimpleDoubleProperty balance = new SimpleDoubleProperty();


    @Override
    public void init() throws Exception {
        // while loading splash screen - get connection
        databaseManager = new DatabaseManager();
        new Thread(new GetConnectionAndLoadLatestData(summaryChecked)).start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/main.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/resources/img/dollar_logo.png")));
        stage.setTitle("HomeBudgetApp");
        stage.setScene(new Scene(root));
        App.stage.setMinHeight(600);
        App.stage.setMinWidth(800);
        stage.show();
    }

    @Override
    public void stop() {
        // close connection on application close
        databaseManager.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
