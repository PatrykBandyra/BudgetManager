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

public class App extends Application {

    public static Stage stage;
    public static Boolean isSplashLoaded = false;

//    public static ObservableList<DataRow> expenses = FXCollections.observableArrayList();
//    public static ObservableList<DataRow> incomes = FXCollections.observableArrayList();
    public static ObservableList<DataRow> expenses = getDataRowsExpenses(); // TEST
    public static ObservableList<DataRow> incomes = getDataRowsIncomes();   // TEST
    public static SimpleDoubleProperty balance = new SimpleDoubleProperty();

    /**
     * Produce some data to test something [TEST]
     */
    public static ObservableList<DataRow> getDataRowsExpenses() {
        ObservableList<DataRow> rows = FXCollections.observableArrayList();
        rows.addAll(new DataRow(100, 12.65, 1, "pc", 2020, 12, 25, "Jam", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes", "Food"),
                new DataRow(200, 234.99, 34.56, "kg", 2020, 12, 24, "Potatoes and cabbage dish hohohoho", "Food"),
                new DataRow(300, 1.99, 1, "pc", 2020, 12, 24, "Gum", "Food"));
        return rows;
    }

    /**
     * Produce some data to test something [TEST]
     */
    public static ObservableList<DataRow> getDataRowsIncomes() {
        ObservableList<DataRow> rows = FXCollections.observableArrayList();
        rows.addAll(new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(200, 200, 1, "pc", 2020, 12, 24, "Bike sold", "Internet"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(100, 20000, 1, "pc", 2020, 12, 25, "Salary", "Cyclic"),
                new DataRow(300, 1.99, 1, "pc", 2020, 12, 24, "Gum sold", "Food"));
        return rows;
    }

    @Override
    public void init() throws Exception {

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

    public static void main(String[] args) {
        launch(args);
    }
}
