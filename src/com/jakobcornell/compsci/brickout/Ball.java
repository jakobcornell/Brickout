package com.jakobcornell.compsci.brickout;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Ball implements Serializable {
  private static final long serialVersionUID = 1L;
  public double x, y; // positions
  public double xv, yv; // velocities
  public boolean strong;
  protected static BufferedImage image;
  static {
    try {
      image = ImageIO.read(new File("assets/ball.bmp"));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /*
   * Used when an (extra ball) powerup is activated
   */
  public Ball split() {
    return new Ball();
  }
  
  /*
   * Paints itself onto the passed-in Graphics2D object
   */
  public void paint(final Graphics2D g) {
    g.draw(new Ellipse2D.Double((x-0.5), (y-0.5), 1, 1));
    final AffineTransform t = AffineTransform.getTranslateInstance(x-0.5, y-0.5);
    t.scale(1.0/image.getWidth(), 1.0/image.getHeight());
    g.drawImage(image, t, null);
  }
}
