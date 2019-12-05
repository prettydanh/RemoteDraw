package com.starea;

import com.starea.converter.DrawingObjectsToStringConverter;
import com.starea.datamodel.Infrastructure;

import java.io.*;
import java.net.Socket;
import java.util.zip.Inflater;

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
        String protocol;

        while (true) {
            protocol = Infrastructure.getInstance().getProtocol();
            while (protocol == null) {
                protocol = Infrastructure.getInstance().getProtocol();
                System.out.println(protocol);
            }
            if(protocol.equals("TERMINATE")) {
                break;
            }
            writer.println(protocol);
            if(protocol.equals("INVITE")) {
                writer.println(Infrastructure.getInstance().getData());
            }
            if (protocol.equals("JOIN")) {
                writer.println(Infrastructure.getInstance().getName());
                writer.println(Infrastructure.getInstance().getJoinCode());
            }
            if (protocol.equals("LEAVE")) {
                if (Infrastructure.getInstance().getName().equals("host")) {
                    writer.println(Infrastructure.getInstance().getCode());
                    System.out.println(Infrastructure.getInstance().getCode());
                } else {
                    writer.println(Infrastructure.getInstance().getJoinCode());
                    System.out.println(Infrastructure.getInstance().getJoinCode());
                }
                writer.println(Infrastructure.getInstance().getName());
                System.out.println(Infrastructure.getInstance().getName());
            }
            Infrastructure.getInstance().setProtocol(null);
        }

    }
}
