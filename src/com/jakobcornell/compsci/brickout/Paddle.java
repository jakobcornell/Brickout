package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
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
    size = 8;
  }

  public Ball launch() {
    if (loadedBall != null) {
      Ball hashtagLoadedBall = loadedBall;
      loadedBall = null;
      hashtagLoadedBall.x = x;
      hashtagLoadedBall.y = 2.5;
      hashtagLoadedBall.xv = 0.4;
      hashtagLoadedBall.yv = 0.4;
      return hashtagLoadedBall;
    }
    return null;
  }

  public Collection<Bullet> shoot() {
    return null;
  }

  public void paint(Graphics2D g) {
    g.draw(new Rectangle2D.Double(x-size/2, 1, size, 1));
    if (loadedBall != null)
      g.draw(new Ellipse2D.Double(x - 0.5, 2, 1, 1));
  }
}
