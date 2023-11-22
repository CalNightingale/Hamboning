package engine.Components;

import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public interface Component<T extends Component<T>> {
  void onTick(long nanos);

  void onDraw(GraphicsContext g);

  void onLateTick();
  Element serialize(Element el);

  CompEnum getTag();

}
