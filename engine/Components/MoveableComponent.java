package engine.Components;

import engine.GameObject;
import engine.support.Vec2d;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;


public class MoveableComponent implements Component<MoveableComponent>{
  protected double moveSpeed;
  private static HashMap<MovementDir, Vec2d> dirInfo;
  protected HashMap<MovementDir, Boolean> shouldMove;



  protected GameObject o;
  protected TransformComponent tc;

  public MovementDir currDir = MovementDir.NONE;
  private long frameCount = 0;

  public MoveableComponent(GameObject o, double moveAmount){
    this.moveSpeed = moveAmount;
    setDirs();

    shouldMove = new HashMap<>();
    shouldMove.put(MovementDir.UP, false);
    shouldMove.put(MovementDir.RIGHT, false);
    shouldMove.put(MovementDir.LEFT, false);
    shouldMove.put(MovementDir.DOWN, false);

    this.o = o;
    this.tc = this.o.getTransform();


  }

  public MoveableComponent(Element data, TransformComponent tc){
    System.out.println("bad news moveablecomp");


  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "MoveableComponent");
    el.setAttribute("moveAmount", String.valueOf(moveSpeed));
    el.setTextContent(shouldMove.toString());
    return el;
  }

  public MoveableComponent(Element data, GameObject o){
    this.moveSpeed = Double.parseDouble(data.getAttribute("moveAmount"));
    setDirs();

    shouldMove = createHashMapFromString(data.getTextContent());

    this.o = o;
    this.tc = this.o.getTransform();

  }

  public static HashMap<MovementDir, Boolean> createHashMapFromString(String input) {
    HashMap<MovementDir, Boolean> map = new HashMap<>();

    // Remove the curly braces and trim the input string
    input = input.substring(1, input.length() - 1).trim();

    // Split the string into individual key-value pair strings
    String[] pairs = input.split(", ");

    // Process each key-value pair
    for (String pair : pairs) {
      // Split the key-value pairs by the equals sign
      String[] keyValue = pair.split("=");
      String key = keyValue[0].trim();
      MovementDir dirKey = MovementDir.valueOf(key);
      boolean value = Boolean.parseBoolean(keyValue[1].trim());

      // Add to map
      map.put(dirKey, value);
    }

    return map;
  }


  public void setDirs(){
    dirInfo = new HashMap<>();
    dirInfo.put(MovementDir.UP, new Vec2d(0, -1));
    dirInfo.put(MovementDir.DOWN, new Vec2d(0, 1));
    dirInfo.put(MovementDir.LEFT, new Vec2d(-1, 0));
    dirInfo.put(MovementDir.RIGHT, new Vec2d(1, 0));
    dirInfo.put(MovementDir.NONE, new Vec2d(0));




  }
  public void setStop(){
    shouldMove.put(MovementDir.UP, false);
    shouldMove.put(MovementDir.DOWN, false);
    shouldMove.put(MovementDir.LEFT, false);
    shouldMove.put(MovementDir.RIGHT, false);
  }
  public void setDir(boolean set, MovementDir dir){
    shouldMove.put(dir, set);
  }

  public void setUP(boolean set){shouldMove.put(MovementDir.UP, set);}
  public void setDOWN(boolean set){
    shouldMove.put(MovementDir.DOWN, set);}
  public void setLEFT(boolean set){shouldMove.put(MovementDir.LEFT, set);}
  public void setRIGHT(boolean set){shouldMove.put(MovementDir.RIGHT, set);}

  public Integer getNumDirs(){
    int numDirs = 0;
    if (shouldMove.get(MovementDir.UP))numDirs++;
    if (shouldMove.get(MovementDir.DOWN))numDirs++;
    if (shouldMove.get(MovementDir.LEFT))numDirs++;
    if (shouldMove.get(MovementDir.RIGHT))numDirs++;
    return numDirs;
  }

  public boolean isMoving() {
    return getNumDirs() > 0;
  }

  @Override
  public void onTick(long nanos) {
    //System.out.println(currDir);
    double moveAmount = this.moveSpeed * nanos / 1000000000;
    int numDirs = getNumDirs();
    if (numDirs == 0){
      currDir = MovementDir.NONE;
      SpriteComponent ac = this.o.getComponent(CompEnum.Sprite);
      if (ac != null){
        ac.setDir(currDir);
      }
      return;

    }
    if (numDirs == 4) return;
    if (numDirs == 2) moveAmount /= Math.sqrt(2); //normalizes for diagonal motion


    if (shouldMove.get(MovementDir.UP)) {
      moveUp(moveAmount);

      currDir = MovementDir.UP;
    } else if (shouldMove.get(MovementDir.DOWN)) {
      moveDown(moveAmount);

      currDir = MovementDir.DOWN;
    } else if (shouldMove.get(MovementDir.LEFT)) {
      moveLeft(moveAmount);

      currDir = MovementDir.LEFT;
    } else if (shouldMove.get(MovementDir.RIGHT)){
      moveRight(moveAmount);

      currDir = MovementDir.RIGHT;
    }


    SpriteComponent ac = this.o.getComponent(CompEnum.Sprite);
    if (ac != null){
      ac.setDir(currDir);
    }

    PathfindingComponent pc = this.o.getComponent(CompEnum.Pathfinding);
    if (pc != null){
        pc.setStart(this.o.getTransform().getPosition());
      }
    }


  public void moveUp(double amt){
    double newX = this.tc.getPosition().x;
    double newY = this.tc.getPosition().y;
    newY -= amt;
    this.tc.setPos(new Vec2d(newX, newY));
  }


  public void moveDown(double amt){
    double newX = this.tc.getPosition().x;
    double newY = this.tc.getPosition().y;
    newY += amt;
    this.tc.setPos(new Vec2d(newX, newY));
  }

  public void moveLeft(double amt){
    double newX = this.tc.getPosition().x;
    double newY = this.tc.getPosition().y;
    newX -= amt;
    this.tc.setPos(new Vec2d(newX, newY));
  }

  public void moveRight(double amt){
    double newX = this.tc.getPosition().x;
    double newY = this.tc.getPosition().y;
    newX += amt;
    this.tc.setPos(new Vec2d(newX, newY));
  }
  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onLateTick() {

  }



  @Override
  public CompEnum getTag() {
    return CompEnum.Moveable;
  }
}
