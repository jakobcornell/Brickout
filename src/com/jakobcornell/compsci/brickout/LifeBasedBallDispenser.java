package com.jakobcornell.compsci.brickout;

public class LifeBasedBallDispenser {
  private int balls; // the number of balls left - analogous to lives left

  public LifeBasedBallDispenser(int initialBalls) {
    balls = initialBalls;
  }

  public Ball remove() {
    if (balls > 0) { // returns a new ball if there are enough remaining
      balls--;
      return new Ball();
    }
    return null;
  }
  
  public int size() {
    return balls;
  }
}
