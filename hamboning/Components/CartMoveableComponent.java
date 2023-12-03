package hamboning.Components;

import engine.Components.GraphicsComponent;
import engine.Components.MoveableComponent;
import engine.GameObject;
import engine.Shapes.AAB;
import engine.Systems.GraphicsSystem;
import engine.Systems.SystemEnum;
import engine.UILibrary.UIElement;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import hamboning.HamboningConstants;
import javafx.scene.paint.Color;

public class CartMoveableComponent extends MoveableComponent {
    private final GameObject character;
    public CartMoveableComponent(GameObject cart, double moveAmount, GameObject character) {
        super(cart, moveAmount);
        this.character = character;
    }

    @Override
    public void onTick(long nanos) {
        super.onTick(nanos);
        // if moving, set character position to cart position
        if (this.isMoving()) {
            character.tc.setPos(this.o.tc.getPosition());
        }
    }

    private void makeTrack(Vec2d trackPos) {
        GameObject track1 = new GameObject(trackPos, HamboningConstants.TRACK_SIZE, this.o.getGW());
        UIElement trackElement = new UIRectangle(trackPos, HamboningConstants.TRACK_SIZE, Color.BROWN, this.o.getGW().getScreenSize());
        GraphicsComponent trackGC = new GraphicsComponent(track1.getTransform(), new Vec2d(0), trackElement);
        track1.addComponent(trackGC);
        GraphicsSystem g = this.o.getGW().getSystem(SystemEnum.Graphics);
        g.addObjectToLayer(track1,1);
    }

    @Override
    public void moveUp(double amt) {
        // do actual movement
        super.moveUp(amt);
        // create track
        double bottomY = this.o.tc.getPosition().y + this.o.tc.getSize().y;
        double x1 = this.o.tc.getPosition().x;
        double x2 = this.o.tc.getPosition().x + this.o.tc.getSize().x - HamboningConstants.TRACK_SIZE.x;
        Vec2d track1Pos = new Vec2d(x1, bottomY);
        Vec2d track2Pos = new Vec2d(x2, bottomY);
        makeTrack(track1Pos);
        makeTrack(track2Pos);
    }
}
