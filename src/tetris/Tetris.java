package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *
 * Created by Konstantin Garkusha on 2/21/15.
 * ===============================
 * | Game is under construction! |
 * ===============================
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
        SwingUtilities.invokeLater(() -> new Tetris());
    }

    private void helpFrame(){
        JFrame helpFrame = new JFrame("Help");
        // Text container
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        String help = PrepareInformation.getHelp();
        textPane.setText(help);
        textPane.setEditable(false);

        textPanel.add(textPane);
        helpFrame.add(textPanel);

        // setup frame
        helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        helpFrame.setPreferredSize(SCREEN_SIZE);
        helpFrame.pack();
        helpFrame.setVisible(true);
        helpFrame.setLocationRelativeTo(null);
    }

    private void errorFrame(String error){
        JFrame errorFrame = new JFrame("Error");
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        errorFrame.add(basic);
        basic.add(Box.createVerticalGlue());
        // Text field
        JPanel message = new JPanel();
        message.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(error);
        label.setBorder(BorderFactory.createEmptyBorder(5,15,0,0));
        message.add(label);
        basic.add(message);

        //Button field
        JPanel button = new JPanel();
        button.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Close");
        // Listener, which will close error frame
        close.addActionListener(e -> errorFrame.dispatchEvent(new WindowEvent(errorFrame, WindowEvent.WINDOW_CLOSING)));
        button.add(close);
        button.add(Box.createRigidArea(new Dimension(15, 0)));
        basic.add(button);

        basic.add(Box.createRigidArea(new Dimension(0,5)));

        // setup the frame
        errorFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        errorFrame.setPreferredSize(new Dimension(300, 120));
        errorFrame.pack();
        errorFrame.setVisible(true);
        errorFrame.setResizable(false);
        errorFrame.setLocationRelativeTo(null);
    }

    private void aboutTetrisFrame(){
        JFrame helpFrame = new JFrame("About Tetris");
        // Text container
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        String aboutTetris = PrepareInformation.getAboutTetris();
        textPane.setText(aboutTetris);
        textPane.setEditable(false);
        // wrap JTextPane to the JScrollPane to add scrollable
        JScrollPane jsp = new JScrollPane(textPane);
        textPanel.add(jsp);
        helpFrame.add(textPanel);

        // setup frame
        helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        helpFrame.setPreferredSize(SCREEN_SIZE);
        helpFrame.pack();
        helpFrame.setVisible(true);
        helpFrame.setLocationRelativeTo(null);
    }

    private void restartApp()
    {

        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        try {
            final File currentJar = new File(Tetris.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // is it a jar file?
            if(!currentJar.getName().endsWith(".jar"))
                return;

            // Build command: java -jar application.jar
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            //build and start new command
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch ( IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    private void loadGame(){
        // create full address to file (Ubuntu won't right work without it)
        String loadFile = "game.save";
        // get jar file path + filename
        String loadPath =  Tetris.class.getProtectionDomain().getCodeSource().getLocation().toString();
        // cut "file:" and replace "%20" with " "
        loadPath = loadPath.substring(6).replaceAll("%20", " ");
        // cut filename and add save file
        String load = loadPath.substring(0, loadPath.lastIndexOf('/') + 1) + loadFile;

        File file = new File(load);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            new Serialization().readExternal(ois);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            errorFrame("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
            errorFrame("Saved file is not valid");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            errorFrame("Saved file have an error");
        }

    }

    private void saveGame(){
        // create full address to file (Ubuntu won't right work without it)
        String saveFile = "game.save";
        // get jar file path + filename
        String savePath =  Tetris.class.getProtectionDomain().getCodeSource().getLocation().toString();
        // cut "file:" and replace "%20" with " "
        savePath = savePath.substring(6).replaceAll("%20", " ");
        // cut filename and add save file
        String save = savePath.substring(0, savePath.lastIndexOf('/') + 1) + saveFile;

        File file = new File(save);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            new Serialization().writeExternal(oos);
        } catch (IOException e) {
            e.printStackTrace();
            errorFrame("Can't create save file!");
        }
    }

    private void makeGUI(){
        /**MAIN panel*/
        basicPanel = new JPanel(new BorderLayout());
        basicPanel.setBackground(AREA_BACKGROUND_COLOR);
        add(basicPanel);

        /**MENUBAR panel*/
        JMenuBar menubar = new JMenuBar();
        menubar.setBackground(new Color(102, 102, 102));
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem loadGame = new JMenuItem("Load");
        loadGame.addActionListener(e -> loadGame());

        JMenuItem saveGame = new JMenuItem("Save");
        saveGame.addActionListener(e -> saveGame());

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(e -> helpFrame());

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> restartApp());

        JMenuItem info = new JMenuItem("About Tetris");
        info.addActionListener(e -> aboutTetrisFrame());

        fileMenu.add(loadGame);
        fileMenu.add(saveGame);
        fileMenu.add(restart);
        helpMenu.add(help);
        helpMenu.add(info);
        fileMenu.add(exit);

        menubar.add(fileMenu);
        menubar.add(helpMenu);
        setJMenuBar(menubar);


        /**LEFT panel*/
        leftPanel = new JPanel(new FlowLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                final int START_X = 10;
                final int START_Y = 10;

                // Draw background area
                g.setColor(AREA_BACKGROUND_COLOR);
                g.fillRect(0, 0, leftPanel.getWidth(), leftPanel.getHeight());

                // Draw 3D edging of Matrix
                g.setColor(MATRIX3D_COLOR);
                for (int i = 0; i < 5; i++) {
                    g.drawLine(START_X + 202 + i, START_Y + 1 + i, START_X + 202 + i, START_Y + 413 + i);   //VerticalDirection
                    g.drawLine(START_X + 1 + i, START_Y + 412 + i, START_X + 203 + i, START_Y + 412 + i);  //HorizontalDirection
                }

                g.setColor(MATRIX_BACKGROUND_COLOR);
                g.fillRect(START_X, START_Y, 201, 411);
                // Draw The Matrix
                for (int y = 2; y < PlayThread.SIDE_Y - 1; y++) {
                    for (int x = 2; x < PlayThread.SIDE_X - 2; x++) {
                        char cross = PlayThread.matrix[y][x];
                        if (PlayThread.matrix[y][x] != ' '){
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
                            g.drawRect(START_X + (x - 2) * 20, START_Y + 10 + (y - 2) * 20, 20, 20);
                            g.fillRect(START_X + (x - 2) * 20 + 3, START_Y + 10+ (y - 2) * 20 + 3, 17, 17);
                        }
                    }
                }
                // Draw falling Tetriminos
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
                        g.drawRect(START_X + (x - 2) * 20, START_Y  + 10 + (y - 2) * 20, 20, 20);
                        g.fillRect(START_X + (x - 2) * 20 + 3, START_Y  + 10 + (y - 2) * 20 + 3, 17, 17);
                    }
                }
                //Draw hide line
                g.setColor(AREA_BACKGROUND_COLOR);
                g.fillRect(START_X, 0, 220, 10);
                if (isStopped){
                    g.setXORMode(Color.white);
                    g.setFont(PAUSE_FONT);
                    g.drawString("PAUSE",18, 220);
                    g.setPaintMode();
                    setFont(defaultFont);
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
                g.setColor(MATRIX3D_COLOR);
                for (int i = 0; i < 5; i++) {
                    g.drawLine(101 + i, 51 + i, 101 + i, 141 + i);
                    g.drawLine(11 + i, 141 + i, 101 + i, 141 + i);
                }

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
                g.drawString("\"P\":", 10, 380);           g.drawString("PAUSE THE GAME", 70, 380);

            }
        };
        rightPanel.add(rightTopPanel);
            /**RIGHT BOTTOM panel*/
//        rightBottomPanel = new JPanel(new FlowLayout());
//        JTextPane jTextPane= new JTextPane();
//        jTextPane.setContentType("text/text");
//        jTextPane.setText("Press F1 to help");
//        jTextPane.setBackground(MATRIX_BACKGROUND_COLOR);
//        jTextPane.setEditable(false);
//        rightBottomPanel.add(jTextPane);
//        rightPanel.add(rightBottomPanel);

        basicPanel.add(rightPanel,  BorderLayout.EAST);

        setTitle("Tetris game by Konstantin Garkusha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(SCREEN_SIZE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);

        defaultFont = getFont();
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
                        //If "F1" start help menu
                    else if (e.getKeyCode() == KeyEvent.VK_F1)
                        helpFrame();
                        //If "P" pause the game
                    else if (e.getKeyCode() == KeyEvent.VK_P) {
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
