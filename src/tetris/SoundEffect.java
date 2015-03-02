package tetris;

/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * Define all your sound effect names and the associated wave file.
 * To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * You might optionally invoke the static method SoundEffect.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * You can use the static variable SoundEffect.volume to mute the sound.
 *
 * @author Konstantin Garkusha
 */
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum SoundEffect {
    DOWN("down.wav"),                           // Tetriminos is down
    ROW_COMPLETE("rowComplete.wav"),            // Then delete row
    GAME_OVER("gameOver.wav");                   // game over
    private static float volume = 0.0f;       // from -30.0f to +6.0f

    public static void setVolume(int value) {
        volume = value;
    }

    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;

    // Constructor to construct each element of the enum with its own sound file.
    SoundEffect(String soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            URL url = Tetris.class.getClassLoader().getResource("sound/" + soundFileName);
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play or Re-play the sound effect from the beginning, by rewinding.
    public void play() {
        if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
        clip.setFramePosition(0); // rewind to the beginning
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume); // Reduce volume by XX decibels.
        clip.start();     // Start playing
    }

    // Optional static method to pre-load all the sound files.
    static void init() {
        values(); // calls the constructor for all the elements
    }
}