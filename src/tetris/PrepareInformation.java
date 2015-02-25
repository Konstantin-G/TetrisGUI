package tetris;

import java.io.*;

/**
 * Created by Konstantin Garkusha on 25.2.2015.
 *
 */
public class PrepareInformation {
    private static final String ABOUT_TETRIS = readAboutTetris();
    private static String help;

    public static String getAboutTetris() {
        return ABOUT_TETRIS;
    }

    public static String getHelp() {
        return help;
    }

    private static String readAboutTetris(){
        String result ="";
        String line;
        try(BufferedReader fileReader = new BufferedReader(new FileReader("aboutTetris.html"))) {
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
