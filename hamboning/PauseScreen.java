package hamboning;

import engine.Application;
import engine.Screen;
import engine.UILibrary.UIButton;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class PauseScreen extends Screen {
    public PauseScreen(Application app, Vec2d screenSize) {
        super(app, screenSize);
        Vec2d savePos = screenSize.smult(0.5);
        Vec2d saveSize = screenSize.smult(0.1);
        UIButton saveButton = new UIButton(savePos, saveSize, Color.PALETURQUOISE, screenSize, Color.DARKBLUE, "SAVE", new Vec2d(0));
        addElements(saveButton);
    }
}
