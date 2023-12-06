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
import engine.UILibrary.UIPolygon;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import hamboning.Components.CartMoveableComponent;
import hamboning.Components.CharacterInputComponent;
import hamboning.Components.TileCollisionComponent;
import javafx.scene.paint.Color;

public class HamboningWorld extends GameWorld {
    private PhysicsSystem p;
    private DecaySystem d;
    private GraphicsSystem g;
    private SpriteSystem s;
    private InputSystem i;
    private CollisionSystem c;
    private SoundSystem soundSystem;
    private boolean firstTick = true;
    private HamboningMapLoader mapLoader;

    private GameObject mordecai;
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

        //creating background
//        GameObject ninBackground = new GameObject(new Vec2d(0), HamboningConstants.GW_SIZE, this);
//
//        UIRectangle backgroundElement = new UIRectangle(new Vec2d(0), HamboningConstants.GW_SIZE, HamboningConstants.NIN_BACKGROUND_COLOR, getScreenSize());
//        GraphicsComponent backgroundGC = new GraphicsComponent(ninBackground.tc, getScreenSize(), backgroundElement);
//        SoundComponent backgroundSC = new SoundComponent(HamboningConstants.SFX_PATH_HAMBONING);
//        ninBackground.addComponent(backgroundGC);
//        ninBackground.addComponent(backgroundSC);
//        soundSystem.addObject(ninBackground);
//        g.addObjectToLayer(ninBackground, 0);
//        addObjects(ninBackground);


        mordecai = new GameObject(HamboningConstants.MORD_POS, HamboningConstants.MORD_SIZE, this);
        SpriteComponent mordSprite = new SpriteComponent(new Vec2d(80, 80), new Vec2d(30, 50), Color.MAROON,
            this.getScreenSize(), mordecai.tc, false);
        mordecai.addComponent(mordSprite);

//        SoundComponent backgroundSC = new SoundComponent(HamboningConstants.SFX_PATH_HAMBONING);
//        mordecai.addComponent(backgroundSC);
//        soundSystem.addObject(mordecai);

        MoveableComponent mc = new MoveableComponent(mordecai, 0.5);
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
        GraphicsComponent mordGC = new GraphicsComponent(mordecai.tc, getScreenSize(), mordSprite);
        mordecai.addComponent(mordGC);
        g.addObjectToLayer(mordecai, 1);

        this.addObjects(mordecai);

        cart = makeCart();
    }

    private GameObject makeCart() {
        GameObject cart = new GameObject(HamboningConstants.CART_POS, HamboningConstants.CART_SIZE, this);
        // Graphics
        SpriteComponent cartSprite = new SpriteComponent(new Vec2d(0), new Vec2d(100,90), Color.WHITE,
                this.getScreenSize(), cart.tc, false);
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
    }

}
