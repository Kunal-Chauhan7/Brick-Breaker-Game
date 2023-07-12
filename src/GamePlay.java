import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private Mapgenrator map;

    public GamePlay() {
        map = new Mapgenrator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D)g);

        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(682, 0, 3, 592);

        g.setColor(Color.WHITE);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,590,30);

        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.YELLOW);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

        if (totalBricks <= 0){
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.blue);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You Win !!!, Score: "+score,190,300);
            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        }

        if (ballPositionY > 570){
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.blue);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over!! , Score: "+score,190,300);
            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play){

//            ballPositionX += ballXDir * 2;  // Increase the increment by multiplying with 2
//            ballPositionY += ballYDir * 2;  // Increase the increment by multiplying with 2

            if (new Rectangle(ballPositionX,ballPositionY,20,20).intersects(new Rectangle(playerX,550,100,8))){
                ballYDir=-ballYDir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j*map.brickWidth+80;
                        int brickY = i*map.brickHeight+50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX,ballPositionY,20,20);
                        Rectangle brickRect = rect;
                        if (ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score+=5;

                            if (ballPositionX + 19 <= brickRect.x || ballPositionX+1 >= brickRect.x+brickRect.width){
                                ballXDir=-ballXDir;
                            }
                            else{
                                ballYDir =-ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX+=ballXDir;
            ballPositionY+=ballYDir;


            if (ballPositionX < 0){
                ballXDir=-ballXDir;
            }
            if (ballPositionY < 0){
                ballYDir=-ballYDir;
            }
            if (ballPositionX > 670){
                ballXDir=-ballXDir;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 0) {
                playerX = 0;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            if (!play){
                play=true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new Mapgenrator(3,7);

                repaint();
            }
        }
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
