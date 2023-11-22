package engine.Systems;

import engine.GameObject;
import java.util.HashMap;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public class TimerSystem extends BaseSystem {
  public TimerSystem(){
    super();
    setTag(SystemEnum.Timer);

  }

  @Override
  public void onDraw(GraphicsContext g){

  }
  

  @Override
  public Element serialize(Element el){

    return null;
  }

  @Override
  public void onTick(long nanos){

  }

}
