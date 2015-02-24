package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * Created by Konstantin Garkusha on 2/21/15.
 */
public class Tetris extends JFrame{
    public static JPanel basicPanel;
    static JPanel leftPanel;
    private static JPanel rightPanel;
    private static JPanel rightTopPanel;
    static JPanel rightBottomPanel;
    static Tetris tetris;
    private PlayThread playThread;
    private boolean isStopped = false;

    private static final Dimension SCREEN_SIZE = new Dimension(500, 540);

    private static final Font INFO_FONT = new Font("SHERIF", Font.BOLD, 12);
    private static final Font PAUSE_FONT = new Font("SHERIF", Font.BOLD, 50);
    private Font defaultFont;

    private static final Color AREA_BACKGROUND_COLOR = new Color(43, 43, 43);
    private static final Color MATRIX_BACKGROUND_COLOR = new Color(71, 70, 71);
    private static final Color MATRIX3D_COLOR = new Color(30, 30, 30);



    public Tetris() {
        makeGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tetris();
            }
        });
    }

    private void makeHelpFrame(){
        JFrame helpFrame = new JFrame();
        helpFrame.setTitle("Help");
//        helpFrame.pack();
        helpFrame.setSize(SCREEN_SIZE);
        helpFrame.setResizable(false);
        helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        helpFrame.setVisible(true);
        setLocationRelativeTo(null);
    }

    private void makeGUI(){
        /**MAIN panel*/
        basicPanel = new JPanel(new BorderLayout());
        basicPanel.setBackground(AREA_BACKGROUND_COLOR);
        add(basicPanel);

        /**MENUBAR panel*/
        JMenuBar menubar = new JMenuBar();
        menubar.setBackground(new Color(102, 102, 102));
        JMenu menu = new JMenu("Menu");


        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        menu.add(exit);
        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(e -> makeHelpFrame());
        menu.add(help);
        menubar.add(menu);
        setJMenuBar(menubar);


        /**LEFT panel*/
        leftPanel = new JPanel(new FlowLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(AREA_BACKGROUND_COLOR);
                g.fillRect(0, 0, leftPanel.getWidth(), leftPanel.getHeight());

                g.setColor(MATRIX3D_COLOR);
                g.drawLine(212, 0, 212, 412);
                g.drawLine(213, 0, 213, 413);
                g.drawLine(214, 0, 214, 414);
                g.drawLine(10, 412, 212, 412);
                g.drawLine(11, 413, 213, 413);
                g.drawLine(12, 414, 214, 414);

                g.setColor(MATRIX_BACKGROUND_COLOR);
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
                                default: g.setColor(MATRIX_BACKGROUND_COLOR);
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
                                g.setColor(MATRIX_BACKGROUND_COLOR);
                        }
                        g.drawRect(10 + (x - 2) * 20, 10 + (y - 2) * 20, 20, 20);
                        g.fillRect(10 + (x - 2) * 20 + 3, 10 + (y - 2) * 20 + 3, 17, 17);
                    }
                }
            }
        };
        leftPanel.add(Box.createRigidArea(new Dimension(220,0)));
        basicPanel.add(leftPanel,  BorderLayout.WEST);

        /**RIGHT panel*/
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(Box.createRigidArea(new Dimension(260, 0)));
            /**RIGHT TOP panel*/
        rightTopPanel = new JPanel(new FlowLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(AREA_BACKGROUND_COLOR);
                g.fillRect(0, 0, rightTopPanel.getWidth(), rightTopPanel.getHeight());

                g.setColor(MATRIX_BACKGROUND_COLOR);
                g.fillRect(10, 50, 90, 90);

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
                                    g.setColor(MATRIX_BACKGROUND_COLOR);
                            }
                            g.drawRect(15 + nextX * 20, 55 + nextY * 20, 20, 20);
                            g.fillRect(15 + nextX * 20 + 3, 55 + nextY * 20 + 3, 17, 17);
                        }
                    }
                }

                g.setFont(INFO_FONT);
                g.setColor(Color.black);
                g.drawString("NEXT: ", 10, 30);
                g.drawString(String.format("SCORE:   %d", PlayThread.getScore()), 10, 160);
                g.drawString(String.format("LEVEL:   %d", PlayThread.getLevel()), 10, 180);
                g.drawString("CONTROL:", 10, 240);
                g.drawString("LEFT:", 10, 260);            g.drawString("MOVE TO LEFT", 70, 260);
                g.drawString("RIGHT:", 10, 280);           g.drawString("MOVE TO RIGHT", 70, 280);
                g.drawString("UP:", 10, 300);              g.drawString("COUNTERCLOCKWISE ROTATION", 70, 300);
                g.drawString("DOWN:", 10, 320);            g.drawString("CLOCKWISE ROTATION", 70, 320);
                g.drawString("SPACE:", 10, 340);           g.drawString("DROP", 70, 340);
                g.drawString("ESC:", 10, 360);             g.drawString("LEAVE THE GAME", 70, 360);
                g.drawString("PAUSE:", 10, 380);           g.drawString("PAUSE THE GAME", 70, 380);
                if (isStopped){
                    g.setXORMode(Color.white);
                    g.setFont(PAUSE_FONT);
                    g.drawString("PAUSE",18, 220);
                    g.setPaintMode();
                    setFont(defaultFont);
                }
            }
        };
        rightPanel.add(rightTopPanel);
            /**RIGHT BOTTOM panel*/
//        rightBottomPanel = new JPanel(new FlowLayout());
//
//        JTextPane jTextPane= new JTextPane();
//        jTextPane.setContentType("text/text");
//        jTextPane.setText("Press F1 to help");
//        jTextPane.setBackground(MATRIX_BACKGROUND_COLOR);
//        jTextPane.setEditable(false);
//        rightBottomPanel.add(jTextPane);
//        rightPanel.add(rightBottomPanel);

        basicPanel.add(rightPanel,  BorderLayout.EAST);


        setTitle("Tetris game by Konstantin Garkusha");
        pack();
        setSize(SCREEN_SIZE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setFocusable(true);

        defaultFont = getFont();


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

        setVisible(true);

        playThread = new PlayThread();
        playThread.start();


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
                        PlayThread.getFalling().rotateDirection("COUNTERCLOCKWISE");
                        //If "SPACE" Drop down tetriminos
                    else if (e.getKeyCode() == KeyEvent.VK_SPACE)
                        PlayThread.dropDown();
                        //If "PAUSE" pause the game
                    else if (e.getKeyCode() == KeyEvent.VK_PAUSE) {
                        if (!isStopped) {
                            isStopped = true;
                            playThread.suspend();
                        } else {
                            isStopped = false;
                            playThread.resume();
                        }
                    }
                }
                repaint();
            }
        });
    }
}
