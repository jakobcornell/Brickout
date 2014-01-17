package com.jakobcornell.compsci.brickout;

import java.util.HashSet;

public class ExampleLevel extends Field {
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
