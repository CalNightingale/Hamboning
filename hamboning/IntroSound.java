package hamboning;

import engine.UILibrary.UISound;
import engine.support.Vec2d;

import java.util.Arrays;

public class IntroSound extends UISound {
    private final double[] beatDrops;
    private final double dropThreshold = 0.03;
    private final double minTimeBetweenDrops = 0.2;
    private double lastDrop;
    public IntroSound(Vec2d position, Vec2d screenSize, String path) {
        super(position, screenSize, path);
        beatDrops = new double[] {0.42, 2.76, 5.00, 7.02, 8.62, 10.2};
        lastDrop = -1;
    }

    private boolean detectBeatDrop(double curPos) {
        for (double dropTime : beatDrops) {
            if (curPos - lastDrop > minTimeBetweenDrops &&
                    Math.abs(curPos - dropTime) < dropThreshold) {
                lastDrop = curPos;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTick(long nanos) {
        super.onTick(nanos);
        double curPos = this.clip.getMicrosecondPosition() / 1e6;
        if (detectBeatDrop(curPos)) System.out.println("BEAT DROP");
    }

}
