package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Brick implements Serializable {
  private static final long serialVersionUID = 1L;
  public double x, y; // location
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
  
  /*
   * Called when this brick is hit (e.g. by a ball or a bullet)
   * Powerup functionality not yet implemented
   */
  public Powerup hit(final boolean strong) {
    broken = true;
    return null;
  }
  
  /*
   * Paints itself onto the passed-in Graphics2D object
   */
  public void paint(final Graphics2D g) {
    final AffineTransform t = AffineTransform.getTranslateInstance(x, y);
    t.scale(4.0/image.getWidth(), 2.0/image.getHeight());
    g.drawImage(image, t, null);
  }
}
