package com.jakobcornell.compsci.brickout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Field {
  public transient int score;
  private transient Queue<Ball> availableBalls;
  private Set<Ball> activeBalls;
  private Set<Brick> bricks;
  private Set<Bullet> bullets;
  private Set<Powerup> powerups;
  private transient Paddle paddle;
  
  public Field() {
    availableBalls = new ArrayDeque<Ball>();
    activeBalls = new HashSet<Ball>();
    bricks = new HashSet<Brick>();
    bullets = new HashSet<Bullet>();
    final Ball ball = new Ball();
    final Brick brick = new Brick();
    final Bullet bullet = new Bullet();
    activeBalls.add(ball);
    bricks.add(brick);
    bullets.add(bullet);
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
      }
      
      public void keyReleased(KeyEvent arg0) {
        
      }
      
      public void keyTyped(KeyEvent arg0) {}
    });
    panel.setFocusable(true);
    frame.getContentPane().add(panel);
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);
    field.initialize(new ArrayDeque<Ball>(), new Paddle());
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      public void run() {
        field.tick();
        panel.repaint();
      }
    }, 0, 30);
  }
  
  public void initialize(Queue<Ball> availableBalls, Paddle paddle) {
    this.availableBalls = availableBalls;
    this.paddle = paddle;
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
    for (Ball ball : activeBalls) {
      ball.x += ball.xv;
      ball.y += ball.yv;
    }
    Iterator<Ball> i = activeBalls.iterator();
    while (i.hasNext()) {
      Ball ball = i.next();
      if (ball.x < 0.5) {
        ball.xv = Math.abs(ball.xv); // absolute value ensures that opposite velocities don't cancel out in adjacent ticks
        ball.x = 1-ball.x;
      }
      else if (ball.x > 79.5) {
        ball.xv = -Math.abs(ball.xv);
        ball.x = 80-(1-(80-ball.x));
      }
      if (ball.y < 0.5) {
        i.remove();
      }
      else if (ball.y > 59.5) {
        ball.yv = -Math.abs(ball.yv);
        ball.y = 60-(1-(60-ball.y));
      }
    }
    for (Bullet bullet : bullets)
      if (bullet.y > 60)
        bullet = null;
    if (paddle.x < paddle.size / 2)
      paddle.x = paddle.size / 2;
    else if (paddle.x > 80 - paddle.size / 2)
      paddle.x = 80 - paddle.size / 2;
  }

  public void onPaddleMoveEvent(PaddleMoveEvent e) {
    if (e == PaddleMoveEvent.left)
      paddle.x -= paddle.xv;
    else if (e == PaddleMoveEvent.right)
      paddle.x += paddle.xv;
  }

  public void onShootEvent() {
    
  }

  public void onBallLaunchEvent() {
    
  }

  public boolean isOver() {
    return false;
  }

  public boolean isWon() {
    return false;
  }
}
