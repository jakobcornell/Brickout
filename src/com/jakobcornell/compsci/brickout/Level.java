package com.jakobcornell.compsci.brickout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Level {
  public static Field getFromFile(final File inFile) throws IOException, ClassNotFoundException {
    final ObjectInputStream des = new ObjectInputStream(new FileInputStream(inFile));
    final Field level = (Field) des.readObject();
    des.close();
    return level;
  }
  
  public static void saveToFile(final Field level, final File outFile) throws IOException {
    final ObjectOutputStream ser = new ObjectOutputStream(new FileOutputStream(outFile));
    ser.writeObject(level);
    ser.close();
  }
}
