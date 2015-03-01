package tetris;

import tetris.tetriminos.Tetriminos;
import tetris.tetriminos.TetriminosFactory;

import java.io.*;

/**
 * This class encapsulates all the serializations of a game, so as to separate the serializations
 * codes from the game codes.
 *
 * @author Konstantin Garkusha
 */
public class Serialization implements Externalizable {
    static final long serialVersionUID = 1L;
    private static final TetriminosFactory TETRIMINOS_FACTORY = TetriminosFactory.getTetriminosInstance();
    private char[][] matrix;
    private int score;
    private Tetriminos falling;
    private Tetriminos next;

    public Serialization() {
    }

    public void saveGame(){
        String saveFile = "game.save";
        // create absolutePath to jar file (Ubuntu won't right work without it)
        String savePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        savePath = savePath.substring(0, savePath.lastIndexOf(File.separator));
        String save = savePath + File.separator+ saveFile;


        File file = new File(save);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            writeExternal(oos);
        } catch (IOException e) {
            e.printStackTrace();
            Tetris.errorFrame("Can't create save file!");
        }
    }

    public void loadGame(){
        String loadFile = "game.save";
        // create absolutePath to jar file (Ubuntu won't right work without it)
        String loadPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        loadPath = loadPath.substring(0, loadPath.lastIndexOf(File.separator));
        String load = loadPath + File.separator+ loadFile;

        File file = new File(load);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            readExternal(ois);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Tetris.errorFrame("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
            Tetris.errorFrame("Saved file is not valid");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Tetris.errorFrame("Saved file have an error");
        }

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        matrix = PlayThread.matrix;
        score = PlayThread.getScore();
        falling = PlayThread.getFalling();
        next = TETRIMINOS_FACTORY.getNextTetriminos();

        out.writeObject(falling);
        out.writeObject(next);
        out.writeObject(matrix);
        out.writeInt(score);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        falling = (Tetriminos)in.readObject();
        next = (Tetriminos)in.readObject();
        matrix = (char[][]) in.readObject();
        score = in.readInt();

        PlayThread.matrix = matrix;
        PlayThread.setScore(score);
        TETRIMINOS_FACTORY.addLoadedTetriminos(falling, next);
    }
}
