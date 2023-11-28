package hamboning;


import engine.Application;
import engine.GameObject;
import engine.GameWorld;
import engine.SaveLoad;
import engine.Screen;
import engine.UILibrary.UIButton;
import engine.Viewport;
import engine.support.Vec2d;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.scene.paint.Color;


public class HamboningScreen extends Screen {

    private GameWorld gw;
    private Viewport vp;
    private SaveLoad sl;
    private boolean firstTick = true;
    public HamboningScreen(Application app, Vec2d screenSize){
        super(app, screenSize);
        app.setActiveScreen(this);


        Vec2d ninPortSize = new Vec2d(screenSize.y, screenSize.y);
        Vec2d ninPortPos =  new Vec2d((screenSize.x/2) - (ninPortSize.x/2),0);

        Vec2d gameWorldSize = HamboningConstants.GW_SIZE;
        this.gw = new HamboningWorld(gameWorldSize, screenSize, true);
        this.vp = new Viewport(ninPortPos, ninPortSize, screenSize, gw);

        gw.givePort(this.vp); //would love to avoid this
        addElements(this.vp);

        //Vec2d pos, Vec2d siz, Color butCol, Vec2d screenSize, Color textCol, String text, Vec2d rounding
        Vec2d savePos = new Vec2d(10);
        Vec2d saveSize = new Vec2d(60, 40);

        UIButton saveButton = new UIButton(savePos, saveSize, Color.PALETURQUOISE, screenSize, Color.DARKBLUE, "SAVE", new Vec2d(0));
        addElements(saveButton);


        Vec2d loadPos = new Vec2d(10).plus(new Vec2d(0,saveSize.y + 10 ));

        UIButton loadButton = new UIButton(loadPos, saveSize, Color.DARKBLUE, screenSize, Color.PALETURQUOISE,  "LOAD", new Vec2d(0));
        addElements(loadButton);
        loadButton.setClickAction(() -> {
            System.out.println("loading");
            removeEl(this.vp);
            this.gw = new HamboningWorld(gameWorldSize, screenSize, false);
            this.vp = new Viewport(ninPortPos, ninPortSize, screenSize, this.gw);
            gw.givePort(this.vp);
            sl.load("Nin/MapFiles/NinSaveFile.xml", this.gw);
            addElements(this.vp);
            System.out.println("finished loading");

            if (loadButton.getColor() == Color.DARKBLUE){
                loadButton.setColor(Color.NAVY);
            } else {
                loadButton.setColor(Color.DARKBLUE);
            }





        });

        saveButton.setClickAction(() -> {
            try {
                sl.saveGameState("Nin/MapFiles/NinSaveFile.xml", this.gw);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (saveButton.getTextColor() == Color.DARKBLUE){
                saveButton.setTextColor(Color.WHITE);
            } else {
                saveButton.setTextColor(Color.DARKBLUE);
            }

        });




    }

    @Override
    public void onTick(long nanos){
        super.onTick(nanos);

        if (this.firstTick){
            this.firstTick = false;

            this.sl = new SaveLoad();

            try {
                sl.saveGameState("Nin/MapFiles/NinSaveFile.xml", gw);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


}
