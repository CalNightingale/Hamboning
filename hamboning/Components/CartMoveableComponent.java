package hamboning.Components;

import engine.Components.DecayComponent;
import engine.Components.GraphicsComponent;
import engine.Components.MoveableComponent;
import engine.GameObject;
import engine.GameWorld;
import engine.Shapes.AAB;
import engine.Systems.DecaySystem;
import engine.Systems.GraphicsSystem;
import engine.Systems.SystemEnum;
import engine.UILibrary.UIElement;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import hamboning.HamboningConstants;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class CartMoveableComponent extends MoveableComponent {
    private final GameObject mordecai;
    private final GameObject rigby;
    public CartMoveableComponent(GameObject cart, double moveAmount, GameObject mordecai, GameObject rigby) {
        super(cart, moveAmount);
        this.mordecai = mordecai;
        this.rigby = rigby;
    }

    public CartMoveableComponent(Element data, GameObject thisO, GameWorld gw){
        super(thisO, 0);
        // TODO FIX THIS IT FOR SURE WONT WORK
        this.mordecai = null;
        this.rigby = null;
    }

    public Vec2d getMordPosInCart() {
        return this.o.tc.getPosition();
    }

    public Vec2d getRigbyPosInCart() {
        return new Vec2d(this.o.tc.getPosition().x + this.o.tc.getSize().x/2, this.o.tc.getPosition().y);
    }

    @Override
    public void onTick(long nanos) {
        super.onTick(nanos);
        // if moving, set mordecai position to cart position
        if (this.isMoving()) {
            mordecai.tc.setPos(getMordPosInCart());
            rigby.tc.setPos(getRigbyPosInCart());
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
