package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Ball {
  public double x, y;
  public double xv, yv;
  public boolean strong;
  
  public Ball split() {
    return new Ball();
  }
  
  public void paint(Graphics2D g) {
    g.draw(new Ellipse2D.Double((x-0.5), (y-0.5), 1, 1));
  }
}
