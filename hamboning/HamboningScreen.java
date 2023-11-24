package hamboning;

import engine.GameWorld;
import engine.Screen;
import engine.Application;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class HamboningScreen extends Screen {
    public HamboningScreen(Application app, Vec2d screenSize){
        super(app, screenSize);
        UIRectangle testRect = new UIRectangle(new Vec2d(screenSize.x / 2, screenSize.y / 2), new Vec2d(40),
                Color.LAVENDER, screenSize);

        addElements(testRect);
    }
}
