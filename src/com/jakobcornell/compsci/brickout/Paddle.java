package com.jakobcornell.compsci.brickout;

import java.awt.Graphics;
import java.util.Collection;

public class Paddle {
   public double x;
   public double xv;
   public int size;
   protected boolean guns;
   public Ball loadedBall;
   
   public Ball launch() {
      return new Ball();
   }
   
   public Collection<Ball> shoot() {
      return null;
   }
   
   public void paint(Graphics g) {
      g.drawRect((int) (x-size/2), 10, size, 10);
   }
}
