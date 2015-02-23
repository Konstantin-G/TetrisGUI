package tetris;

import tetris.tetriminos.Tetriminos;
import tetris.tetriminos.TetriminosFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * Created by kashey on 2/21/15.
 */
public class Tetris extends JApplet {

    static Tetris tetris;
    private PlayThread t;
    private boolean isStopped = false;

    private Font infoFont = new Font("SHERIF", Font.BOLD, 12);
    private Font pauseFont = new Font("SHERIF", Font.BOLD, 50);
    private Font defaultFont;

    private Color backgroundColor = new Color(71, 70, 71);

// If i want run app in the window
//    public static void main(String[] args) {
//
//        // create and set up the applet
//        Tetris applet = new Tetris();
//        applet.setPreferredSize(new Dimension(540, 420));
//        applet.init();
//
//        // create a frame to host the applet, which is just another type of Swing Component
//        JFrame mainFrame = new JFrame();
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // add the applet to the frame and show it
//        mainFrame.add(applet);
//        mainFrame.pack();
//        mainFrame.setVisible(true);
//
//        // start the applet
//        applet.start();
//    }



    public void init(){
        // If i want run app in the window
//        try {
//            SwingUtilities.invokeAndWait(new Runnable() {
//                public void run() {
//                   makeGUI();
//                }
//            });
//        } catch (Exception e) {
//            System.err.println("createGUI didn't complete successfully");
//        }
        // If i want run like Applet
        makeGUI();
        tetris = this;
        defaultFont = tetris.getFont();

        t = new PlayThread();
        t.start();
    }

    private void makeGUI(){
        setSize(new Dimension(540, 420));
        //Настройка аплета для использования компоновки потоков.
//        setLayout(new FlowLayout());
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (null != PlayThread.getFalling()) {
                    //If equals 'ESC' - immediately live the game.
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                        System.exit(0);
                    //If "LEFT" - move tetriminos to left
                    if (e.getKeyCode() == KeyEvent.VK_LEFT)
                        PlayThread.getFalling().moveToLeft();
                        //If "RIGHT" - move tetriminos to right
                    else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                        PlayThread.getFalling().moveToRight();
                        //If "DOWN" - clockwise rotate tetriminos
                    else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        PlayThread.getFalling().rotateDirection("CLOCKWISE");
                        //If "UP" - anticlockwise rotate tetriminos
                    else if (e.getKeyCode() == KeyEvent.VK_UP)
                        PlayThread.getFalling().rotateDirection("ANTICLOCKWISE");
                        //If "SPACE" Drop down tetriminos
                    else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                        PlayThread.dropDown();
                        //If "PAUSE" pause the game
                    else if (e.getKeyCode() == KeyEvent.VK_PAUSE) {
                        if (!isStopped) {
                            showStatus("pause");
                            isStopped = true;
                            stop();
                        } else {
                            showStatus("game");
                            isStopped = false;
                            start();
                        }
                    }
                }
                repaint();
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        t.suspend();
    }

    @Override
    public void start() {
        super.start();
        t.resume();
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.blue);
        g.drawLine(8, 10, 8, 412);
        g.drawLine(212, 10, 212, 412);
        g.drawLine(8, 412, 212, 412);
        g.setColor(backgroundColor);
        g.fillRect(10, 0, 201, 411);

        for (int y = 2; y < PlayThread.SIDE_Y - 1; y++) {
            for (int x = 2; x < PlayThread.SIDE_X - 2; x++) {
                char cross = PlayThread.MATRIX[y][x];
                if (PlayThread.MATRIX[y][x] != ' '){
                    switch (cross) {
                        case '0' : g.setColor(Color.MAGENTA);
                            break;
                        case '1' : g.setColor(Color.BLUE);
                            break;
                        case '2' : g.setColor(Color.ORANGE);
                            break;
                        case '3' : g.setColor(Color.YELLOW);
                            break;
                        case '4' : g.setColor(Color.GREEN);
                            break;
                        case '5' : g.setColor(Color.CYAN);
                            break;
                        case '6' : g.setColor(Color.RED);
                            break;
                        default: g.setColor(backgroundColor);
                    }
                    g.drawRect(10 + (x - 2) * 20, 10 + (y - 2) * 20, 20, 20);
                    g.fillRect(10 + (x - 2) * 20 + 3, 10 + (y - 2) * 20 + 3, 17, 17);
                }
            }
        }

