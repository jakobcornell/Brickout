package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Brick implements Serializable {
  private static final long serialVersionUID = 1L;
  public double x, y; // location
  public boolean broken;
  
  public Brick(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public boolean isBreakable() {
    return false;
  }
  
  /*
   * Called when this brick is hit (e.g. by a ball or a bullet)
   * Powerup functionality not yet implemented
   */
  public Powerup hit(boolean strong) {
    broken = true;
    return null;
  }
  
  /*
   * Paints itself onto the passed-in Graphics2D object
   */
  public void paint(Graphics2D g) {
    g.draw(new Rectangle2D.Double(x, y, 4, 2));
  }
}
