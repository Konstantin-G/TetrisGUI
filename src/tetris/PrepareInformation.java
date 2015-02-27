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

    private static String getAbsolutePath(){
        // create absolutePath to jar file (Ubuntu won't right work without it)
        String savePath = Tetris.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        savePath = savePath.substring(0, savePath.lastIndexOf("/"));
        return savePath + File.separator;
    }

    private static String readAboutTetris(){
        String load = getAbsolutePath() + "aboutTetris.html";
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
        String load = getAbsolutePath() + "help.html";
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
