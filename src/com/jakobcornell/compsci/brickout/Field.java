package com.jakobcornell.compsci.brickout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
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
  protected transient Set<BrickFragment> brickFragments;
  private transient Paddle paddle;
  
  /*
   * Initializes the field with the objects that shouldn't be serialized
   */
  public void initialize(final LifeBasedBallDispenser availableBalls, final Paddle paddle) {
    this.availableBalls = availableBalls;
    this.paddle = paddle;
    brickFragments = Collections.synchronizedSet(new HashSet<BrickFragment>());
  }
  
  /*
   * Paints the objects in this field onto the passed-in Graphics2D object
   */
  protected final static Stroke lineStroke = new BasicStroke(0.1F);
  public void paint(final Graphics2D g) {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 80, 60);
    g.setColor(Color.WHITE);
    g.setStroke(lineStroke);
    synchronized (brickFragments) {
      for (final BrickFragment fragment : brickFragments)
        fragment.paint(g);
    }
    synchronized (activeBalls) {
      for (final Ball ball : activeBalls)
        ball.paint(g);
    }
    synchronized (bricks) {
      for (final Brick brick : bricks)
        brick.paint(g);
    }
    synchronized (bullets) {
      for (final Bullet bullet : bullets)
        bullet.paint(g);
    }
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
    synchronized (activeBalls) {
      for (final Ball ball : activeBalls) {
        ball.x += ball.xv;
        ball.y += ball.yv;
      }
    }
    synchronized (bullets) {
      for (final Bullet bullet : bullets) {
        bullet.x += bullet.xv;
        bullet.y += bullet.yv;
      }
    }
    synchronized (powerups) {
      for (final Powerup powerup : powerups) {
        powerup.x += powerup.xv;
        powerup.y += powerup.yv;
      }
    }
    synchronized (brickFragments) {
      for (final BrickFragment fragment : brickFragments)
        fragment.tick();
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
    synchronized (activeBalls) {
      final Iterator<Ball> i = activeBalls.iterator();
      while (i.hasNext())
        if (i.next().y < 0.5)
          i.remove();
    }
    synchronized (bricks) {
      final Iterator<Brick> i = bricks.iterator();
      while (i.hasNext()) {
        final Brick brick = i.next();
        if (brick.broken) {
          i.remove();
          score += 100;
        }
      }
    }
    synchronized (bullets) {
      final Iterator<Bullet> i = bullets.iterator();
      while (i.hasNext())
        if (i.next().y > 60) // if the bullet is past the top edge of the field...
          i.remove();
    }
    synchronized (brickFragments) {
      final Iterator<BrickFragment> i = brickFragments.iterator();
      while (i.hasNext())
        if (i.next().broken)
          i.remove();
    }
  }
  
  private void updateVelocities() { // collision detection
    synchronized (activeBalls) {
      final Iterator<Ball> i = activeBalls.iterator();
      while (i.hasNext()) {
        final Ball ball = i.next();
        
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
          final double magnitude = Math.sqrt(ball.xv * ball.xv + ball.yv * ball.yv);
          // tiltAxis allows the simulation of a tilted paddle so that the ball bounces horizontally away from the paddle when it hits
          final double tiltAxis = Math.PI / 2 - 0.6 * (ball.x - paddle.x) / paddle.size;
          direction = Math.PI + tiltAxis - (direction - tiltAxis);
          ball.xv = magnitude * Math.cos(direction);
          ball.yv = magnitude * Math.sin(direction);
          ball.y = 2.5 + (2.5 - ball.y);
        }
        
        // ball and brick
        synchronized (bricks) {
          for (final Brick brick: bricks) {
            boolean hit=false;
            if (ball.x > brick.x - 0.5 && ball.x < brick.x + 4 + 0.5)
              if (ball.y > brick.y - 0.5 && ball.y < brick.y) {
                hit=true;
                if (!ball.strong) {
                  ball.yv = -Math.abs(ball.yv);
                  ball.y = brick.y - 0.5 - (ball.y + 0.5 - brick.y);
                }
              }
              else if (ball.y < brick.y + 2 + 0.5 && ball.y > brick.y + 2) {
                hit=true;
                if (!ball.strong) {
                  ball.yv = Math.abs(ball.yv);
                  ball.y = brick.y + 2 + 0.5 + (brick.y + 2 + 0.5 - ball.y);
                }
              }
            if (ball.y > brick.y - 0.5 && ball.y < brick.y + 2 + 0.5)
              if (ball.x > brick.x - 0.5 && ball.x < brick.x) {
                hit=true;
                if (!ball.strong) {
                  ball.xv = -Math.abs(ball.xv);
                  ball.x = brick.x - 0.5 - (ball.x + 0.5 - brick.x);
                }
              }
              else if (ball.x < brick.x + 4 + 0.5 && ball.x > brick.x + 4) {
                hit=true;
                if (!ball.strong) {
                  ball.xv = Math.abs(ball.xv);
                  ball.x = brick.x + 4 + 0.5 + (brick.x + 4 + 0.5 - ball.x);
                }
              }
            if(hit) {
              brick.hit(ball.strong);
              brickFragments.addAll(BrickFragment.explode(brick, 0, 0));
            }
          }
        }
      }
    }
  }
  
  /*
   * Moves the paddle based on the value of the PaddleMoveEvent enum provided
   */
  public void onPaddleMoveEvent(final PaddleMoveEvent e) {
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
    final Ball ball3r = paddle.launch();
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
