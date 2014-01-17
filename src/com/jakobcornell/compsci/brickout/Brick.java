package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Brick {
  public double x, y;
  public boolean broken;
  protected static BufferedImage image;
  static {
    try {
      image = ImageIO.read(new File("assets/brick.bmp"));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public Brick(final int x, final int y) {
    this.x = x;
    this.y = y;
  }
  
  public boolean isBreakable() {
    return false;
  }
  
  public Powerup hit(final boolean strong) {
    broken = true;
    return null;
  }
  
  public void paint(final Graphics2D g) {
    final AffineTransform t = new AffineTransform();
    t.translate(x, y);
    g.drawImage(image, t, null);
    g.draw(new Rectangle2D.Double(x, y, 4, 2));
  }
}
