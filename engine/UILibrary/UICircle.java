package engine.UILibrary;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class UICircle extends UIElement{
  private double stroke = 0;
  private Color strokeColor = Color.BLACK;
  private boolean justStroke = false;

  public UICircle(Vec2d position, Vec2d size, Color color, Vec2d screenSize){
    super(position, size, color, screenSize);

  }

  public UICircle(Vec2d position, Vec2d size, Color color, Vec2d screenSize, double stroke, Color strokeColor, boolean justStroke){
    super(position, size, color, screenSize);
    this.stroke = stroke;
    this.strokeColor = strokeColor;
    this.justStroke = justStroke;

  }

  public UICircle(Element el, Vec2d ss){
    super(el, ss);
  }

  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    midEl.setAttribute("id", "Circle");
    return midEl;
  }

  @Override
  public void onDraw(GraphicsContext g){
    super.onDraw(g);
    g.setFill(getColor());

    if (!justStroke) {
      g.fillOval(getPosition().x, getPosition().y, getSize().x, getSize().y);

    }
    if (stroke != 0){
      g.setStroke(this.strokeColor);
      g.setLineWidth(this.stroke);
      g.strokeOval(getPosition().x, getPosition().y, getSize().x, getSize().y);

    }


  }

}
