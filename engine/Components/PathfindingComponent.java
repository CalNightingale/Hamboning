package engine.Components;

import engine.AILibrary.Map;
import engine.AILibrary.Pathfinding;
import engine.GameObject;
import engine.support.Vec2d;
import engine.support.Vec2i;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public class PathfindingComponent implements Component<PathfindingComponent>{
  public Vec2i start;
  public Vec2i target;
  private Map maze;
  private List<Vec2i> path;
  private Pathfinding pfinder;
  private Vec2d tileSize;
  private GameObject targetObj;

  public PathfindingComponent(Vec2i start, Vec2i target, Map maze, Vec2d tileSize, GameObject targetObj){
    this.targetObj = targetObj;
    this.start = start;
    this.target = target;
    this.maze = maze;
    this.tileSize = tileSize;

    this.pfinder = new Pathfinding();
    this.path = this.pfinder.aStarPathfinding(this.start, this.target, this.maze);
    //System.out.println(start);
    //System.out.println(target);
    //System.out.println(this.path);



  }

  public PathfindingComponent(Element data, TransformComponent tc){

  }

  public GameObject getTarget(){return targetObj;}
  public void setTargetObj(GameObject targ){this.targetObj = targ;}

  public void setStart(Vec2d s){

    Vec2d targetD = s.smult(1 / tileSize.x);
    Vec2i newStart = new Vec2i((int) targetD.x, (int) targetD.y);
    if (newStart != start && maze.getVal(newStart) == 1){
      this.start = newStart;
      this.path = this.pfinder.aStarPathfinding(this.start, this.target, this.maze);

    }
  }

  public List<Vec2i> getPath(){return this.path;}

  public void setTarget(Vec2d t){
    Vec2d targetD = t.smult(1 / tileSize.x);
    Vec2i newTarget = new Vec2i((int) targetD.x, (int) targetD.y);
    if (newTarget != this.target && maze.getVal(newTarget) == 1){

      this.target = newTarget;
      this.path = this.pfinder.aStarPathfinding(this.start, this.target, this.maze);
    }


  }

  public void setMaze(Map m){
    this.maze = m;
    this.path = this.pfinder.aStarPathfinding(this.start, this.target, this.maze);

  }


  @Override
  public void onTick(long nanos) {
    setTarget(this.targetObj.getTransform().getPosition());
  }

  @Override
  public void onDraw(GraphicsContext g) {

  }


  @Override
  public void onLateTick() {

  }

  @Override
  public Element serialize(Element el) {
    return null;
  }

  @Override
  public CompEnum getTag() {
    return CompEnum.Pathfinding;
  }
}
