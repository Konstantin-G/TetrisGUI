package tetris;

import java.io.*;
import java.net.URL;

/**
 * This class encapsulates all the prepare information in the game, so as to separate the information
 * codes from the game codes.
 *
 * @author Konstantin Garkusha
 */
class PrepareInformation {
    private static final String ABOUT_TETRIS = readAboutTetris();
    private static final String HELP = readHelp();

    public static String getAboutTetris() {
        return ABOUT_TETRIS;
    }

    public static String getHelp() {
        return HELP;
    }

    private static String readAboutTetris(){
        URL load = Tetris.class.getClassLoader().getResource("aboutTetris.html");
        String result ="";
        String line;
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(load.openStream()))) {
            while (null != (line = fileReader.readLine())){
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "Can't load file.";
        }
        return result;
    }

    private static String readHelp(){
        URL read = Tetris.class.getClassLoader().getResource("help.html");
        String result ="";
        String line;
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(read.openStream()))) {
            while (null != (line = fileReader.readLine())){
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "Can't load file.";
        }
        return result;
    }
}
