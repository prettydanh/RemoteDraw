package com.starea;

import com.starea.converter.DrawingObjectsToStringConverter;
import com.starea.datamodel.Infrastructure;

import java.io.*;
import java.net.Socket;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        writer.println(Infrastructure.getInstance().getProtocol());

        if(Infrastructure.getInstance().getProtocol().equals("INVITE")) {
            writer.println(Infrastructure.getInstance().getData());
        }

        if(Infrastructure.getInstance().getProtocol().equals("JOIN")) {
            writer.println(Infrastructure.getInstance().getName());
            writer.println(Infrastructure.getInstance().getJoinCode());
        }
    }
}
