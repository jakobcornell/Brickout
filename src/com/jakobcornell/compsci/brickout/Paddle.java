package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

public class Paddle {
  public double x;
  public double xv;
  public int size = 5;
  protected boolean guns;
  public Ball loadedBall;

  public Ball launch() {
    return new Ball();
  }

  public Collection<Ball> shoot() {
    return null;
  }

  public void paint(Graphics2D g) {
    g.draw(new Rectangle2D.Double(x-size/2, 1, size, 1));
  }
}
