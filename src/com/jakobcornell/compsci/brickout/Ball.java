package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class Ball implements Serializable {
  private static final long serialVersionUID = 1L;
  public double x, y; // positions
  public double xv, yv; // velocities
  public boolean strong;
  
  /*
   * Used when an (extra ball) powerup is activated
   */
  public Ball split() {
    return new Ball();
  }
  
  /*
   * Paints itself onto the passed-in Graphics2D object
   */
  public void paint(Graphics2D g) {
    g.draw(new Ellipse2D.Double((x-0.5), (y-0.5), 1, 1));
  }
}
