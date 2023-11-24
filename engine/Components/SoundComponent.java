package engine.Components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

import javax.sound.sampled.*;
import java.io.*;

public class SoundComponent implements Component<SoundComponent>{
    private final String filePath;
    private Clip clip;
    private boolean loaded;
    public SoundComponent(Element data, TransformComponent tc, Vec2d ss) {
        filePath = data.getAttribute("filePath");
        clip = null;
        loaded = false;
    }

    // Direct from sound lecture slide 16
    public void load() {
        try {
            File file = new File(filePath);
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            AudioInputStream stream = AudioSystem.getAudioInputStream(in);
            clip = AudioSystem.getClip();
            clip.open(stream);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file: " + filePath);
            System.exit(1);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file type: " + filePath);
        } catch (IOException e) {
            System.out.println("IO Exception during loading of file: " + filePath);
            System.exit(2);
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException occurred in soundfile: " + filePath);
            System.exit(3);
        }
    }
    @Override
    public void onTick(long nanos) {
        if (!loaded) {
            load();
            loaded = true;
        }
        clip.start();
    }

    @Override
    public void onDraw(GraphicsContext g) {

    }

    @Override
    public void onLateTick() {

    }

    @Override
    public Element serialize(Element el) {
        return null;
    }

    @Override
    public CompEnum getTag() {
        return CompEnum.Sound;
    }
}
