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

public class HamboningWorld extends GameWorld {
    private PhysicsSystem p;
    private GraphicsSystem g;
    private SpriteSystem s;
    private InputSystem i;
    private CollisionSystem c;
    private SoundSystem soundSystem;
    private boolean firstTick = true;
    private HamboningMapLoader mapLoader;



    public HamboningWorld(Vec2d gwSize, Vec2d screenSize, boolean firstTick){
        super(gwSize, screenSize, firstTick);

        this.firstTick = firstTick;
        //this.cr.registerComponentFactory("NinCharInputComponent", (data, gameObject, gameWorld) -> new NinCharInputComponent(data, gameObject, gameWorld));
        setupSystems();
    }

    @Override
    public void onTick(long nanos){
        if (firstTick){
            firstTick = false;
            setLevel();
        }
        super.onTick(nanos);

        for (GameObject o: oList){
            if (o.tc.getPosition().y > 3){
                addToRemovals(o);
                System.out.println("removing fallen object");
            }
        }

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


        //creating platform
        GameObject ninPlat = new GameObject(HamboningConstants.PLATFORM_POS, HamboningConstants.PLATFORM_SIZE, this);

        UIRectangle ninPlatEl = new UIRectangle(ninPlat.tc.getPosition(), ninPlat.tc.getSize(), HamboningConstants.NIN_PLATFORM_COLOR, getScreenSize());

        AAB ninPlatColl = new AAB(ninPlat.tc.getPosition(), ninPlat.tc.getSize(), true);
        CollisionComponent ninPlatCC = new CollisionComponent(ninPlatColl, ninPlat);
        ninPlat.addComponent(ninPlatCC);
        c.addObjectToLayer(ninPlat, 1);

        PhysicsComponent ninPlatPC = new PhysicsComponent(HamboningConstants.PLATFORM_MASS, HamboningConstants.PLATFORM_REST, ninPlat.tc);
        ninPlat.addComponent(ninPlatPC);
        p.addPhysicsObject(ninPlat, 0, true);


        GraphicsComponent ninPlatGC = new GraphicsComponent(ninPlat.tc, getScreenSize(), ninPlatEl);
        ninPlat.addComponent(ninPlatGC);
        g.addObjectToLayer(ninPlat, 1);
        addObjects(ninPlat);


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
    }

}
