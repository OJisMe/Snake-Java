import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

  static final int SCREEN_W = 600;
  static final int SCREEN_H = 600;

  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = (SCREEN_W * SCREEN_W) / UNIT_SIZE;
  static final int DELAY = 85;

  // todo 2d array
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  int bodyParts = 6;
  int applesEaten;

  // todo point?
  int appleX;
  int appleY;

  // TODO enum?
  char direction = 'R';
  boolean running = false;
  Timer timer;
  Random random;

  GamePanel() {
    random = new Random();
    setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
    setBackground(new Color(100,150,100));
    setFocusable(true);
    addKeyListener(new MyKeyAdapter());
    startGame();
  }

  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    if (running) {
      //creates a grid (optional)
      g.setColor(Color.darkGray);
      for (int i = 0; i < SCREEN_H / UNIT_SIZE; i++) {
        g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_H);
      }
      for (int i = 0; i < SCREEN_W / UNIT_SIZE; i++) {
        g.drawLine(0, i * UNIT_SIZE, SCREEN_W, i * UNIT_SIZE);
      }
      g.setColor(new Color(174,30,40));
      g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

      for (int i = 0; i < bodyParts; i++) {
        if (i == 0) {
          g.setColor(new Color(50, 150, 100));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        } else {
          g.setColor(new Color(50, 100, 100));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }
      g.setColor(Color.RED);
      g.setFont(new Font("Apple Chancery", Font.BOLD, 40));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("Score :"+applesEaten, (SCREEN_W - metrics.stringWidth("Score :"+applesEaten)) / 2, g.getFont().getSize());

    } else {
      gameOver(g);
    }
  }

  public void newApple() {
    appleX = random.nextInt(SCREEN_W / UNIT_SIZE) * UNIT_SIZE;
    appleY = random.nextInt(SCREEN_H / UNIT_SIZE) * UNIT_SIZE;
  }

  public void move() {
    for (int i = bodyParts; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];
    }

    switch (direction) {
      case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }
  }

  public void checkApple() {
    if (x[0] == appleX && y[0] == appleY) {
      bodyParts++;
      applesEaten++;
      newApple();
    }
  }

  public void checkCollisions() {
    //check body
    for (int i = bodyParts; i > 0; i--) {
      if (x[0] == x[i] && y[0] == y[i]) {
        running = false;
      }
    }

    //check left wall
    if (x[0] < 0) {
      running = false;
    }
    //check right wall
    if (x[0] > SCREEN_W) {
      running = false;
    }
    //check top wall
    if (y[0] < 0) {
      running = false;
    }
    //check bottom wall
    if (y[0] > SCREEN_H) {
      running = false;
    }
    if (!running) {
      timer.stop();
    }

  }

  public void gameOver(Graphics g) {
    g.setColor(Color.red);
    //score
    g.setFont(new Font("Apple Chancery", Font.BOLD, 40));
    FontMetrics metrics0 = getFontMetrics(g.getFont());
    g.drawString("Score :"+applesEaten, (SCREEN_W - metrics0.stringWidth("Score :"+applesEaten)) / 2, g.getFont().getSize());

//Game Over Text
    g.setFont(new Font("Apple Chancery", Font.BOLD, 75));
    FontMetrics metrics = getFontMetrics((g.getFont()));
    g.drawString("Game Over", (SCREEN_W - metrics.stringWidth("Game Over")) / 2, SCREEN_H / 2);
  }


  /**
   * Invoked when an action occurs.
   *
   * @param e the event to be processed
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollisions();
    }
    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
          if (direction != 'R') {
            direction = 'L';
          }
          break;

        case KeyEvent.VK_RIGHT:
          if (direction != 'L') {
            direction = 'R';
          }
          break;

        case KeyEvent.VK_UP:
          if (direction != 'D') {
            direction = 'U';
          }
          break;

        case KeyEvent.VK_DOWN:
          if (direction != 'U') {
            direction = 'D';
          }
          break;
      }
    }
  }
}
