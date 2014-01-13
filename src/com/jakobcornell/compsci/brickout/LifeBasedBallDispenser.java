package com.jakobcornell.compsci.brickout;

public class LifeBasedBallDispenser {
  private int balls;

  public LifeBasedBallDispenser(int initialBalls) {
    balls = initialBalls;
  }

  public Ball remove() {
    if (balls > 0) {
      balls--;
      return new Ball();
    }
    return null;
  }
  
  public int size() {
    return balls;
  }
}
