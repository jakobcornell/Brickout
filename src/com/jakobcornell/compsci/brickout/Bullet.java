package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

public class Bullet implements Serializable {
  private static final long serialVersionUID = 1L;
  public double x, y; // locations
  public double xv, yv; // velocities
  
  /*
   * Paints itself onto the passed-in Graphics2D object
   */
  public void paint(Graphics2D g) {
    g.draw(new Line2D.Double(x, y, 0, 1));
  }
}
