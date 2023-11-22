package engine.Components;

import engine.GameObject;
import engine.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public class DecayComponent implements Component<DecayComponent>{
  private double seconds;
  private GameWorld gw;
  private GameObject go;
  private long accumulatedTime = 0;
  public DecayComponent(double seconds, GameObject o, GameWorld gw){
    this.seconds = seconds * 1e9;
    this.go = o;
    this.gw = gw;

  }

  public DecayComponent(Element data, TransformComponent tc, Vec2d ss){

  }

  @Override
  public void onTick(long nanos) {
    accumulatedTime += nanos;
    if (accumulatedTime > seconds){
      gw.addToRemovals(go);
    }


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
    return CompEnum.Decay;
  }


}
