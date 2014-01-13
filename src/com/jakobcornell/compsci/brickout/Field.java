package com.jakobcornell.compsci.brickout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Field {
  public transient int score;
  private transient LifeBasedBallDispenser availableBalls;
  private Set<Ball> activeBalls;
  private Set<Brick> bricks;
  private Set<Bullet> bullets;
  private Set<Powerup> powerups;
  private transient Paddle paddle;
  
  public Field() {
    activeBalls = new HashSet<Ball>();
    bricks = new HashSet<Brick>();
    bullets = new HashSet<Bullet>();
    powerups = new HashSet<Powerup>();
    for (int i = 10; i < 70; i += 20)
      for (int j = 30; j < 60; j += 10)
        bricks.add(new Brick(i, j));
  }
  
  public static void main(String[] args) {
    final Field field = new Field();
    JFrame frame = new JFrame();
    final JPanel panel = new JPanel() {
      public void paintComponent(Graphics gr) {
        Graphics2D g = ((Graphics2D) gr);
        g.scale(10, -10);
        g.translate(0, -60);
        field.paint(g);
      }
    };
    panel.setPreferredSize(new Dimension(800, 600));
    panel.addKeyListener(new KeyListener(){
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
          field.onPaddleMoveEvent(PaddleMoveEvent.left);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
          field.onPaddleMoveEvent(PaddleMoveEvent.right);
        else if (e.getKeyCode() == KeyEvent.VK_UP)
          field.onBallLaunchEvent();
      }
      
      public void keyReleased(KeyEvent arg0) {}
      
      public void keyTyped(KeyEvent arg0) {}
    });
    panel.setFocusable(true);
    frame.getContentPane().add(panel);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    field.initialize(new LifeBasedBallDispenser(3), new Paddle());
    final Timer timer = new Timer();
    final Runnable done = new Runnable() {
      public void run() {
        timer.cancel();
        if (field.isOver())
          System.out.println("FAIL");
        else
          System.out.println("WINZ");
      }
    };
    timer.schedule(new TimerTask() {
      public void run() {
        field.tick();
        panel.repaint(0);
        if (field.isOver() || field.isWon()) {
          done.run();
        }
      }
    }, 0, 30);
  }
  
  public void initialize(LifeBasedBallDispenser availableBalls, Paddle paddle) {
    this.availableBalls = availableBalls;
    this.paddle = paddle;
    paddle.loadedBall = new Ball();
  }
  
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
  
  public void tick() {
    updatePositions();
    prune();
    updateVelocities();
  }
  
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
  
  private void prune() {
    Iterator<Ball> i0 = activeBalls.iterator();
    while (i0.hasNext())
      if (i0.next().y < 0.5) {
        i0.remove();
        paddle.loadedBall = availableBalls.remove();
      }
    Iterator<Brick> i1 = bricks.iterator();
    while (i1.hasNext())
      if (i1.next().broken)
        i1.remove();
    Iterator<Bullet> i2 = bullets.iterator();
    while (i2.hasNext())
      if (i2.next().y > 60)
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
        double direction = Math.atan2(ball.yv, ball.xv);
        double magnitude = Math.sqrt(ball.xv * ball.xv + ball.yv * ball.yv);
        double tiltAxis = Math.PI / 2 - 0.6 * (ball.x - paddle.x) / paddle.size;
        direction = Math.PI + tiltAxis - (direction - tiltAxis);
        ball.xv = magnitude * Math.cos(direction);
        ball.yv = magnitude * Math.sin(direction);
        ball.y = 2.5 + (2.5 - ball.y);
      }
      // ball and brick
      for (Brick brick: bricks) {
        if (ball.x > brick.x - 0.5 && ball.x < brick.x + 4 + 0.5) {
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
      }
    }
  }
  
  public void onPaddleMoveEvent(PaddleMoveEvent e) {
    if (e == PaddleMoveEvent.left)
      paddle.x -= 0.5;
    else if (e == PaddleMoveEvent.right)
      paddle.x += 0.5;
  }
  
  public void onShootEvent() {
    
  }
  
  public void onBallLaunchEvent() {
    Ball ball3r = paddle.launch();
    if (ball3r != null)
      activeBalls.add(ball3r);
  }
  
  public boolean isOver() {
    return (activeBalls.isEmpty() && paddle.loadedBall == null && availableBalls.size() == 0);
  }
  
  public boolean isWon() {
    return (bricks.isEmpty());
  }
}
