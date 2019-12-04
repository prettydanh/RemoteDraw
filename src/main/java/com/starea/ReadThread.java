package com.starea;

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

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = reader.readLine();
                if(response.equals("CODE")) {
                    Infrastructure.getInstance().setCode(reader.readLine());
                    System.out.println(Infrastructure.getInstance().getCode());
                }

                if(response.equals("DATA")) {
                    Infrastructure.getInstance().setData(reader.readLine());
                    System.out.println(Infrastructure.getInstance().getData());
                }

                if(response.equals("NOTIFICATION")) {
                    Infrastructure.getInstance().setNotification(reader.readLine());
                    System.out.println(Infrastructure.getInstance().getNotification());
                }

                if(response.equals("RESULT")) {
                    Infrastructure.getInstance().setResult(reader.readLine());
                    System.out.println(Infrastructure.getInstance().getResult());
                    if(Infrastructure.getInstance().getResult().equals("Failed")) {
                        break;
                    }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
