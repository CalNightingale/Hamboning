package hamboning;

import engine.Components.CollisionComponent;
import engine.Components.Component;
import engine.Components.ComponentRegistry;
import engine.Components.GraphicsComponent;
import engine.Components.InputComponent;
import engine.Components.MoveableComponent;
import engine.Components.PhysicsComponent;
import engine.Components.ProjectileComponent;
import engine.Components.SpriteComponent;
import engine.GameObject;
import engine.GameWorld;
import engine.SaveLoad;
import engine.Shapes.AAB;
import engine.Shapes.Circle;
import engine.Shapes.Polygon;
import engine.Systems.CollisionSystem;
import engine.Systems.GraphicsSystem;
import engine.Systems.InputSystem;
import engine.Systems.PathfindingSystem;
import engine.Systems.PhysicsSystem;
import engine.Systems.ProjectileSystem;
import engine.Systems.SpriteSystem;
import engine.Systems.TimerSystem;
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
    private boolean firstTick = true;



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

        //creating background
        GameObject ninBackground = new GameObject(new Vec2d(0), HamboningConstants.GW_SIZE, this);

        UIRectangle backgroundElement = new UIRectangle(new Vec2d(0), HamboningConstants.GW_SIZE, HamboningConstants.NIN_BACKGROUND_COLOR, getScreenSize());
        GraphicsComponent backgroundGC = new GraphicsComponent(ninBackground.tc, getScreenSize(), backgroundElement);
        ninBackground.addComponent(backgroundGC);
        g.addObjectToLayer(ninBackground, 0);
        addObjects(ninBackground);


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