        if (null != PlayThread.getFalling()) {
            for (Coordinates c : PlayThread.getFalling().coordinates) {
                int x = c.getX();
                int y = c.getY();
                char cross = PlayThread.getFalling().TETRIMINOS_CHAR;
                    switch (cross) {
                        case '0':
                            g.setColor(Color.MAGENTA);
                            break;
                        case '1':
                            g.setColor(Color.BLUE);
                            break;
                        case '2':
                            g.setColor(Color.ORANGE);
                            break;
                        case '3':
                            g.setColor(Color.YELLOW);
                            break;
                        case '4':
                            g.setColor(Color.GREEN);
                            break;
                        case '5':
                            g.setColor(Color.CYAN);
                            break;
                        case '6':
                            g.setColor(Color.RED);
                            break;
                        default:
                            g.setColor(backgroundColor);
                    }
                g.drawRect(10 + (x - 2) * 20, 10 + (y - 2) * 20, 20, 20);
                g.fillRect(10 + (x - 2) * 20 + 3, 10 + (y - 2) * 20 + 3, 17, 17);
            }
        }
        g.setColor(backgroundColor);
        g.fillRect(300, 50, 90, 90);

        g.setColor(Color.blue);
        for (int nextY = 0; nextY < 4; nextY++) {
            for (int nextX = 0; nextX < PlayThread.getNext().length; nextX++) {
                char cross = PlayThread.getNext()[nextY][nextX];
                if (cross != ' ') {
                    switch (cross) {
                        case '0':
                            g.setColor(Color.MAGENTA);
                            break;
                        case '1':
                            g.setColor(Color.BLUE);
                            break;
                        case '2':
                            g.setColor(Color.ORANGE);
                            break;
                        case '3':
                            g.setColor(Color.YELLOW);
                            break;
                        case '4':
                            g.setColor(Color.GREEN);
                            break;
                        case '5':
                            g.setColor(Color.CYAN);
                            break;
                        case '6':
                            g.setColor(Color.RED);
                            break;
                        default:
                            g.setColor(backgroundColor);
                    }
                    g.drawRect(305 + nextX * 20, 55 + nextY * 20, 20, 20);
                    g.fillRect(305 + nextX * 20 + 3, 55 + nextY * 20 + 3, 17, 17);
                }
            }
        }

        g.setFont(infoFont);
        g.setColor(Color.black);
        g.drawString("NEXT: ", 300, 40);
        g.drawString(String.format("SCORE:   %d", PlayThread.getScore()), 300, 180);
        g.drawString(String.format("LEVEL:   %d", PlayThread.getLevel()), 300, 200);
        g.drawString("CONTROL:", 300, 240);
        g.drawString("LEFT:    MOVE TO LEFT", 300, 260);
        g.drawString("RIGHT:   MOVE TO RIGHT", 300, 280);
        g.drawString("UP:      ANTICLOCKWISE ROTATION", 300, 300);
        g.drawString("DOWN:    CLOCKWISE ROTATION", 300, 320);
        g.drawString("SPACE:   DROP", 300, 340);
        g.drawString("ESC:     LEAVE THE GAME", 300, 360);
        g.drawString("PAUSE:     PAUSE THE GAME", 300, 380);
        if (isStopped){
            g.setXORMode(Color.white);
            g.setFont(pauseFont);
            g.drawString("PAUSE",18, 220);
            g.setPaintMode();
            setFont(defaultFont);
        }
    }


}
