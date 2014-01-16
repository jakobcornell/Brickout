package com.jakobcornell.compsci.brickout.leveldev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jakobcornell.compsci.brickout.Field;

public class Level extends Field {
  public static Level stupidLevel() {
    final Level stupid = new Level(); // get on my level
    // TODO put stupid level initialization here
    return stupid;
  }
  
  public static Level getFromFile(final File inFile) throws IOException, ClassNotFoundException {
    final ObjectInputStream des = new ObjectInputStream(new FileInputStream(inFile));
    final Level level = (Level) des.readObject();
    des.close();
    return level;
  }
  
  public static void saveToFile(final Field level, final File outFile) throws IOException {
    final ObjectOutputStream ser = new ObjectOutputStream(new FileOutputStream(outFile));
    ser.writeObject(level);
    ser.close();
  }
}
