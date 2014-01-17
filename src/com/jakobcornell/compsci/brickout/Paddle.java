package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

public class Paddle {
  public double x; // position
  public double xv; // velocity
  public int size; // width in game units (relative to the size of a ball)
  protected boolean guns;
  public Ball loadedBall;
  
  public Paddle() {
    x = 0;
    size = 8;
  }
  
  /*
   * Returns a ball to be launched if there is one loaded, or returns null if there isn't
   */
  public Ball launch() {
    if (loadedBall != null) {
      final Ball ballToLaunch = loadedBall;
      loadedBall = null;
      ballToLaunch.x = x;
      ballToLaunch.y = 2.5;
      ballToLaunch.xv = 0;
      ballToLaunch.yv = 0.4;
      return ballToLaunch;
    }
    return null;
  }
  
  /*
   * (Not yet implemented)
   */
  public Collection<Bullet> shoot() {
    return null;
  }
  
  /*
   * Paints this paddle onto the provided Graphics2D object
   */
  public void paint(final Graphics2D g) {
    g.draw(new Rectangle2D.Double(x-size/2, 1, size, 1));
    if (loadedBall != null) // if there is a loaded ball, draw it above this paddle
      Ball.paint(g, x, 2.5);
  }
}
