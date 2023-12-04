package hamboning.Components;

import engine.Components.DecayComponent;
import engine.Components.GraphicsComponent;
import engine.Components.MoveableComponent;
import engine.GameObject;
import engine.Shapes.AAB;
import engine.Systems.DecaySystem;
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

    private void spawnTrack(Vec2d trackPos) {
        GameObject track = new GameObject(trackPos, HamboningConstants.TRACK_SIZE, this.o.getGW());
        // graphics
        UIElement trackElement = new UIRectangle(trackPos, HamboningConstants.TRACK_SIZE, Color.BROWN, this.o.getGW().getScreenSize());
        GraphicsComponent trackGC = new GraphicsComponent(track.getTransform(), new Vec2d(0), trackElement);
        track.addComponent(trackGC);
        GraphicsSystem g = this.o.getGW().getSystem(SystemEnum.Graphics);
        g.addObjectToLayer(track,1);
        // decay
        DecayComponent trackD = new DecayComponent(HamboningConstants.TRACK_DECAY_TIME, track, this.o.getGW());
        track.addComponent(trackD);
        DecaySystem d = this.o.getGW().getSystem(SystemEnum.Decay);
        d.addObject(track);
    }

    private void makeTracks() {
        // TODO rework once cart sprite implemented
        double bottomY = this.o.tc.getPosition().y + this.o.tc.getSize().y;
        double x1 = this.o.tc.getPosition().x;
        double x2 = this.o.tc.getPosition().x + this.o.tc.getSize().x - HamboningConstants.TRACK_SIZE.x;
        Vec2d track1Pos = new Vec2d(x1, bottomY);
        Vec2d track2Pos = new Vec2d(x2, bottomY);
        spawnTrack(track1Pos);
        spawnTrack(track2Pos);
    }

    @Override
    public void moveUp(double amt) {
        // do actual movement
        super.moveUp(amt);
        // create tracks
        makeTracks();
    }

    @Override
    public void moveDown(double amt) {
        // do actual movement
        super.moveDown(amt);
        // create tracks
        makeTracks();
    }

    @Override
    public void moveLeft(double amt) {
        // do actual movement
        super.moveLeft(amt);
        // create tracks
        makeTracks();
    }

    @Override
    public void moveRight(double amt) {
        // do actual movement
        super.moveRight(amt);
        // create tracks
        makeTracks();
    }

}
