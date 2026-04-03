package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.WelcomeScreen;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        new WelcomeScreen().show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}