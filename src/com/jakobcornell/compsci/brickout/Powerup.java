package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Powerup {
  public double x, y;
  public double xv, yv;

  public abstract void activate(Field field);

  public void paint(Graphics2D g) {
    g.draw(new Rectangle2D.Double(x-2, y-1, 4, 2));
  }
}
