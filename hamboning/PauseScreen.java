package hamboning;

import engine.Application;
import engine.Screen;
import engine.UILibrary.UIButton;
import engine.UILibrary.UIImage;
import engine.support.Vec2d;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PauseScreen extends Screen {
    private boolean firstTick;
    public PauseScreen(Application app, Vec2d screenSize) {
        super(app, screenSize);
        firstTick = true;
    }

    private void initializeScreen() {
        // background image
        Image backgroundFXImg = new Image(HamboningConstants.MENU_BACKGROUND_PATH);
        UIImage backgroundImage = new UIImage(new Vec2d(0), this.getScreenSize(),
                Color.MAROON, this.getScreenSize(), backgroundFXImg);
        addElements(backgroundImage);
        // buttons
        makeButtons(this.getScreenSize());
    }

    @Override
    public void onTick(long nanos) {
        if (this.firstTick) {
            initializeScreen();
            firstTick = false;
        }
    }

    private void makeButtons(Vec2d screenSize) {
        double buttonWidth = screenSize.x * HamboningConstants.SCREEN_TO_BUTTON_WIDTH_RATIO;
        double buttonTopBound = screenSize.y * HamboningConstants.BUTTON_TOPBOUND_SCREENHEIGHT_RATIO;
        double buttonOffset = screenSize.y * HamboningConstants.BUTTON_SPACING_SCREENHEIGHT_RATIO;
        int numButtons = 4;
        double totalButtonHeight = screenSize.y - buttonTopBound - buttonOffset*(numButtons-1);
        double buttonHeight = totalButtonHeight / numButtons;
        Vec2d buttonSize = new Vec2d(buttonWidth, buttonHeight);
        double buttonX = screenSize.x/2 - buttonWidth/2;
        // new game
        UIButton newGameButton = new UIButton(new Vec2d(buttonX, buttonTopBound), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "NEW GAME", new Vec2d(0));
        addElements(newGameButton);
        // save
        double saveY = buttonTopBound + buttonHeight + buttonOffset;
        UIButton saveButton = new UIButton(new Vec2d(buttonX, saveY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "SAVE", new Vec2d(0));
        addElements(saveButton);
        // load
        double loadY = buttonTopBound + (buttonHeight + buttonOffset) * 2;
        UIButton loadButton = new UIButton(new Vec2d(buttonX, loadY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "LOAD", new Vec2d(0));
        addElements(loadButton);
        // quit
        double quitY = buttonTopBound + (buttonHeight + buttonOffset) * 3;
        UIButton quitButton = new UIButton(new Vec2d(buttonX, quitY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "QUIT", new Vec2d(0));
        addElements(quitButton);
    }
}
