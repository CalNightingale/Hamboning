package engine.Systems;

import engine.GameObject;
import java.util.HashMap;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public class ProjectileSystem extends BaseSystem<ProjectileSystem>{
  public ProjectileSystem(){
    setTag(SystemEnum.Projectile);
  }

  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){
    super.initialize(el, gameObjectMap);
    //would fill in here for oList
  }


  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    midEl.setAttribute("id", "ProjectileSystem");
    return midEl;
  }

  @Override
  public void onTick(long nanos) {
    for (GameObject obj: objList){
      obj.onTick(nanos);
    }

  }


}
