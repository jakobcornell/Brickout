package com.jakobcornell.compsci.brickout;

import java.util.HashSet;

/*
 * An example level to be used to demonstrate the functionality of the game
 */
public class ExampleLevel extends Field {
  private static final long serialVersionUID = 1L;

  public ExampleLevel() {
    activeBalls = new HashSet<Ball>();
    bricks = new HashSet<Brick>();
    bullets = new HashSet<Bullet>();
    powerups = new HashSet<Powerup>();
    for (int i = 10; i < 70; i += 10)
      for (int j = 30; j < 60; j += 5)
        bricks.add(new Brick(i, j));
  }
}
