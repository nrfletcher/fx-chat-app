package com.example.socketfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    /*
    Our client controller for interfacing wth the client application, particularly sending and updating our GUI data
    sendMessage() became a bit of a spaghetti disaster due to unforeseen issues with static methods and sending data
    We send our textField and vBox to manipulate outside the application itself, and instead deal with it here
     */

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public ClientController(Socket socket, String userName) {
        try {

            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;

        } catch (IOException e) {
            shutDownClient(socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMessage(String userName, TextField textField, Button button, VBox vBox) {
        Thread messageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    bufferedWriter.write(userName);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    Scanner scanner = new Scanner(System.in);
                    while(socket.isConnected()) {

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

                                    if(message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit")) {
                                        try {
                                            bufferedWriter.write(userName + " has exited the server");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            bufferedWriter.newLine();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            bufferedWriter.flush();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("Exiting program");
                                        System.exit(0);
                                    }
                                    System.out.println(userName + ": " + message);
                                    try {
                                        bufferedWriter.write(userName + ": " + message);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        bufferedWriter.newLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        bufferedWriter.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    shutDownClient(socket, bufferedWriter, bufferedReader);
                }
            }
        });
        messageThread.start();

    }

    public void receiveMessages(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while(socket.isConnected()) {
                    try {

                        message = bufferedReader.readLine();
                        ClientApplication.addIncomingMessages(message, vBox);
                        System.out.println(message);

                    } catch (IOException e) {
                        shutDownClient(socket, bufferedWriter, bufferedReader);
                    }
                }
            }
        }).start();
    }

    public void shutDownClient(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runClient(String IP, String userName, VBox vBox, TextField textField, Button button) throws IOException {
        // Using 8080 by default as it is popular port for network communication
        Socket socket = new Socket(IP, 8080);
        ClientController client = new ClientController(socket, userName);
        client.receiveMessages(vBox);
        client.sendMessage(userName, textField, button, vBox);
    }

}
