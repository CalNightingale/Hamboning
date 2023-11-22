package engine.Components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Element;

public class ClickableComponent implements Component<ClickableComponent>{

  public ClickableComponent(Element data, TransformComponent tc){

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
    return null;
  }

  public void onClick(MouseEvent e){

  }

  @Override
  public CompEnum getTag() {
    return CompEnum.Clickable;
  }
}
