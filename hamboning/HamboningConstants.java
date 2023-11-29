package hamboning;

import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class HamboningConstants {
    public static final Vec2d GW_SIZE = new Vec2d(4,4);
    public static final Vec2d MAP_SIZE = new Vec2d(20);
    public static final double TILE_AMT = 64;
    public static final Color NIN_BACKGROUND_COLOR = Color.LIGHTBLUE;
    public static final Vec2d MORD_POS = new Vec2d(10,18);
    public static final Vec2d MORD_SIZE = new Vec2d(0.6,0.6);
    public static final Color NIN_PLATFORM_COLOR = Color.GOLD;
    public static final double PLATFORM_MASS = 1;
    public static final double PLATFORM_REST = 0.2;

    public static final String SAVE_PATH = "hamboning/saves/save.xml";
    public static final String SFX_PATH_HAMBONING = "hamboning/assets/hamboning.wav";
    public static final Vec2d CART_POS = new Vec2d(12,18);
    public static final Vec2d CART_SIZE = new Vec2d(1,1);
}
