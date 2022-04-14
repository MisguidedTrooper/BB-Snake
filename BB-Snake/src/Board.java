//Import modules
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.*;
import java.util.*;

public class Board extends JPanel implements ActionListener {

	//Declare the size of the board
    private final int B_WIDTH = 1000;
    private final int B_HEIGHT = 1000;
    private final int DOT_SIZE = 50;
    private final int ALL_DOTS = 400;
    //Used to spawn the scrap metal in random places
    private final int RAND_POS = 20;
    //Sets game speed
    private final int DELAY = 50;
    
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int scrap_x;
    private int scrap_y;
    //Start the game going right
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image scrap;
    private Image head;
    private int hs = 0;

//Set up the board
    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }
    //Load BB-8 images
    private void loadImages() {
        ImageIcon iid = new ImageIcon(getClass().getClassLoader().getResource("b.png"));
        ball = iid.getImage(); 
        
        ImageIcon iia = new ImageIcon(getClass().getClassLoader().getResource("scrap.png"));
        scrap = iia.getImage();
        
        ImageIcon iih = new ImageIcon(getClass().getClassLoader().getResource("h.png"));
        head = iih.getImage();
    }
    //Start game
    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locatescrap();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    //Draws the graphics
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(scrap, scrap_x, scrap_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
            Score(g);
            HighScore(g);
            
        } else {

            gameOver(g);
            Score(g);
        }        
    }
    //Score
    private void Score(Graphics g) {
        int score = (dots - 3) * 10;
        String msg = "Score: "+ score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }
    
    private void HighScore(Graphics g) {

        int hstemp = (dots - 3) * 10;
        if (hstemp>hs) {
        	hs = hstemp;
        }
        String msg = "Highscore: "+ hs;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, 20);
    }
    //Game Over
    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, (B_HEIGHT / 2)-30);
        inGame = true;
        initGame();

    }
    //Relocate the scrap in a random position if it has been consumed
    private void checkscrap() {

        if ((x[0] == scrap_x) && (y[0] == scrap_y)) {

            dots++;
            locatescrap();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }
    //Stop the game if you have hit the edge, or yourself
    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }
    //Method for relocating scrap
    private void locatescrap() {

        int r = (int) (Math.random() * RAND_POS);
        scrap_x = ((r * DOT_SIZE));
        
        if (scrap_x>900) {
        	scrap_x = scrap_x - 50;
        	
        }
        
        if (scrap_x==0) {
        	scrap_x = scrap_x + 50;
        	
        }
        
        r = (int) (Math.random() * RAND_POS);
        scrap_y = ((r * DOT_SIZE));
        
        if (scrap_y>900) {
        	scrap_y = scrap_y - 50;
        }
        
        if (scrap_y == 0) {
        	scrap_y = scrap_y + 50;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkscrap();
            checkCollision();
            move();
        }

        repaint();
    }
    //Allows the user to control the snake using the arrow keys
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_A ||key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_D ||key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_W ||key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_S ||key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
