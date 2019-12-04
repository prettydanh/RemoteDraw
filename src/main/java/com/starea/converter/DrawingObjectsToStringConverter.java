package com.starea.converter;

import com.starea.datamodel.DrawingObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class DrawingObjectsToStringConverter {

    private static DrawingObjectsToStringConverter instance = new DrawingObjectsToStringConverter();

    public static DrawingObjectsToStringConverter getInstance() {
        return instance;
    }

    public static String toString(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static ArrayList fromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        ArrayList objects = (ArrayList)ois.readObject();
        ois.close();
        return objects;
    }
}
