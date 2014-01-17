package com.jakobcornell.compsci.brickout;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game {
  public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException, InvocationTargetException {
    Game game = new Game();
    game.availableBalls = new LifeBasedBallDispenser(3);
    game.paddle = new Paddle();
    while(game.availableBalls.size()>0) {
      Field f = Level.getFromFile(new File("level.ser"));
      game.levelLifeCycle(f);
    }
    // when the game is over, shows a dialog providing score information
    JOptionPane.showMessageDialog(game.frame, String.format("Game over!\nScore: %d", game.score), "Brickout", JOptionPane.INFORMATION_MESSAGE);
    System.exit(0);
  }
  
  private int score;
  private JFrame frame;
  private Paddle paddle;
  private LifeBasedBallDispenser availableBalls;
  
  public Game() {
    frame = new JFrame();
    frame.setResizable(false);
    frame.setVisible(true);
  }
  
  private void levelLifeCycle(final Field field) throws InterruptedException, InvocationTargetException {
    final JPanel panel = new JPanel() {
      private static final long serialVersionUID = 1L;
      public void paintComponent(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.scale(10, -10);
        g.translate(0, -60);
        field.paint(g);
      }
    };
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
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
          
          public void keyReleased(KeyEvent arg0) {} // not really anything
          
          public void keyTyped(KeyEvent arg0) {} // not really anything
        });
        panel.setFocusable(true);
        frame.getContentPane().add(panel);
        frame.pack();
        panel.requestFocusInWindow(); // focuses the game panel so that keyboard input goes there by default
      }
    });
    field.initialize(availableBalls, paddle);
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      public void run() { // actual method that is run each iteration
        field.tick();
        panel.repaint(0);
      }
    }, 0, 30);
    while(!field.isOver())
      Thread.sleep(100);
    timer.cancel(); // ends the timer
    score += field.score; // adds the user's score during the current level to the global player score
    SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        frame.remove(panel);
      }
    });
  }
}
