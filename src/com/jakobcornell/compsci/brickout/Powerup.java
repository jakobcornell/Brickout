package com.jakobcornell.compsci.brickout;

import java.awt.Graphics;

public abstract class Powerup {
   public double x, y;
   
   public abstract void activate(Field field);
   
   public void paint(Graphics g) {
     g.drawRect((int) (10*x-20), (int) (600-10*y-10), 40, 20);
   }
}
