package engine;

import engine.Components.CompEnum;
import engine.Components.ComponentRegistry;
import engine.Components.DecayComponent;
import engine.Systems.BaseSystem;
import engine.Systems.CollisionSystem;
import engine.Systems.GraphicsSystem;
import engine.Systems.InputSystem;
import engine.Systems.SystemEnum;
import engine.support.Vec2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GameWorld {
  private final Vec2d gameWorldSize;
  public List<GameObject> oList = new ArrayList<>();
  private List<GameObject> removalQueue = new ArrayList<>();
  public HashMap<SystemEnum, BaseSystem<? extends BaseSystem>> systems = new HashMap<>();
  private Vec2d ss;
  public Viewport vp;
  public ComponentRegistry cr;


  public GameWorld(Vec2d gwSize, Vec2d screenSize, boolean firstTick){
    this.gameWorldSize = gwSize;
    this.ss = screenSize;
    this.cr = new ComponentRegistry();


  }

  public Vec2d getScreenSize(){return this.ss;}



  public Vec2d getGWSize() {
    return gameWorldSize;
  }

  public <T extends BaseSystem<?>> void addSystem(T s){systems.put(s.getTag(), s);}

  public void addObjects(GameObject o){oList.add(o);}

  public void onDraw(GraphicsContext g){

    this.systems.get(SystemEnum.Graphics).onDraw(g);

  }

  public void clearSystems(){this.systems = new HashMap<>();}

  public <T extends BaseSystem<T>> T getSystem(SystemEnum tag) {
    if (systems.containsKey(tag)) {
      return (T) systems.get(tag);
    }
    return null;
  }

  public void onTick(long nanos){

    for (SystemEnum systemEnum : SystemEnum.values()) {
      if (this.systems.get(systemEnum) != null) {
        this.systems.get(systemEnum).onTick(nanos);
      }
    }
    for (GameObject o: oList){
      DecayComponent dc = o.getComponent(CompEnum.Decay);
      if (dc != null){
        o.onTick(nanos);
      }
    }

    this.processRemovals();
  }


  public void addToRemovals(GameObject o){
    this.removalQueue.add(o);
  }
  public void processRemovals(){
    for (GameObject obj: removalQueue){
      oList.remove(obj);
      for (BaseSystem s: systems.values()){
        s.removeObject(obj);
      }
    }
    removalQueue.clear();

  }

  public void onKeyPressed(KeyEvent e){
    //System.out.println("pressing key");
    InputSystem is = getSystem(SystemEnum.Input);
    is.onKeyPressed(e);


  }

  public void givePort(Viewport vp){this.vp = vp;}

  public void onKeyReleased(KeyEvent e){
    InputSystem is = getSystem(SystemEnum.Input);
    is.onKeyReleased(e);

  }

  public void onKeyTyped(KeyEvent e) {

  }

  public void onMouseClicked(MouseEvent e){

  }

  public Viewport getVP(){return this.vp;}
  public void setLevel(){}

  public void onMousePressed(Vec2d point){
    GraphicsSystem gs = getSystem(SystemEnum.Graphics);
    gs.onMousePressed(point);
  }

  public void onMouseReleased(MouseEvent e){
    CollisionSystem cs = getSystem(SystemEnum.Collision);
    cs.onMouseReleased(e);
  }

  public void onMouseDragged(Vec2d point){
    GraphicsSystem gs = getSystem(SystemEnum.Graphics);
    gs.onMouseDragged(point);

  }

  public void onMouseMoved(MouseEvent e){

  }

  public void onResize(Vec2d newSize, double aspect){
    GraphicsSystem gs = getSystem(SystemEnum.Graphics);
    gs.onResize(newSize, aspect);

  }


}
