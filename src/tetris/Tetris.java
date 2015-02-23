package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * Created by Konstantin Garkusha on 2/21/15.
 */
public class Tetris extends JPanel{
    JFrame mainFrame;

    static Tetris tetris;
    private PlayThread t;
    private boolean isStopped = false;

    private Font infoFont = new Font("SHERIF", Font.BOLD, 12);
    private Font pauseFont = new Font("SHERIF", Font.BOLD, 50);
    private Font defaultFont;

    private Color fieldBackgroundColor = new Color(71, 70, 71);
    private Color areaBackgroundColor = new Color(43, 43, 43);


    public Tetris() {
        tetris = this;
        setPreferredSize(new Dimension(540, 420));
        setFocusable(true);
        makeGUI();
        defaultFont = getFont();

        t = new PlayThread();
        t.start();
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> new Tetris());
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void makeGUI(){

        mainFrame = new JFrame();
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setSize(new Dimension(540, 460));
        mainFrame.setMinimumSize(new Dimension(540, 460));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(this);

//        JButton leftButton = new JButton("LEFT");
//        leftButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(leftButton);
//
//        JButton rightButton = new JButton("RIGHT");
//        rightButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(rightButton);

//        JButton upButton = new JButton("ANTICLOCKWISE_ROTATION");
//        upButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(upButton);
//
//        JButton downButton = new JButton("CLOCKWISE_ROTATION");
//        downButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(downButton);
//
//        JButton spaceButton = new JButton("DROP");
//        spaceButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(spaceButton);
//
//        JButton pauseButton = new JButton("PAUSE");
//        pauseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(pauseButton);
//
//        JButton escButton = new JButton("ESC");
//        escButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        mainFrame.add(escButton);
        mainFrame.pack();
        mainFrame.setVisible(true);


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
                            isStopped = true;
                            t.suspend();
                        } else {
                            isStopped = false;
                            t.resume();
                        }
                    }
                }
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(areaBackgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.blue);
        g.drawLine(8, 10, 8, 412);
        g.drawLine(212, 10, 212, 412);
        g.drawLine(8, 412, 212, 412);

        g.setColor(fieldBackgroundColor);
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
                        default: g.setColor(fieldBackgroundColor);
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
                        g.setColor(fieldBackgroundColor);
                }
                g.drawRect(10 + (x - 2) * 20, 10 + (y - 2) * 20, 20, 20);
                g.fillRect(10 + (x - 2) * 20 + 3, 10 + (y - 2) * 20 + 3, 17, 17);
            }
        }
        g.setColor(fieldBackgroundColor);
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
                            g.setColor(fieldBackgroundColor);
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
