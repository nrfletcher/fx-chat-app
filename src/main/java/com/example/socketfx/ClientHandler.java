package com.example.socketfx;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    /*
    The client handler class will allow us to handle multiple clients at once from our client class
    We create a client handler which itself holds a list of client handlers
    Our server class creates a new handler for each new client joining the server
     */

    static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            // Streams are bytes, writers are characters
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            sendMessageOutward(clientUserName + " has entered the chat");
        } catch (IOException e) {
            shutDownClientHandler(socket, bufferedWriter, bufferedReader);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                sendMessageOutward(messageFromClient);
            } catch (IOException e) {
                shutDownClientHandler(socket, bufferedWriter, bufferedReader);
                break;
            }
        }
    }

    public void sendMessageOutward(String message) {
        // For each client handler in our list, check if the user in question is not the user themselves
        // If not, write our message to each user individually and flush our writer to ensure all data is sent
        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if(!clientHandler.clientUserName.equals(clientUserName)) {

                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                shutDownClientHandler(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        // sendMessageOutward("Server: " + clientUserName + " has left");
    }

    public void shutDownClientHandler(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        // Instinctively we will not want our client handler to be a part of our list if it is shutdown, but..
        // .. additionally this is necessary to avoid an IOError when trying to access an inactive client
        removeClientHandler();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}