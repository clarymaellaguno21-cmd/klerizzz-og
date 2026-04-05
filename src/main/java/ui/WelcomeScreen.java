package ui;

import javafx.animation.FadeTransition;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeScreen {
    public void show(Stage stage) {
        VBox mainRoot = new VBox();
        mainRoot.getStyleClass().add("welcome-bg");

        ScrollPane scrollPane = new ScrollPane(mainRoot);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // HERO SECTION
        VBox hero = new VBox(35);
        hero.setAlignment(Pos.CENTER);
        hero.setPrefHeight(750);
        // Transparent background ensures the gradient is visible
        hero.setStyle("-fx-background-color: transparent;");

        Text miniLabel = new Text("JAVA INTERFACE PROJECT");
        miniLabel.setStyle("-fx-fill: #6C5CE7; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        Text heroTitle = new Text("Modern Contact\nManagement System");
        heroTitle.setStyle("-fx-font-size: 48px; -fx-font-family: 'Segoe UI Semibold'; -fx-fill: #2D3436;");
        heroTitle.setTextAlignment(TextAlignment.CENTER);

        Text description = new Text("An elegant way to organize your professional network. Built with JavaFX and persistent CSV storage.");
        description.setStyle("-fx-font-size: 18px; -fx-fill: #636E72;");
        description.setTextAlignment(TextAlignment.CENTER);
        description.setWrappingWidth(550);

        Button enterBtn = new Button("GET STARTED");
        enterBtn.getStyleClass().add("btn-get-started");
        enterBtn.setStyle("-fx-background-color: linear-gradient(to right, #6C5CE7, #8E7DFF); -fx-background-radius: 30; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 18 50; -fx-font-size: 16px; -fx-cursor: hand;");

        enterBtn.setOnAction(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(400), mainRoot);
            ft.setFromValue(1.0); ft.setToValue(0.0);
            ft.setOnFinished(ev -> new ContactManagerApp().start(stage));
            ft.play();
        });

        hero.getChildren().addAll(miniLabel, heroTitle, description, enterBtn);

        // HOW IT WORKS
        VBox infoSection = new VBox(40);
        infoSection.setAlignment(Pos.CENTER);
        infoSection.setStyle("-fx-background-color: transparent; -fx-border-color: #E1DFFC; -fx-border-width: 1 0 0 0; -fx-padding: 100 50 100 50;");

        HBox steps = new HBox(60);
        steps.setAlignment(Pos.CENTER);
        steps.getChildren().addAll(
                createStep("1", "Download & Install", "Get started in seconds with our simple installation process."),
                createStep("2", "Add Contacts", "Import existing contacts or add new ones with an intuitive form."),
                createStep("3", "Organize & Search", "Quickly find and manage your entire professional network.")
        );

        infoSection.getChildren().addAll(new Text("How it works") {{ setStyle("-fx-font-size: 32px; -fx-font-weight: bold;"); }}, steps);
        mainRoot.getChildren().addAll(hero, infoSection);

        Scene scene = new Scene(scrollPane, 1000, 750);
        scene.getStylesheets().add(getClass().getResource("/styles/purple-theme.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createStep(String num, String title, String desc) {
        VBox box = new VBox(12); box.setAlignment(Pos.CENTER);
        StackPane circle = new StackPane(new Text(num) {{ setStyle("-fx-fill: #6C5CE7; -fx-font-weight: bold; -fx-font-size: 18px;"); }});
        circle.setStyle("-fx-background-color: white; -fx-background-radius: 50; -fx-min-width: 60; -fx-min-height: 60; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        box.getChildren().addAll(circle, new Text(title) {{ setStyle("-fx-font-weight: bold;"); }}, new Text(desc) {{ setStyle("-fx-fill: #636E72;"); setWrappingWidth(180); setTextAlignment(TextAlignment.CENTER); }});
        return box;
    }
}