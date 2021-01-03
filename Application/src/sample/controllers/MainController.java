package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import sample.App;
import sample.DataRow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label balanceLabel;

    @FXML
    private JFXButton incomeButton;
    @FXML
    private TableView<DataRow> incomeTable;
    @FXML
    private JFXButton moreIncomeButton;
    @FXML
    private TableColumn<DataRow, Double> incomeValue;
    @FXML
    private TableColumn<DataRow, String> incomeName;
    @FXML
    private TableColumn<DataRow, String> incomeCategory;
    @FXML
    private TableColumn<DataRow, String> incomeDate;

    @FXML
    private JFXButton expenseButton;
    @FXML
    private TableView<DataRow> expenseTable;
    @FXML
    private JFXButton moreExpenseButton;
    @FXML
    private TableColumn<DataRow, Double> expenseValue;
    @FXML
    private TableColumn<DataRow, String> expenseName;
    @FXML
    private TableColumn<DataRow, String> expenseCategory;
    @FXML
    private TableColumn<DataRow, String> expenseDate;

    @FXML
    private AnchorPane root;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private VBox drawer;

    private ObservableList<DataRow> expensesShort;
    private ObservableList<DataRow> incomesShort;

    public void initialize(URL url, ResourceBundle rb) {
        App.stage.setResizable(true);   // enable resizing in main scene
        if (!App.isSplashLoaded) {
            loadSplashScreen();
        }

        // hamburger animation
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);

        // drawer animation
        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        openNav.setToX(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);

        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.getTranslateX() != 0) {
                openNav.play();
                incomeButton.setDisable(true);
                expenseButton.setDisable(true);
                moreExpenseButton.setDisable(true);
                moreIncomeButton.setDisable(true);
                incomeTable.setDisable(true);
                expenseTable.setDisable(true);
            } else {
                closeNav.setToX(-(drawer.getWidth())-20);
                closeNav.play();
                incomeButton.setDisable(false);
                expenseButton.setDisable(false);
                moreExpenseButton.setDisable(false);
                moreIncomeButton.setDisable(false);
                incomeTable.setDisable(false);
                expenseTable.setDisable(false);
            }
        });
        // initialize balance value
        balanceLabel.textProperty().bind(App.balance.asString());

        // initialize main scene table views
        incomeValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        incomeName.setCellValueFactory(new PropertyValueFactory<>("name"));
        incomeCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        incomeDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        expenseValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        expenseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        expenseCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        expenseDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // get data for tables from observable lists
        expensesShort = FXCollections.observableArrayList();
        incomesShort = FXCollections.observableArrayList();
        initializeExpensesShort();
        initializeIncomesShort();

        expenseTable.setItems(expensesShort);
        incomeTable.setItems(incomesShort);
    }

    private void loadSplashScreen() {
        try {
            App.isSplashLoaded = true;

            AnchorPane pane = FXMLLoader.load(getClass().getResource(("/sample/resources/splashScreen.fxml")));
            root.getChildren().setAll(pane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            fadeOut.setOnFinished((e) -> {
                try {
                    AnchorPane parentContent = FXMLLoader.load(getClass().getResource(("/sample/resources/main.fxml")));
                    AnchorPane.setTopAnchor(parentContent, 0.0);
                    AnchorPane.setBottomAnchor(parentContent, 0.0);
                    AnchorPane.setLeftAnchor(parentContent, 0.0);
                    AnchorPane.setRightAnchor(parentContent, 0.0);
                    root.getChildren().setAll(parentContent);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main scene to expense scene after pressing expense button
     */
    @FXML
    private void changeSceneToExpenseScene(ActionEvent event) {
        try {
            Parent expenseParent = FXMLLoader.load(getClass().getResource("/sample/resources/expenseScreen.fxml"));
            Scene expenseScene = new Scene(expenseParent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(expenseScene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main scene to income scene after pressing income button
     */
    @FXML
    private void changeSceneToIncomeScene(ActionEvent event) {
        try {
            Parent incomeParent = FXMLLoader.load(getClass().getResource("/sample/resources/incomeScreen.fxml"));
            Scene incomeScene = new Scene(incomeParent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(incomeScene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main to expense details scene
     */
    @FXML
    private void seeMoreExpense(ActionEvent event) {
        try {
            Parent expenseDetailsParent = FXMLLoader.load(getClass().getResource("/sample/resources/expenseDetails.fxml"));
            Scene expenseDetailsScene = new Scene(expenseDetailsParent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(expenseDetailsScene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main to income details scene
     */
    @FXML
    private void seeMoreIncome(ActionEvent event) {
        try {
            Parent incomeDetailsParent = FXMLLoader.load(getClass().getResource("/sample/resources/incomeDetails.fxml"));
            Scene incomeDetailsScene = new Scene(incomeDetailsParent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(incomeDetailsScene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes expenses short observable list with latest expenses
     */
    private void initializeExpensesShort() {
        for (int i = 0; i < 8; ++i) {
            try {
                expensesShort.add(App.expenses.get(i));
            } catch (IndexOutOfBoundsException exception) {
                break;
            }
        }
    }

    /**
     * Initializes incomes short observable list with latest incomes
     */
    private void initializeIncomesShort() {
        for (int i = 0; i < 8; ++i) {
            try{
                incomesShort.add(App.incomes.get(i));
            } catch (IndexOutOfBoundsException exception) {
                break;
            }
        }
    }

    /**
     * Changes scene from main to balance by month
     */
    @FXML
    private void showBalanceByMonth(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/resources/balanceByMonth.fxml"));
            Scene scene = new Scene(parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main to incomes by month
     */
    @FXML
    public void showIncomesByMonth(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/resources/incomesByMonth.fxml"));
            Scene scene = new Scene(parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main to expenses by month
     */
    @FXML
    private void showExpensesByMonth(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/resources/expensesByMonth.fxml"));
            Scene scene = new Scene(parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main to incomes by category
     */
    @FXML
    private void showIncomesByCategory(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/resources/incomesByCategory.fxml"));
            Scene scene = new Scene(parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Changes scene from main to expenses by category
     */
    @FXML
    private void showExpensesByCategory(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/sample/resources/expensesByCategory.fxml"));
            Scene scene = new Scene(parent, App.stage.getScene().getWidth(), App.stage.getScene().getHeight());
            App.stage.setScene(scene);
            App.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
