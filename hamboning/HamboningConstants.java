package hamboning;

import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class HamboningConstants {
    public static final Vec2d GW_SIZE = new Vec2d(4,4);
    public static final Vec2d MAP_SIZE = new Vec2d(20);
    public static final double TILE_AMT = 64;
    public static final String SAVE_PATH = "hamboning/saves/save.xml";
    public static final String SFX_PATH_HAMBONING = "hamboning/assets/hamboning.wav";
    public static final Vec2d CART_POS = new Vec2d(12,18);
    public static final Vec2d CART_SIZE = new Vec2d(1,1);
    public static final double ENTER_CART_MAX_DIST = 0.75;
    public static final Vec2d TRACK_SIZE = new Vec2d(0.1,0.1);
    public static final double TRACK_DECAY_TIME = 0.5;

    // MENU BUTTON STUFF
    public static final Color BUTTON_COLOR = Color.TRANSPARENT;
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    public static final double SCREEN_TO_BUTTON_WIDTH_RATIO = 0.5;
    public static final double BUTTON_TOPBOUND_SCREENHEIGHT_RATIO = 0.3;
    public static final double BUTTON_SPACING_SCREENHEIGHT_RATIO = 0.05;
    public static final String MENU_BACKGROUND_PATH = "hamboning/assets/title_background.jpeg";
    public static final String TITLE_FONT_PATH = "hamboning/assets/IceCreamPartySolid.ttf";
    public static final String BUTTON_FONT_PATH = "hamboning/assets/IceCreamParty.ttf";
    public static final int BUTTON_FONT_SIZE = 50;
    public static final String TITLE_MUSIC_PATH = "hamboning/assets/introMusic.wav";

    // MORDECAI AND RIGBY
    public static final Vec2d MORD_POS = new Vec2d(10,18);
    public static final Vec2d MORD_SIZE = new Vec2d(0.6,0.6);
    public static final double MORD_SPEED = 0.5;
}
