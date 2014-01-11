package com.jakobcornell.compsci.brickout;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Ball {
  public double x, y;
  public double xv, yv;
  public boolean strong;
  
  public Ball() {
    x = 0;
    y = 0;
    xv = 0.5;
    yv = 0.5;
  }
  
  public Ball split() {
    return new Ball();
  }
  
  public void paint(Graphics gr) {
    Graphics2D g = (Graphics2D) gr;
    g.draw(new Ellipse2D.Double((x-0.5), (y-0.5), 1, 1));
  }
}
