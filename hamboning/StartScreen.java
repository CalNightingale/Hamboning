package hamboning;

import engine.Application;
import engine.Screen;
import engine.UILibrary.*;
import engine.support.Vec2d;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class StartScreen extends Screen {
    private boolean firstTick;
    private final HamboningScreen hs;
    private final Application app;
    private IntroSound introSound;
    private int curBeatDrop;
    private UIText title;
    private UIText createdByText;
    private UIElement buttonContainer;

    public StartScreen(Application app, Vec2d screenSize, HamboningScreen hs) {
        super(app, screenSize);
        firstTick = true;
        this.hs = hs;
        this.app = app;
        curBeatDrop = 0;
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
        title = new UIText(titleLoc, new Vec2d(100), "Hamboning: The Game",
                HamboningConstants.TITLE_FONT_PATH, Color.WHITE, getScreenSize());
        addElements(title);
        title.setVisible(false);

        createdByText = new UIText(getScreenSize().smult(0.5), new Vec2d(40),
                "Created by Cal Nightingale and Elliott Rosenberg", HamboningConstants.BUTTON_FONT_PATH,
                Color.WHITE, getScreenSize());
        addElements(createdByText);
        createdByText.setVisible(false);

        // sound
        this.introSound = new IntroSound(getScreenSize().smult(0.5), getScreenSize(),
                HamboningConstants.TITLE_MUSIC_PATH);
        addElements(introSound);
    }

    @Override
    public void onTick(long nanos) {
        super.onTick(nanos);
        if (this.firstTick) {
            initializeScreen();
            firstTick = false;
        }
        if (!(this.introSound.clip == null) && this.introSound.detectBeatDrop()) {
            curBeatDrop++;
            updateDisplayOnBeatDrop();
        }
    }

    private void updateDisplayOnBeatDrop() {
        switch (curBeatDrop) {
            case 1:
                title.setVisible(true);
                break;
            case 2:
                title.setVisible(false);
                createdByText.setVisible(true);
                break;
            case 3:
                createdByText.setText("Based with love off The Regular Show");
                break;
            case 4:
                createdByText.setText("Final Project; 2D Game Engines; Brown University");
                break;
            case 5:
                createdByText.setText("Produced by Jacques Nissen");
                break;
            case 6:
                createdByText.setVisible(false);
                title.setVisible(true);
                buttonContainer.setVisible(true);
                break;
        }
    }

    private void makeButtons(Vec2d screenSize) {
        buttonContainer = new UIElement(new Vec2d(0), getScreenSize(), getScreenSize());

        double buttonWidth = screenSize.x * HamboningConstants.SCREEN_TO_BUTTON_WIDTH_RATIO;
        double buttonTopBound = screenSize.y * HamboningConstants.BUTTON_TOPBOUND_SCREENHEIGHT_RATIO;
        double buttonOffset = screenSize.y * HamboningConstants.BUTTON_SPACING_SCREENHEIGHT_RATIO;
        int numButtons = 2;
        double totalButtonHeight = screenSize.y - buttonTopBound - buttonOffset*(numButtons-1);
        double buttonHeight = totalButtonHeight / numButtons;
        Vec2d buttonSize = new Vec2d(buttonWidth, buttonHeight);
        double buttonX = screenSize.x/2 - buttonWidth/2;
        // new game
        UIButton newGameButton = new UIButton(new Vec2d(buttonX, buttonTopBound), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_FONT_PATH,
                HamboningConstants.BUTTON_FONT_SIZE, HamboningConstants.BUTTON_TEXT_COLOR,
                "NEW GAME", new Vec2d(0));
        buttonContainer.addChild(newGameButton);

        newGameButton.setClickAction(() -> this.app.setActiveScreen(this.hs));
        // save
        /*
        double saveY = buttonTopBound + buttonHeight + buttonOffset;
        UIButton saveButton = new UIButton(new Vec2d(buttonX, saveY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_FONT_PATH,
                HamboningConstants.BUTTON_FONT_SIZE, HamboningConstants.BUTTON_TEXT_COLOR,
                "SAVE", new Vec2d(0));
        buttonContainer.addChild(saveButton);
        saveButton.setClickAction(() -> {
            this.hs.save(saveButton);
            this.app.setActiveScreen(this.hs);
        });
        // load
        double loadY = buttonTopBound + (buttonHeight + buttonOffset) * 2;
        UIButton loadButton = new UIButton(new Vec2d(buttonX, loadY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_FONT_PATH,
                HamboningConstants.BUTTON_FONT_SIZE, HamboningConstants.BUTTON_TEXT_COLOR,
                "LOAD", new Vec2d(0));
        buttonContainer.addChild(loadButton);
        loadButton.setClickAction(() -> {
            this.hs.load(loadButton);
            this.app.setActiveScreen(this.hs);
        });*/
        // quit
        //double quitY = buttonTopBound + (buttonHeight + buttonOffset) * 3;
        double quitY = buttonTopBound + (buttonHeight + buttonOffset) * 1;
        UIButton quitButton = new UIButton(new Vec2d(buttonX, quitY), buttonSize,
                HamboningConstants.BUTTON_COLOR, screenSize, HamboningConstants.BUTTON_FONT_PATH,
                HamboningConstants.BUTTON_FONT_SIZE, HamboningConstants.BUTTON_TEXT_COLOR,
                "QUIT", new Vec2d(0));
        buttonContainer.addChild(quitButton);
        quitButton.setClickAction(this.app::quit);
        addElements(buttonContainer);
        buttonContainer.setVisible(false);
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        // if escape is pressed and the intro is playing, cut to end
        if (e.getCode() == KeyCode.ESCAPE &&
            introSound.clip.isRunning()) {
            introSound.clip.stop();
            createdByText.setVisible(false);
            title.setVisible(true);
            buttonContainer.setVisible(true);
        } else if (e.getCode() == KeyCode.M) {
            // pass muting command to hs so it can toggle the music
            this.hs.onKeyPressed(e);
        }
    }
}
