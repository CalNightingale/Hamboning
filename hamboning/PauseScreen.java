package hamboning;

import engine.Application;
import engine.Screen;
import engine.UILibrary.UIButton;
import engine.UILibrary.UIImage;
import engine.UILibrary.UIText;
import engine.support.Vec2d;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PauseScreen extends Screen {
    private boolean firstTick;
    private final HamboningScreen hs;
    private final Application app;

    public PauseScreen(Application app, Vec2d screenSize, HamboningScreen hs) {
        super(app, screenSize);
        firstTick = true;
        this.hs = hs;
        this.app = app;
    }

    private void initializeScreen() {
        // background image
        Image backgroundFXImg = new Image(HamboningConstants.MENU_BACKGROUND_PATH);
        UIImage backgroundImage = new UIImage(new Vec2d(0), this.getScreenSize(),
                Color.MAROON, this.getScreenSize(), backgroundFXImg);
        addElements(backgroundImage);
        // buttons
        makeButtons(this.getScreenSize());
        // text
        Vec2d titleLoc = new Vec2d(this.getScreenSize().x/2, 75);
        UIText title = new UIText(titleLoc, new Vec2d(100), "Hamboning: The Game",
                HamboningConstants.TITLE_FONT_PATH, Color.WHITE, getScreenSize());
        addElements(title);
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
        newGameButton.setClickAction(() -> this.app.setActiveScreen(this.hs));
        // save
        double saveY = buttonTopBound + buttonHeight + buttonOffset;
        UIButton saveButton = new UIButton(new Vec2d(buttonX, saveY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "SAVE", new Vec2d(0));
        addElements(saveButton);
        saveButton.setClickAction(() -> {
            this.hs.save(saveButton);
            this.app.setActiveScreen(this.hs);
        });
        // load
        double loadY = buttonTopBound + (buttonHeight + buttonOffset) * 2;
        UIButton loadButton = new UIButton(new Vec2d(buttonX, loadY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "LOAD", new Vec2d(0));
        addElements(loadButton);
        loadButton.setClickAction(() -> {
            this.hs.load(loadButton);
            this.app.setActiveScreen(this.hs);
        });
        // quit
        double quitY = buttonTopBound + (buttonHeight + buttonOffset) * 3;
        UIButton quitButton = new UIButton(new Vec2d(buttonX, quitY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_TEXT_COLOR,
                "QUIT", new Vec2d(0));
        addElements(quitButton);
        quitButton.setClickAction(this.app::quit);
    }
}
