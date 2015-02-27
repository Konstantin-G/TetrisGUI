package tetris;

import java.io.*;

/**
 * Created by Konstantin Garkusha on 25.2.2015.
 *
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

    private static String getPath(){
        // create full path to file (Ubuntu won't right work without it)
        // get jar file path + filename
        String path =  Tetris.class.getProtectionDomain().getCodeSource().getLocation().toString();
        // cut "file:" and replace "%20" with " "
        path = path.substring(6).replaceAll("%20", " ");
        path = path.substring(0, path.lastIndexOf('/') + 1);
        return path;
    }

    private static String readAboutTetris(){
        String load = getPath() + "aboutTetris.html";
        String result ="";
        String line;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(load))) {
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
        String load = getPath() + "help.html";
        String result ="";
        String line;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(load))) {
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
