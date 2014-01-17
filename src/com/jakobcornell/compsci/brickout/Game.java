package com.jakobcornell.compsci.brickout;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {
  public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
    Game game = new Game();
    game.availableBalls = new LifeBasedBallDispenser(3);
    game.paddle = new Paddle();
    while(game.availableBalls.size()>0) {
      Field f = Level.getFromFile(new File("level.ser"));
      game.levelLifeCycle(f);
    }
  }
  
  private JFrame frame;
  private Paddle paddle;
  private LifeBasedBallDispenser availableBalls;
  
  public Game() {
    frame = new JFrame();
    frame.setResizable(false);
    frame.setVisible(true);
  }
  
  private void levelLifeCycle(final Field field) throws InterruptedException {
    final JPanel panel = new JPanel() {
      private static final long serialVersionUID = 1L;
      public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
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
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
          field.onBallLaunchEvent();
      }
      
      public void keyReleased(KeyEvent arg0) {}
      
      public void keyTyped(KeyEvent arg0) {}
    });
    panel.setFocusable(true);
    frame.getContentPane().add(panel);
    frame.pack();
    field.initialize(availableBalls, paddle);;
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      public void run() {
        field.tick();
        panel.repaint(0);
      }
    }, 0, 30);
    while(!field.isOver())
      Thread.sleep(100);
    timer.cancel();
    if (field.isOver())
      System.out.println("FAIL");
    else
      System.out.println("WINZ");
    System.out.println("Score: "+field.score);
    frame.remove(panel);
  }
}
