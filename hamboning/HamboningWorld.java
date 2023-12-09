package hamboning;

import engine.Components.*;
import engine.GameObject;
import engine.GameWorld;
import engine.SaveLoad;
import engine.Shapes.AAB;
import engine.Shapes.Circle;
import engine.Shapes.Polygon;
import engine.Systems.*;
import engine.UILibrary.UICircle;
import engine.UILibrary.UIElement;
import engine.UILibrary.UIPolygon;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import hamboning.Components.*;
import javafx.scene.paint.Color;

public class HamboningWorld extends GameWorld {
    private PhysicsSystem p;
    private DecaySystem d;
    private GraphicsSystem g;
    private SpriteSystem s;
    private InputSystem i;
    private CollisionSystem c;
    private TimerSystem t;
    private SoundSystem soundSystem;
    private boolean firstTick = true;
    private HamboningMapLoader mapLoader;

    private GameObject mordecai;
    private GameObject rigby;
    private GameObject cart;



    public HamboningWorld(Vec2d gwSize, Vec2d screenSize, boolean firstTick){
        super(gwSize, screenSize, firstTick);

        this.firstTick = firstTick;
        this.cr.registerComponentFactory("CartMoveableComponent", (data, gameObject, gameWorld) -> new CartMoveableComponent(data, gameObject, gameWorld));
        this.cr.registerComponentFactory("CharacterInputComponent", CharacterInputComponent::new);
        this.cr.registerComponentFactory("TileCollisionComponent", TileCollisionComponent::new);
        //this.cr.registerComponentFactory("NinCharInputComponent"), (data, gameObject, gameWorld) -> new NinCharInputComponent(data, gameObject, gameWorld);
        setupSystems();
    }

    @Override
    public void onTick(long nanos){
        if (firstTick){
            firstTick = false;
            setLevel();
        }
        super.onTick(nanos);

    }

    public void setLevel(){

        mapLoader = new HamboningMapLoader(this.s, this.g, this.c, this.i, HamboningConstants.MAP_SIZE, this, 123456);
        mordecai = makeMordecai();

        // make rigby
        rigby = new GameObject(mordecai.tc.getPosition().plus(0,1), HamboningConstants.MORD_SIZE, this);

        // graphics
        UIElement rigbySprite = new UIRectangle(rigby.tc.getPosition(), rigby.tc.getSize(), Color.BROWN, getScreenSize());
        GraphicsComponent rigbyGC = new GraphicsComponent(rigby.tc, new Vec2d(0), rigbySprite);
        rigby.addComponent(rigbyGC);
        g.addObjectToLayer(rigby, 1);

        // move
        RigbyMoveComponent rigbyMC = new RigbyMoveComponent(rigby, HamboningConstants.MORD_SPEED, mordecai);
        rigby.addComponent(rigbyMC);
        t.addObject(rigby);

        cart = makeCart();
    }

    private GameObject makeMordecai() {
        mordecai = new GameObject(HamboningConstants.MORD_POS, HamboningConstants.MORD_SIZE, this);

        MordecaiAnimationComponent mordSprite2 = new MordecaiAnimationComponent(new Vec2d(0,130), new Vec2d(64,64), Color.MAROON,
                this.getScreenSize(), mordecai.tc, true, 1, 3, 0.5);
        mordecai.addComponent(mordSprite2);

        MoveableComponent mc = new MoveableComponent(mordecai, HamboningConstants.MORD_SPEED);
        mordecai.addComponent(mc);

        CharacterInputComponent mordecaiIC = new CharacterInputComponent(mordecai, this, this::getInCart);
        mordecai.addComponent(mordecaiIC);
        i.addObject(mordecai);

        CenterComponent mordCC = new CenterComponent(this.vp, mordecai);
        mordecai.addComponent(mordCC);

        AAB mordAAB = new AAB(mordecai.tc.getPosition(), mordecai.tc.getSize(), false, "mordecai");
        TileCollisionComponent mordColC = new TileCollisionComponent(mordAAB, mordecai);
        mordecai.addComponent(mordColC);
        c.addObjectToLayer(mordecai, 1);

        s.addAndLoad(mordecai, "hamboning/assets/CharacterAssets/mordecai.png");
        GraphicsComponent mordGC = new GraphicsComponent(mordecai.tc, getScreenSize(), mordSprite2);
        mordecai.addComponent(mordGC);
        g.addObjectToLayer(mordecai, 1);

        this.addObjects(mordecai);

        return mordecai;
    }

    private GameObject makeCart() {
        GameObject cart = new GameObject(HamboningConstants.CART_POS, HamboningConstants.CART_SIZE, this);
        // Graphics
        AnimationComponent cartSprite = new AnimationComponent(new Vec2d(0), new Vec2d(100, 90), Color.MAROON,
            this.getScreenSize(), cart.tc, false, 1, 1, 0.5);
        cart.addComponent(cartSprite);
        s.addAndLoad(cart, "hamboning/assets/MapAssets/hamboning-golfcart.png");
        GraphicsComponent cartGC = new GraphicsComponent(cart.tc, getScreenSize(), cartSprite);
        cart.addComponent(cartGC);
        g.addObjectToLayer(cart, 1);

        // Movement
        MoveableComponent cartMC = new CartMoveableComponent(cart, 1, mordecai);
        cart.addComponent(cartMC);
        CharacterInputComponent cartIC = new CharacterInputComponent(cart, this, this::getOutOfCart);
        cart.addComponent(cartIC);

        this.addObjects(cart);
        return cart;
    }

    private void getInCart() {
        // Determine whether character is close enough to enter cart
        double dist = mordecai.tc.getCenter().dist(cart.tc.getCenter());
        if (dist > HamboningConstants.ENTER_CART_MAX_DIST) return;
        // stop mordecai movement
        MoveableComponent mc = mordecai.getComponent(CompEnum.Moveable);
        mc.setStop();
        // deregister mordecai from input listening
        i.removeObject(mordecai);
        // move mordecai into center of cart
        mordecai.tc.setPos(cart.tc.getPosition());
        // register cart for input listening
        i.addObject(cart);
    }

    private void getOutOfCart() {
        // deregister cart from input listening
        i.removeObject(cart);
        // register mordecai for input listening
        i.addObject(mordecai);
    }

    private void setupSystems(){
        clearSystems();
        this.p = new PhysicsSystem();
        addSystem(p);
        this.g = new GraphicsSystem();
        addSystem(g);
        this.s = new SpriteSystem();
        addSystem(s);
        this.c = new CollisionSystem();
        addSystem(c);
        this.i = new InputSystem();
        addSystem(i);
        this.soundSystem = new SoundSystem();
        addSystem(soundSystem);
        this.d = new DecaySystem();
        addSystem(d);
        this.t = new TimerSystem();
        addSystem(t);
    }

}
