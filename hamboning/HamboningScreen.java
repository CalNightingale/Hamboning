package hamboning;


import engine.Application;
import engine.GameObject;
import engine.GameWorld;
import engine.SaveLoad;
import engine.Screen;
import engine.UILibrary.UIButton;
import engine.UILibrary.UIText;
import engine.Viewport;
import engine.support.Vec2d;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class HamboningScreen extends Screen {

    private HamboningWorld gw;
    private Viewport vp;
    private SaveLoad sl;
    private boolean firstTick = true;
    private Screen menuScreen;

    private ScoreElement scoreElement;


    public HamboningScreen(Application app, Vec2d screenSize){
        super(app, screenSize);

        this.sl = new SaveLoad();
        this.scoreElement = new ScoreElement(new Vec2d(85,25), new Vec2d(50),
                HamboningConstants.TITLE_FONT_PATH, Color.BLACK, getScreenSize());

        Vec2d vpSize = new Vec2d(screenSize.x, screenSize.y);
        // if want skinny vp, make vpsize y,y not x,y
        Vec2d vpPos =  new Vec2d((screenSize.x/2) - (vpSize.x/2),0);

        this.gw = new HamboningWorld(HamboningConstants.GW_SIZE, screenSize, scoreElement, true);
        this.vp = new Viewport(vpPos, vpSize, screenSize, gw);

        gw.givePort(this.vp); //would love to avoid this
        addElements(this.vp);

        // must be done after vp added to show up on top
        addElements(scoreElement);

        /*
        Vec2d savePos = new Vec2d(10);
        Vec2d saveSize = new Vec2d(60, 40);

        UIButton saveButton = new UIButton(savePos, saveSize, Color.PALETURQUOISE, screenSize, Color.DARKBLUE, "SAVE", new Vec2d(0));
        addElements(saveButton);


        Vec2d loadPos = new Vec2d(10).plus(new Vec2d(0,saveSize.y + 10 ));

        UIButton loadButton = new UIButton(loadPos, saveSize, Color.DARKBLUE, screenSize, Color.PALETURQUOISE,  "LOAD", new Vec2d(0));
        addElements(loadButton);
        loadButton.setClickAction(() -> {
            this.load(loadButton);
        });

        saveButton.setClickAction(() -> {
            this.save(saveButton);
        });*/
    }

    public void setMenuScreen(Screen menuScreen) {
        this.menuScreen = menuScreen;
    }

    public void save(UIButton saveButton) {
        try {
            sl.saveGameState(HamboningConstants.SAVE_PATH, this.gw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (saveButton.getTextColor() == Color.DARKBLUE){
            saveButton.setTextColor(Color.WHITE);
        } else {
            saveButton.setTextColor(Color.DARKBLUE);
        }
    }

    public void load(UIButton loadButton) {
        System.out.println("loading");
        Vec2d vpPos = this.vp.getPosition();
        Vec2d vpSize = this.vp.getSize();
        removeEl(this.vp);
        // TODO THIS LIKELY WILL NOT WORK WITH THE SCORE
        this.gw = new HamboningWorld(HamboningConstants.GW_SIZE, this.getScreenSize(), scoreElement, false);
        this.vp = new Viewport(vpPos, vpSize, this.getScreenSize(), this.gw);
        gw.givePort(this.vp);
        sl.load(HamboningConstants.SAVE_PATH, this.gw);
        addElements(this.vp);
        System.out.println("finished loading");

        // change button color
        if (loadButton.getColor() == Color.DARKBLUE){
            loadButton.setColor(Color.NAVY);
        } else {
            loadButton.setColor(Color.DARKBLUE);
        }
    }

    @Override
    public void onTick(long nanos){
        super.onTick(nanos);
        if (this.firstTick){
            this.firstTick = false;
            try {
                sl.saveGameState(HamboningConstants.SAVE_PATH, gw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            this.app.setActiveScreen(this.menuScreen);
        } else if (e.getCode() == KeyCode.M) {
            this.gw.toggleMute();
        }
        super.onKeyPressed(e);
    }
}
