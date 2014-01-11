package com.jakobcornell.compsci.brickout;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Bullet {
  public double x, y;
  public double xv;

  public void paint(Graphics2D g) {
    g.draw(new Line2D.Double(x, y, 0, 1));
  }
}
