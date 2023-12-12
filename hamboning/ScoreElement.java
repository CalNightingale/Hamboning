package hamboning;

import engine.UILibrary.UIText;
import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class ScoreElement extends UIText {
    private int score;
    public ScoreElement(Vec2d position, Vec2d size, String fontNameOrPath, Color color, Vec2d screenSize) {
        super(position, size, "Score: X", fontNameOrPath, color, screenSize);
        this.setScore(0);
    }

    public void addToScore(int amtToAdd) {
        setScore(score + amtToAdd);
    }

    public void subtractFromScore(int amtToSubtract) {
        this.addToScore(-amtToSubtract);
    }

    public void setScore(int newScore) {
        score = newScore;
        this.setText("Score: " + score);
    }
}
