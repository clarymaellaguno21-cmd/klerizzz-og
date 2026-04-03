package ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toast {
    public enum Type { SUCCESS, ERROR, INFO }

    public static void show(Stage owner, String message, Type type) {
        Stage toast = new Stage();
        toast.initOwner(owner);
        toast.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(message);
        label.getStyleClass().addAll("toast", "toast-" + type.name().toLowerCase());

        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        // FIXED PATH: removed double .css
        scene.getStylesheets().add(Toast.class.getResource("/styles/purple-theme.css").toExternalForm());

        toast.setScene(scene);
        toast.setX(owner.getX() + (owner.getWidth() - 300) / 2);
        toast.setY(owner.getY() + owner.getHeight() - 100);
        toast.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), root);
        fadeIn.setFromValue(0); fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1); fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> toast.close());

        new SequentialTransition(fadeIn, pause, fadeOut).play();
    }
}