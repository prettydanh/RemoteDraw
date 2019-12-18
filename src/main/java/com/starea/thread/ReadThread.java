package com.starea.thread;

import com.starea.datamodel.Drawing;
import com.starea.datamodel.Infrastructure;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.IllegalFormatCodePointException;
import java.util.Optional;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;

    public ReadThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            if(!getSocket().isClosed()) {
                try {
                    InputStream input = socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(input));
                    while (true) {
                        String[] response = reader.readLine().split(":", 2);
                        if (response[0].equals("CODE")) {
                            Infrastructure.getInstance().setCode(response[1]);
                        }

                        if (response[0].equals("DATA")) {
                            Infrastructure.getInstance().setData(response[1]);
                        }

                        if (response[0].equals("NOTIFICATION")) {
                            Infrastructure.getInstance().setNotification(response[1]);
                        }

                        if (response[0].equals("MESSAGE")) {
                            Infrastructure.getInstance().setIncomingMessage(response[1]);
                            System.out.println(Infrastructure.getInstance().getIncomingMessage());
                        }

                        if (response[0].equals("CONNECTION_STATE")) {
                            Infrastructure.getInstance().setConnectionState(response[1]);
                        }

                        if(response[0].equals("NEW_UPDATE")) {
                            Infrastructure.getInstance().setData(response[1]);
                        }

                        if (Infrastructure.getInstance().getConnectionState() != null && Infrastructure.getInstance().getConnectionState().equals("Closed")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {

                    }
                }
                try {
                    socket.close();
                    System.out.println("Socket1 closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
