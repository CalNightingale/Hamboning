package engine.Systems;

import engine.AILibrary.Map;
import engine.GameObject;
import java.util.HashMap;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PathfindingSystem extends BaseSystem<PathfindingSystem>{
  private Map maze;
  public PathfindingSystem(){
    super();
    setTag(SystemEnum.Pathfinding);
  }


  public void setMaze(Map map){this.maze = map;}

  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){
    //would fill in here for oList
  }

  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    Document doc = el.getOwnerDocument();
    Element inputs = doc.createElement("Maze");
    inputs.setTextContent(maze.toString());
    midEl.appendChild(inputs);
    return midEl;
  }

  @Override
  public void onTick(long nanos) {

  }
}
