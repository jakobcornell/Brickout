package com.jakobcornell.compsci.brickout;

import java.util.Collections;
import java.util.HashSet;

/*
 * An example level to be used to demonstrate the functionality of the game
 */
public class ExampleLevel extends Field {
  private static final long serialVersionUID = 1L;
  
  public ExampleLevel() {
    activeBalls = Collections.synchronizedSet(new HashSet<Ball>());
    bricks = Collections.synchronizedSet(new HashSet<Brick>());
    bullets = Collections.synchronizedSet(new HashSet<Bullet>());
    powerups = Collections.synchronizedSet(new HashSet<Powerup>());
    for (int i = 4; i <= 72; i += 4)
      for (int j = 22; j <= 54; j += 2)
        if(i != 8 && i != 68 && j != 26 && j != 28 && j != 50 && j != 48)
          bricks.add(new Brick(i, j));
  }
}