/*
        GameObject ninObj3 = new GameObject(HamboningConstants.OBJ3_POS, HamboningConstants.OBJ3_SIZE, this);
        UIRectangle ninObj3Shape = new UIRectangle(HamboningConstants.OBJ3_POS, HamboningConstants.OBJ3_SIZE, HamboningConstants.OBJ3_COLOR, getScreenSize());

        //Circle ninObj2ColShape = new Circle(HamboningConstants.OBJ2_POS, HamboningConstants.OBJ2_SIZE, false);
        AAB ninObj3ColShape = new AAB(HamboningConstants.OBJ3_POS, HamboningConstants.OBJ3_SIZE, true);
        CollisionComponent ninObj3CC = new CollisionComponent(ninObj3ColShape, ninObj3);
        ninObj3.addComponent(ninObj3CC);
        c.addObjectToLayer(ninObj3, 1);


        PhysicsComponent ninObj3PC = new PhysicsComponent(HamboningConstants.OBJ3_MASS, HamboningConstants.OBJ3_REST, ninObj3.tc);
        ninObj3.addComponent(ninObj3PC);
        p.addPhysicsObject(ninObj3, HamboningConstants.GRAVITY, true);

        GraphicsComponent ninObj3GC = new GraphicsComponent(ninObj3.tc, getScreenSize(), ninObj3Shape);
        ninObj3.addComponent(ninObj3GC);
        g.addObjectToLayer(ninObj3, 1);
        addObjects(ninObj3);



        //create ninChar
        UIPolygon ninShape = new UIPolygon(HamboningConstants.NIN_CHAR_COLOR, getScreenSize(),
                HamboningConstants.NIN_CHAR_POINT_1, HamboningConstants.NIN_CHAR_POINT_2, HamboningConstants.NIN_CHAR_POINT_3);
        GameObject ninChar = new GameObject(ninShape.getPosition(), ninShape.getSize(), this);

        Polygon ninCharColl = new Polygon(false, HamboningConstants.NIN_CHAR_POINT_1, HamboningConstants.NIN_CHAR_POINT_2, HamboningConstants.NIN_CHAR_POINT_3);
        CollisionComponent ninCharCC = new CollisionComponent(ninCharColl, ninChar);
        ninChar.addComponent(ninCharCC);
        c.addObjectToLayer(ninChar, 1);

        MoveableComponent ninCharMC = new MoveableComponent(ninChar, 0.3);
        ninChar.addComponent(ninCharMC);

        NinCharInputComponent ninCharIC = new NinCharInputComponent(ninChar, this);
        ninChar.addComponent(ninCharIC);
        i.addObject(ninChar);

        ProjectileComponent ninCharProjC = new ProjectileComponent(ninChar, true);
        ninChar.addComponent(ninCharProjC);


        PhysicsComponent ninCharPC = new PhysicsComponent(HamboningConstants.CHAR_MASS, HamboningConstants.CHAR_REST, ninChar.tc);
        ninChar.addComponent(ninCharPC);
        p.addPhysicsObject(ninChar, HamboningConstants.GRAVITY, false);

        GraphicsComponent ninCharGC = new GraphicsComponent(ninChar.tc, getScreenSize(), ninShape);
        ninChar.addComponent(ninCharGC);
        g.addObjectToLayer(ninChar, 1);
        addObjects(ninChar);

        //create object 1
        GameObject ninObj1 = new GameObject(HamboningConstants.OBJ1_POS, HamboningConstants.OBJ1_SIZE, this);
        UICircle ninObj1Shape = new UICircle(HamboningConstants.OBJ1_POS, HamboningConstants.OBJ1_SIZE, HamboningConstants.OBJ1_COLOR, getScreenSize());
        //UIRectangle ninObj1Shape = new UIRectangle(HamboningConstants.OBJ1_POS, HamboningConstants.OBJ1_SIZE, HamboningConstants.OBJ1_COLOR, getScreenSize());

        Circle ninObj1ColShape = new Circle(HamboningConstants.OBJ1_POS, HamboningConstants.OBJ1_SIZE, false);
        //AAB ninObj1ColShape = new AAB(HamboningConstants.OBJ1_POS, HamboningConstants.OBJ1_SIZE, false);
        CollisionComponent ninObj1CC = new CollisionComponent(ninObj1ColShape, ninObj1);
        ninObj1.addComponent(ninObj1CC);



        PhysicsComponent ninObj1PC = new PhysicsComponent(HamboningConstants.OBJ1_MASS, HamboningConstants.OBJ1_REST, ninObj1.tc);
        ninObj1.addComponent(ninObj1PC);


        GraphicsComponent ninObj1GC = new GraphicsComponent(ninObj1.tc, getScreenSize(), ninObj1Shape);
        ninObj1.addComponent(ninObj1GC);




        //create object 2

        GameObject ninObj2 = new GameObject(HamboningConstants.OBJ2_POS, HamboningConstants.OBJ2_SIZE, this);
        //UICircle ninObj2Shape = new UICircle(HamboningConstants.OBJ2_POS, HamboningConstants.OBJ2_SIZE, HamboningConstants.OBJ2_COLOR, getScreenSize());
        UIRectangle ninObj2Shape = new UIRectangle(HamboningConstants.OBJ2_POS, HamboningConstants.OBJ2_SIZE, HamboningConstants.OBJ2_COLOR, getScreenSize());

        //Circle ninObj2ColShape = new Circle(HamboningConstants.OBJ2_POS, HamboningConstants.OBJ2_SIZE, false);
        AAB ninObj2ColShape = new AAB(HamboningConstants.OBJ2_POS, HamboningConstants.OBJ2_SIZE, false);
        CollisionComponent ninObj2CC = new CollisionComponent(ninObj2ColShape, ninObj2);
        ninObj2.addComponent(ninObj2CC);


        PhysicsComponent ninObj2PC = new PhysicsComponent(HamboningConstants.OBJ2_MASS, HamboningConstants.OBJ2_REST, ninObj2.tc);
        ninObj2.addComponent(ninObj2PC);


        GraphicsComponent ninObj2GC = new GraphicsComponent(ninObj2.tc, getScreenSize(), ninObj2Shape);
        ninObj2.addComponent(ninObj2GC);

        c.addObjectToLayer(ninObj1, 1);
        p.addPhysicsObject(ninObj1, HamboningConstants.GRAVITY, false);
        g.addObjectToLayer(ninObj1, 1);
        addObjects(ninObj1);


        c.addObjectToLayer(ninObj2, 1);
        p.addPhysicsObject(ninObj2, HamboningConstants.GRAVITY, false);
        g.addObjectToLayer(ninObj2, 1);
        addObjects(ninObj2);
*/



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

    }

}
