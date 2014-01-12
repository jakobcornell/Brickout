package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

public class Paddle {
  public double x;
  public double xv;
  public int size;
  protected boolean guns;
  public Ball loadedBall;
  
  public Paddle() {
    x = 0;
    xv = 0.5;
    size = 4;
  }
  
  public Ball launch() {
    return new Ball();
  }
  
  public Collection<Bullet> shoot() {
    return null;
  }
  
  public void paint(Graphics2D g) {
    g.draw(new Rectangle2D.Double(x-size/2, 1, size, 1));
  }
}
