package com.jakobcornell.compsci.brickout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public class Field implements Serializable {
  private static final long serialVersionUID = 1L;
  public transient int score;
  private transient LifeBasedBallDispenser availableBalls;
  protected Set<Ball> activeBalls;
  protected Set<Brick> bricks;
  protected Set<Bullet> bullets;
  protected Set<Powerup> powerups;
  private transient Paddle paddle;
  
  /*
   * Initializes the field with the objects that shouldn't be serialized
   */
  public void initialize(LifeBasedBallDispenser availableBalls, Paddle paddle) {
    this.availableBalls = availableBalls;
    this.paddle = paddle;
  }
  
  /*
   * Paints the objects in this field onto the passed-in Graphics2D object
   */
  public void paint(Graphics2D g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 80, 60);
    g.setColor(Color.WHITE);
    g.setStroke(new BasicStroke(0.1F));
    for (Ball ball : activeBalls)
      ball.paint(g);
    for (Brick brick : bricks)
      brick.paint(g);
    for (Bullet bullet: bullets)
      bullet.paint(g);
    paddle.paint(g);
  }
  
  /*
   * Runs the actions of the game: updating positions, removing expired objects, and updating velocities
   */
  public void tick() {
    updatePositions();
    prune();
    updateVelocities();
    if (paddle.loadedBall == null && activeBalls.isEmpty()) // loads a ball if one is needed
      paddle.loadedBall = availableBalls.remove();
  }
  
  /*
   * Moves each game object by, for x and y individually, adding its velocity to its position
   */
  private void updatePositions() {
    for (Ball ball : activeBalls) {
      ball.x += ball.xv;
      ball.y += ball.yv;
    }
    for (Bullet bullet : bullets) {
      bullet.x += bullet.xv;
      bullet.y += bullet.yv;
    }
    for (Powerup powerup : powerups) {
      powerup.x += powerup.xv;
      powerup.y += powerup.yv;
    }
    if (paddle.x < paddle.size / 2)
      paddle.x = paddle.size / 2;
    else if (paddle.x > 80 - paddle.size / 2)
      paddle.x = 80 - paddle.size / 2;
  }
  
  /*
   * Removes expired (e.g. broken, offscreen) objects from the field
   */
  private void prune() {
    Iterator<Ball> i0 = activeBalls.iterator();
    while (i0.hasNext())
      if (i0.next().y < 0.5) { // if the ball is past the bottom edge of the field...
        i0.remove();
      }
    Iterator<Brick> i1 = bricks.iterator();
    while (i1.hasNext())
      if (i1.next().broken) {
        i1.remove();
        score += 100;
      }
    Iterator<Bullet> i2 = bullets.iterator();
    while (i2.hasNext())
      if (i2.next().y > 60) // if the bullet is past the top edge of the field...
        i2.remove();
  }
  
  private void updateVelocities() { // collision detection
    Iterator<Ball> i = activeBalls.iterator();
    while (i.hasNext()) {
      Ball ball = i.next();
      
      // ball and left field edge
      if (ball.x < 0.5) {
        ball.xv = Math.abs(ball.xv); // absolute value ensures that opposite velocities don't cancel out in adjacent ticks
        ball.x = 1-ball.x;
      }
      
      // ball and right field edge
      else if (ball.x > 79.5) {
        ball.xv = -Math.abs(ball.xv);
        ball.x = 80-(1-(80-ball.x));
      }
      
      // ball and top field edge
      if (ball.y > 59.5) {
        ball.yv = -Math.abs(ball.yv);
        ball.y = 60-(1-(60-ball.y));
      }
      
      // ball and paddle
      if (ball.y < 2.5 && ball.x > paddle.x - paddle.size / 2 - 0.5 && ball.x < paddle.x + paddle.size / 2 + 0.5) {
        // properties of the current velocity of the ball
        double direction = Math.atan2(ball.yv, ball.xv);
        double magnitude = Math.sqrt(ball.xv * ball.xv + ball.yv * ball.yv);
        // tiltAxis allows the simulation of a tilted paddle so that the ball bounces horizontally away from the paddle when it hits
        double tiltAxis = Math.PI / 2 - 0.6 * (ball.x - paddle.x) / paddle.size;
        direction = Math.PI + tiltAxis - (direction - tiltAxis);
        ball.xv = magnitude * Math.cos(direction);
        ball.yv = magnitude * Math.sin(direction);
        ball.y = 2.5 + (2.5 - ball.y);
      }
      
      // ball and brick
      for (Brick brick: bricks) {
        if (ball.x > brick.x - 0.5 && ball.x < brick.x + 4 + 0.5) { // if the ball is inside the brick with respect to x...
          if (ball.y > brick.y - 0.5 && ball.y < brick.y) {
            brick.hit(ball.strong);
            if (!ball.strong) {
              ball.yv = -Math.abs(ball.yv);
              ball.y = brick.y - 0.5 - (ball.y + 0.5 - brick.y);
            }
          }
          else if (ball.y < brick.y + 2 + 0.5 && ball.y > brick.y + 2) {
            brick.hit(ball.strong);
            if (!ball.strong) {
              ball.yv = Math.abs(ball.yv);
              ball.y = brick.y + 2 + 0.5 + (brick.y + 2 + 0.5 - ball.y);
            }
          }
        }
        if (ball.y > brick.y - 0.5 && ball.y < brick.y + 2 + 0.5) { // if the ball is inside the brick with respect to y...
          if (ball.x > brick.x - 0.5 && ball.x < brick.x) {
            brick.hit(ball.strong);
            if (!ball.strong) {
              ball.xv = -Math.abs(ball.xv);
              ball.x = brick.x - 0.5 - (ball.x + 0.5 - brick.x);
            }
          }
          else if (ball.x < brick.x + 4 + 0.5 && ball.x > brick.x + 4) {
            brick.hit(ball.strong);
            if (!ball.strong) {
              ball.xv = Math.abs(ball.xv);
              ball.x = brick.x + 4 + 0.5 + (brick.x + 4 + 0.5 - ball.x);
            }
          }
        }
      }
    }
  }
  
  /*
   * Moves the paddle based on the value of the PaddleMoveEvent enum provided
   */
  public void onPaddleMoveEvent(PaddleMoveEvent e) {
    if (e == PaddleMoveEvent.left)
      paddle.x -= 0.5;
    else if (e == PaddleMoveEvent.right)
      paddle.x += 0.5;
  }
  
  /*
   * (Not yet implemented)
   */
  public void onShootEvent() {
  }
  
  public void onBallLaunchEvent() {
    Ball ball3r = paddle.launch();
    if (ball3r != null)
      activeBalls.add(ball3r);
  }
  
  /*
   * Returns whether the game is over (i.e. the player has won or lost)
   */
  public boolean isOver() {
    return (activeBalls.isEmpty() && paddle.loadedBall == null && availableBalls.size() == 0) || isWon();
  }
  
  /*
   * Returns whether the player has won the game
   */
  public boolean isWon() {
    return (bricks.isEmpty());
  }
}
