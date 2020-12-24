package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import sample.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private JFXButton incomeButton;
    @FXML
    private JFXButton expenseButton;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private VBox drawer;

    public void initialize(URL url, ResourceBundle rb) {
        App.stage.setResizable(true);   // enable resizing in main window
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
            } else {
                closeNav.setToX(-(drawer.getWidth())-20);
                closeNav.play();
                incomeButton.setDisable(false);
                expenseButton.setDisable(false);
            }
        });
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
}
