package com.example.socketfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class ServerApplication extends Application {

    /*
    Login page and app page for server side of application is built here
    Sending data to and from controllers here became quite messy, needs a fair amount of cleaning up
    Platform.runLater() allows us to keep our GUI updating consistently alongside the socket communication
    JavaFX does not allow constructors (no args) hence the separation of logic
     */

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("");

        FileInputStream input = new FileInputStream("src/main/resources/com/example/socketfx/iconbright.png");
        Image image = new Image(input);
        stage.getIcons().add(image);

        Scene scene = loginPage(stage);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("login.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public Scene loginPage(Stage stage) {

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 50, 50, 50));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 20, 20, 30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label userName = new Label("Server");
        TextField nameField = new TextField();
        TextField IPField = new TextField();
        Label serverIP = new Label("Port #");
        Button joinServer = new Button("Start");
        Label message = new Label();

        joinServer.setLayoutY(100);
        joinServer.setAlignment(Pos.CENTER);
        joinServer.getStyleClass().add("join");
        userName.getStyleClass().add("text");
        nameField.getStyleClass().add("info");
        serverIP.getStyleClass().add("text");
        IPField.getStyleClass().add("info");

        Tooltip tooltip1 = new Tooltip("Your server name (purely aesthetic");
        Tooltip tooltip2 = new Tooltip("This is the port you want to use as your server");
        nameField.setTooltip(tooltip1);
        userName.setTooltip(tooltip1);
        serverIP.setTooltip(tooltip2);
        IPField.setTooltip(tooltip2);

        gridPane.add(userName, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(serverIP, 0, 1);
        gridPane.add(IPField, 1, 1);
        gridPane.add(message, 1 ,2);
        gridPane.setAlignment(Pos.CENTER);

        Reflection reflection = new Reflection();
        reflection.setFraction(0.7f);
        gridPane.setEffect(reflection);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        Text text = new Text("Chat App Server");
        text.getStyleClass().add("title");

        // text.setFont(Font.font("Helvetica", FontWeight.SEMI_BOLD, 60));
        // text.setEffect(dropShadow);

        hBox.getChildren().add(text);
        hBox.setAlignment(Pos.CENTER);

        borderPane.setId("bp");
        gridPane.setId("root");
        joinServer.setId("login");
        text.setId("text");

        joinServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        String checkName = nameField.getText().toString();
                        String checkIP = IPField.getText().toString();
                        if(!checkName.isEmpty() && !checkIP.isEmpty()) {
                            try {
                                appPage(stage, checkName, checkIP);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        // System.out.println("test");

                    }
                });
            }
        });

        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);
        borderPane.setRight(joinServer);
        joinServer.setTranslateY(27);
        joinServer.setTranslateX(-25);
        userName.setTranslateX(-15);
        serverIP.setTranslateX(-15);

        return new Scene(borderPane, 600, 250);
    }

    public void appPage(Stage stage, String checkName, String port) throws IOException {

        Button button = new Button("Send");
        button.setLayoutY(335);
        button.setLayoutX(450);
        button.setTranslateY(160);
        button.setTranslateX(24);
        button.getStyleClass().add("button");

        TextField textField = new TextField();
        textField.setPrefWidth(361);
        textField.setPrefHeight(26);
        textField.setLayoutX(45);
        textField.setLayoutY(495);
        textField.getStyleClass().add("text-field");

        VBox vBox = new VBox();
        vBox.setPrefWidth(412);
        vBox.setPrefHeight(248);
        // vBox.getStyleClass().add("scroll-pane");

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setLayoutX(45);
        scrollPane.setLayoutY(110);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(500);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        Text text = new Text("Server: " + checkName);
        text.setLayoutX(70);
        text.setLayoutY(45);
        text.getStyleClass().add("title");

        Text portText = new Text("Port #: " + port);
        portText.setLayoutX(70);
        portText.setLayoutY(85);
        portText.getStyleClass().add("title");

        // Font labelFont = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25);
        // label.setFont(labelFont);

        AnchorPane anchorPane = new AnchorPane(scrollPane, text, portText);
        anchorPane.setPrefHeight(396);
        anchorPane.setPrefWidth(478);
        // anchorPane.getStyleClass().add("scroll-pane");

        Scene scene = new Scene(anchorPane, 600, 600);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("chatapp.css")).toExternalForm());

        // This becomes a running trend throughout the application interfaces to controllers
        // Lets us keep the GUI updated and prevents freezing due to thread hanging from clients and server talking
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                stage.setScene(scene);
                try {
                    ServerController.runServer(Integer.parseInt(port), vBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Whenever we get a new client add to our GUI for server
    public static void newClientUpdateGUI(String message, VBox vBox) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5));

                Text text = new Text(message);
                TextFlow textFlow = new TextFlow(text);
                textFlow.getStyleClass().add("text-flow-other");
                text.getStyleClass().add("text-flow-other");

                textFlow.setPadding(new Insets(5));
                hBox.getChildren().add(textFlow);
                vBox.getChildren().add(hBox);
            }
        });
    }
}
