package com.example.socketfx;

import javafx.scene.layout.VBox;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerController {

    /*
    This class is used to instantiate our server which will allow for an opened socket port for clients to use
    By implementing a server socket we are able to use a socket to accept all connections from other sockets
    To enable communication simultaneously we have to create threads for each aspect of our server design
     */

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner scanner;
    private ServerSocket serverSocket = new ServerSocket();

    public ServerController(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
    }

    public void startServer(VBox vBox) {

        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Server is waiting for client connection");
                try {
                    while(!serverSocket.isClosed()) {

                        Socket socket = serverSocket.accept();
                        scanner = new Scanner(System.in);
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        System.out.println("A client has connected");
                        ServerApplication.newClientUpdateGUI("A new client has connected", vBox);

                        // Each time a new client connects via our port we must create a new client handler
                        // We also need a new thread for each client handler so all threads can run together
                        ClientHandler clientHandler = new ClientHandler(socket);
                        Thread thread = new Thread(clientHandler);
                        thread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mainThread.start();
    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfClosed(String message) {
        // Allows user to manually disconnect via the console
        if(message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit")) return true;
        return false;
    }

    public void sendMessage() {

        Thread sender = new Thread(new Runnable() {
            String message;
            @Override
            public void run() {

                try {
                    while(true) {
                        message = scanner.nextLine();
                        if(checkIfClosed(message)) {

                            System.out.println("You have disconnected");
                            scanner.close();
                            bufferedWriter.close();
                            bufferedReader.close();
                            System.exit(0);
                        }

                        bufferedWriter.write(message);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sender.start();
    }

    public void receiveMessages() {

        Thread receiver = new Thread(new Runnable() {
            String message;
            @Override
            public void run() {

                try {
                    message = bufferedReader.readLine();
                    while(message != null) {
                        if(checkIfClosed(message)) {

                            System.out.println("Server is disconnecting you");
                            scanner.close();
                            bufferedWriter.close();
                            bufferedReader.close();
                            System.exit(0);
                        }
                        System.out.println(message);
                        message = bufferedReader.readLine();
                        if(checkIfClosed(message)) {

                            System.out.println("Server is disconnecting you");
                            scanner.close();
                            bufferedWriter.close();
                            bufferedReader.close();
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();
    }

    public static void runServer(int port, VBox vBox) throws IOException {

        // Start our server on port 8080, and build server by opening connections
        ServerSocket serverSocket = new ServerSocket(port);
        ServerController server = new ServerController(serverSocket);

        // Start our threads
        server.startServer(vBox);
        //server.sendMessage();
        //server.receiveMessages();
    }

}
