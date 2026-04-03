package ui;

import model.Contact;
import persistence.FileHandler;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Comparator;

public class ContactManagerApp {
    private FileHandler fileHandler;
    private ObservableList<Contact> contacts;
    private FilteredList<Contact> filteredContacts;
    private TableView<Contact> tableView;
    private Stage primaryStage;

    public void start(Stage stage) {
        this.primaryStage = stage;
        this.fileHandler = new FileHandler("contacts.csv");
        this.contacts = fileHandler.load();
        sortContacts();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("gradient-bg");
        root.setPadding(new Insets(40));

        VBox card = new VBox(25);
        card.getStyleClass().add("main-card");
        card.setPadding(new Insets(40));

        // Header
        Button back = new Button("←"); back.setStyle("-fx-background-color: transparent; -fx-text-fill: #6C5CE7; -fx-font-size: 24px; -fx-cursor: hand;");
        back.setOnAction(e -> new WelcomeScreen().show(stage));
        StackPane iconCircle = new StackPane(new Text("🐱") {{ setStyle("-fx-font-size: 24px;"); }});
        iconCircle.setStyle("-fx-background-color: #E1DFFC; -fx-background-radius: 50; -fx-min-width: 45; -fx-min-height: 45;");
        Text title = new Text("My Contacts"); title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);
        Button add = new Button("+ Add New"); add.getStyleClass().add("btn-primary");
        add.setOnAction(e -> showAddDialog());
        HBox header = new HBox(15, back, iconCircle, title, s, add); header.setAlignment(Pos.CENTER_LEFT);

        // Search bar
        TextField search = new TextField(); search.setPromptText("Search name or email..."); search.getStyleClass().add("search-field");
        filteredContacts = new FilteredList<>(contacts, p -> true);
        search.textProperty().addListener((o, old, v) -> filteredContacts.setPredicate(c -> v == null || v.isEmpty() || c.getFullName().toLowerCase().contains(v.toLowerCase())));

        // Table
        tableView = new TableView<>();
        tableView.getStyleClass().add("contact-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Contact, String> nCol = new TableColumn<>("NAME");
        nCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getFullName()));
        TableColumn<Contact, String> eCol = new TableColumn<>("EMAIL");
        eCol.setCellValueFactory(d -> d.getValue().emailProperty());
        TableColumn<Contact, String> pCol = new TableColumn<>("PHONE");
        pCol.setCellValueFactory(d -> d.getValue().phoneProperty());

        TableColumn<Contact, Void> dCol = new TableColumn<>("");
        dCol.setCellFactory(c -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            { btn.getStyleClass().add("btn-danger"); btn.setOnAction(e -> { contacts.remove(getTableView().getItems().get(getIndex())); fileHandler.save(contacts); }); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null); else { setGraphic(btn); setAlignment(Pos.CENTER); }
            }
        });

        tableView.getColumns().addAll(nCol, eCol, pCol, dCol);
        SortedList<Contact> sorted = new SortedList<>(filteredContacts);
        sorted.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sorted);

        // Clear All Button
        Button clear = new Button("Clear All Contacts"); clear.getStyleClass().add("btn-clear");
        clear.setOnAction(e -> confirmClear());

        card.getChildren().addAll(header, search, tableView, new HBox(clear) {{ setAlignment(Pos.CENTER_RIGHT); }});
        VBox.setVgrow(tableView, Priority.ALWAYS);
        root.setCenter(card);

        Scene scene = new Scene(root, 1000, 750);
        scene.getStylesheets().add(getClass().getResource("/styles/purple-theme.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void sortContacts() { contacts.sort(Comparator.comparing(c -> c.getLastName().toLowerCase())); }

    private void showAddDialog() {
        Dialog<Contact> d = new Dialog<>(); d.initOwner(primaryStage);
        d.getDialogPane().getStylesheets().add(getClass().getResource("/styles/purple-theme.css").toExternalForm());
        ButtonType saveType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        d.getDialogPane().getButtonTypes().addAll(saveType, ButtonType.CANCEL);

        VBox content = new VBox(20); content.setAlignment(Pos.CENTER); content.setPadding(new Insets(40, 60, 40, 60));
        StackPane iconCircle = new StackPane(new Text("🐱") {{ setStyle("-fx-font-size: 32px;"); }});
        iconCircle.setStyle("-fx-background-color: #E1DFFC; -fx-background-radius: 50; -fx-min-width: 70; -fx-min-height: 70;");
        Text hTitle = new Text("Add New Contact"); hTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField f = new TextField(); f.setPromptText("First Name"); f.getStyleClass().add("dialog-field");
        TextField m = new TextField(); m.setPromptText("Middle Name"); m.getStyleClass().add("dialog-field");
        TextField l = new TextField(); l.setPromptText("Last Name"); l.getStyleClass().add("dialog-field");
        TextField e = new TextField(); e.setPromptText("Email"); e.getStyleClass().add("dialog-field");
        TextField p = new TextField(); p.setPromptText("Phone"); p.getStyleClass().add("dialog-field");

        content.getChildren().addAll(iconCircle, hTitle, f, m, l, e, p);
        d.getDialogPane().setContent(content);
        d.setResultConverter(bt -> bt == saveType ? new Contact(f.getText(), m.getText(), l.getText(), e.getText(), p.getText()) : null);
        d.showAndWait().ifPresent(c -> { contacts.add(c); sortContacts(); fileHandler.save(contacts); showToast("Contact Saved! 🐱"); });
    }

    private void confirmClear() {
        Alert a = new Alert(Alert.AlertType.WARNING, "Clear all?", ButtonType.YES, ButtonType.NO);
        a.initOwner(primaryStage);
        a.showAndWait().ifPresent(r -> { if (r == ButtonType.YES) { contacts.clear(); fileHandler.save(contacts); } });
    }

    private void showToast(String msg) {
        Popup p = new Popup(); StackPane r = new StackPane(new Text(msg) {{ getStyleClass().add("toast-text"); }});
        r.getStyleClass().add("toast"); p.getContent().add(r); p.show(primaryStage);
        p.setX(primaryStage.getX() + (primaryStage.getWidth()/2) - 100);
        p.setY(primaryStage.getY() + primaryStage.getHeight() - 100);
        new Timeline(new KeyFrame(Duration.seconds(2), e -> p.hide())).play();
    }
}