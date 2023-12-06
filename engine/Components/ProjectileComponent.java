package engine.Components;

import engine.GameObject;
import engine.GameWorld;
import engine.Projectile;
import engine.Shapes.AAB;
import engine.Shapes.Collision;
import engine.Shapes.Ray;
import engine.Shapes.RaycastResult;
import engine.Systems.CollisionSystem;
import engine.Systems.GraphicsSystem;
import engine.Systems.ProjectileSystem;
import engine.Systems.SpriteSystem;
import engine.Systems.SystemEnum;
import engine.support.Vec2d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class ProjectileComponent implements Component<ProjectileComponent>{
  public String filePath = "s";
  public GameObject o;
  public double projSize = 0;
  public double projSpeed = 0;


  public ProjectileComponent(GameObject o, String filePath, double projSize, double projSpeed){
    this.filePath = filePath;
    this.o = o;
    this.projSize = projSize;
    this.projSpeed = projSpeed;

  }

  public ProjectileComponent(GameObject o, boolean raycast){
    this.o = o;
  }
  public ProjectileComponent(Element data, TransformComponent tc){}

  public ProjectileComponent(Element data, GameObject o){
    projSize = Double.parseDouble(data.getAttribute("projSize"));
    projSpeed = Double.parseDouble(data.getAttribute("projSpeed"));
    filePath = data.getAttribute("filepath");

    this.o = o;


  }

  public Projectile shoot(HashMap<KeyCode, Boolean> inputMap, GameWorld gw, MovementDir currDir){

    SpriteSystem s = gw.getSystem(SystemEnum.Sprite);
    GraphicsSystem g = gw.getSystem(SystemEnum.Graphics);
    CollisionSystem c = gw.getSystem(SystemEnum.Collision);
    ProjectileSystem ps = gw.getSystem(SystemEnum.Projectile);
    Vec2d pSize =new Vec2d(this.o.getTransform().getSize().x * projSize);
    Vec2d pPos = this.o.getTransform().getPosition();
    Projectile p = new Projectile(pPos,pSize , gw
        , currDir);

    SpriteComponent sc = new SpriteComponent(new Vec2d(0), new Vec2d(12, 8), Color.MAROON, gw.getScreenSize(),
        p.getTransform(), false);
    p.addComponent(sc);
    s.addObject(p);


    MoveableComponent pMC = new MoveableComponent(p, projSpeed);
    p.addComponent(pMC);

    CollisionComponent pCC = new CollisionComponent(new AAB(pPos, pSize, true, "projectile"), p);
    p.addComponent(pCC);
    c.addObjectToLayer(p, 2);

    s.loadSprite(p, this.filePath);
    GraphicsComponent pGC = new GraphicsComponent(p.tc, gw.getScreenSize(), sc);
    p.addComponent(pGC);
    g.addObjectToLayer(p, 2);

    ps.addObject(p);

    return p;
    //going to need graphics, sprite, movement, collision, added to g and s

  }

  public RaycastResult shootRay(GameWorld gw, MovementDir currDir){
    //SpriteSystem s = gw.getSystem(SystemEnum.Sprite);
    //GraphicsSystem g = gw.getSystem(SystemEnum.Graphics);
    CollisionSystem c = gw.getSystem(SystemEnum.Collision);
    //ProjectileSystem ps = gw.getSystem(SystemEnum.Projectile);
    //rayResult should include intersections
    TransformComponent tc = this.o.getTransform();
    Vec2d startPoint = tc.getPosition().plus(tc.getSize().smult(0.5));
    Ray rayShot = new Ray(startPoint, currDir.getDir());

    List<RaycastResult> raycasts = new ArrayList<>();
    List<GameObject> oList = new ArrayList<>(c.collideLayers[c.getObjectLayer(this.o)]);

    oList.remove(this.o);
    for (GameObject go: oList){
      CollisionComponent cc = go.getComponent(CompEnum.Collision);
      RaycastResult  rr = cc.getShape().rayCast(rayShot, currDir.getDir(), go);
      if (rr != null){
        raycasts.add(rr);
      }
    }


    RaycastResult closestInt = new RaycastResult(null, null, null, 1000000000, null);
    for (RaycastResult res: raycasts){
      double dist = res.getDist();
      if (dist < closestInt.getDist()){
        closestInt = res;
      }
    }
    if (closestInt.getDist() == 1000000000){
      return null;
    }
    return closestInt;

  }

  @Override
  public void onTick(long nanos) {

  }

  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onLateTick() {

  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "ProjectileComponent");
    el.setAttribute("filepath", filePath);
    el.setAttribute("projSize", Double.toString(projSize));
    el.setAttribute("projSpeed", Double.toString(projSpeed));
    return el;
  }

  @Override
  public CompEnum getTag() {
    return CompEnum.Projectile;
  }
}
