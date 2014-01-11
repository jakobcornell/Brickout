package com.jakobcornell.compsci.brickout;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Brick {
  public double x, y;
  public boolean broken;

  public boolean isBreakable() {
    return false;
  }

  public Powerup hit(boolean strong) {
    return null;
  }

  public void paint(Graphics gr) {
    Graphics2D g = (Graphics2D) gr;
    g.draw(new Rectangle2D.Double(x, y, 4, 2));
  }
}
