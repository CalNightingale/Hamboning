package engine.Components;

import engine.GameObject;
import engine.Viewport;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public class CenterComponent implements Component<CenterComponent>{
  private Viewport vp;
  private TransformComponent tc;
  public CenterComponent(Viewport vp, GameObject o){
    this.vp = vp;
    this.tc = o.getTransform();

  }

  public CenterComponent(Element data, TransformComponent tc){

  }

  @Override
  public void onTick(long nanos) {
    System.out.println("setting viewport");
    this.vp.setPan(this.tc.getPosition());

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
    return CompEnum.Center;
  }
}
