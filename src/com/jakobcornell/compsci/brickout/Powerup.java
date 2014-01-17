package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public abstract class Powerup implements Serializable {
  private static final long serialVersionUID = 1L;
  public double x, y; // locations
  public double xv, yv; // velocities

  public abstract void activate(Field field);

  /*
   * Paints this powerup onto the provided Graphics2D object
   */
  public void paint(Graphics2D g) {
    g.draw(new Rectangle2D.Double(x-2, y-1, 4, 2));
  }
}
