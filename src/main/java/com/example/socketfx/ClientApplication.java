package com.example.socketfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class ClientApplication extends Application {

    /*
    Our main application for user interaction via a GUI letting users send and receive messages over a network connection
    JavaFX does not allow long-running operations on the FX thread, preventing the GUI to update and freeze
    Because we are also calling threads from our controllers we must circumvent the GUI freezing due to hang on both ends
    Since all changes to nodes must happen on the FX thread, we can use Platform.runLater() as a workaround to this issue
     */

    @Override
    public void start(Stage stage) throws IOException {

        FileInputStream input = new FileInputStream("src/main/resources/com/example/socketfx/iconbright.png");
        stage.setTitle("");
        Image image = new Image(input);
        stage.getIcons().add(image);
        Scene scene = loginPage(stage);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("login.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void heightChanger(VBox vBox, ScrollPane scrollPane) {
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollPane.setVvalue((Double) t1);
                scrollPane.getStyleClass().add("scroll-pane");
            }
        });
    }

    // Add received messages to our list
    public static void addLabel(String message, VBox vBox) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5));

        Text text = new Text();
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5));
        hBox.getChildren().add(textFlow);
        vBox.getChildren().add(hBox);
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

        Label userName = new Label("Username");
        TextField nameField = new TextField();
        TextField IPField = new TextField();
        Label serverIP = new Label("Server IP");
        Button joinServer = new Button("Join");
        Label message = new Label();

        joinServer.setLayoutY(100);
        joinServer.setAlignment(Pos.CENTER);
        joinServer.getStyleClass().add("join");
        userName.getStyleClass().add("text");
        nameField.getStyleClass().add("info");
        serverIP.getStyleClass().add("text");
        IPField.getStyleClass().add("info");

        Tooltip tooltip1 = new Tooltip("This is where you input your username");
        Tooltip tooltip2 = new Tooltip("This is the IP you want to connect to. Use 'localhost' on a network");
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

        Text text = new Text("Network Chat App");
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
                String checkName = nameField.getText().toString();
                String checkIP = IPField.getText().toString();
                if(!checkName.isEmpty() && !checkIP.isEmpty()) {
                    try {
                        appPage(checkName, checkIP, stage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("test");
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

    public void appPage(String checkName, String serverIP, Stage stage) throws IOException {

        Button button = new Button("Send");
        button.setLayoutY(345);
        button.setLayoutX(450);
        button.setTranslateY(160);
        button.setTranslateX(24);
        button.getStyleClass().add("button");

        TextField textField = new TextField();
        textField.setPrefWidth(361);
        textField.setPrefHeight(26);
        textField.setLayoutX(45);
        textField.setLayoutY(505);
        textField.getStyleClass().add("text-field");

        VBox vBox = new VBox();
        vBox.setPrefWidth(412);
        vBox.setPrefHeight(248);
        // vBox.getStyleClass().add("scroll-pane");

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setLayoutX(45);
        scrollPane.setLayoutY(90);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(500);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        Text text = new Text("Logged in as: " + checkName);
        text.setLayoutY(45);
        text.setLayoutX(70);
        text.getStyleClass().add("title");

        Text ipText = new Text("Server: " + serverIP);
        ipText.setLayoutX(70);
        ipText.setLayoutY(75);
        ipText.getStyleClass().add("title");
        // Font labelFont = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25);
        // label.setFont(labelFont);

        AnchorPane anchorPane = new AnchorPane(button, textField, scrollPane, text, ipText);
        anchorPane.setPrefHeight(396);
        anchorPane.setPrefWidth(478);
        // anchorPane.getStyleClass().add("scroll-pane");

        heightChanger(vBox, scrollPane);
        sendMessages(button, textField, vBox);

        Scene scene = new Scene(anchorPane, 600, 600);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("chatapp.css")).toExternalForm());

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setScene(scene);
                try {
                    ClientController.runClient(serverIP, checkName, vBox, textField, button);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Update our vBox here
    public static void addIncomingMessages(String message, VBox vBox) {
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

    // This should achieve two things at once
    // 1. Add our message to our GUI, 2. Send that message to other users so their messages update
    public void sendMessages(Button button, TextField textField, VBox vBox) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String message = textField.getText();
                if(!message.isEmpty()) {

                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5));

                    Text text = new Text(message);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.getStyleClass().add("text-flow");
                    text.getStyleClass().add("text-flow");

                    textFlow.setPadding(new Insets(5));
                    hBox.getChildren().add(textFlow);
                    vBox.getChildren().add(hBox);
                    textField.setText("");

                }
            }
        });
    }

}