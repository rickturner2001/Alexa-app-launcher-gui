package com.rickturner.alexplayground;

import javafx.geometry.Insets;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Iterator;

public class Client extends WebSocketClient {

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String filename) {
        System.out.println("Received message: " + filename);
        // Hacky approach, unnecessary iteration of all records in the database. Database is not intended to hold > 20 records.

        Jedis jedis = new Jedis();
        Iterator<String> labels = jedis.smembers("label").iterator();
        Iterator<String> paths = jedis.smembers("path").iterator();

        while (labels.hasNext() && paths.hasNext()) {
            String label = '"' + labels.next().toLowerCase() + '"';
            String path = paths.next();
            if (label.equals(filename.toLowerCase())) {
                System.out.println("Found a match for: " + label);
                String operatingSystem = System.getProperty("os.name");
                Thread commandThread = new Thread(() -> {
                    if (operatingSystem.toLowerCase().contains("windows")) {
                        // Windows
                        try {
                            Runtime.getRuntime().exec("cmd /c start cmd.exe /K " + path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (operatingSystem.toLowerCase().contains("linux")) {
                        // Linux
                        try {
                            Runtime.getRuntime().exec(new String[]{"gnome-terminal", "-e", path});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Other
                        System.out.println("could not match: [" + label + "] [" + filename + "]");
                    }
                });
                commandThread.start();
            }
        }
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }
}