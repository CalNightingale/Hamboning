package engine.UILibrary;

import engine.support.Vec2d;
import javafx.scene.paint.Color;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import javax.sound.sampled.Clip;

public class UISound extends UIElement {
    private final String soundPath;
    protected Clip clip;
    private boolean loaded;

    public UISound(Vec2d position, Vec2d screenSize, String path) {
        super(position, new Vec2d(0), Color.MAROON, screenSize);
        this.soundPath = path;
        this.clip = null;
        this.loaded = false;
    }

    @Override
    public void onTick(long nanos) {
        if (!loaded) {
            play(false);
        }
    }

    public void play(boolean truncate) {
        if (!loaded) {
            load();
            loaded = true;
        }
        // if truncation enabled OR clip is no longer running, play clip from beginning
        if (truncate || !clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void load() {
        try {
            File file = new File(soundPath);
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            AudioInputStream stream = AudioSystem.getAudioInputStream(in);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file: " + soundPath);
            System.exit(1);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file type: " + soundPath);
            System.exit(2);
        } catch (IOException e) {
            System.out.println("IO Exception during loading of file: " + soundPath);
            System.exit(3);
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException occurred in soundfile: " + soundPath);
            System.exit(4);
        }
    }

}
