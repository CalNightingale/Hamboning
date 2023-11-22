package engine.UILibrary;

import engine.Components.TransformComponent;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;


/**
 * problem with rounding atm
 */
public class UIRectangle extends UIElement{
  private Vec2d rounding = new Vec2d(0);
  private double strokeWidth = 0;
  private double degrees = 0;
  private Color strColor = Color.RED;
  private boolean pureStroke = false;

  public UIRectangle(Vec2d position, Vec2d size, Color color, Vec2d screenSize, Vec2d rounding){
    super(position, size, color, screenSize);
    this.rounding = rounding;

  }

  public UIRectangle(Vec2d position, Vec2d size, Color color, Vec2d screenSize){
    super(position, size, color, screenSize);
    this.strokeWidth = 0;
  }

  public UIRectangle(Vec2d position, Vec2d size, Color color, Vec2d screenSize, Vec2d rounding, double strokeWidth, Color strokeColor){
    super(position, size, color, screenSize);
    this.rounding = rounding;
    this.strokeWidth = strokeWidth;
    this.strColor = strokeColor;

  }

  public UIRectangle(Vec2d position, Vec2d size, Color color, Vec2d screenSize, Vec2d rounding, double strokeWidth, Color strokeColor, boolean pureStroke){
    super(position, size, color, screenSize);
    this.rounding = rounding;
    this.strokeWidth = strokeWidth;
    this.strColor = strokeColor;
    this.pureStroke = pureStroke;

  }
  public UIRectangle(Element el, Vec2d ss){
    super(el, ss);

  }

  public UIRectangle(Vec2d position, Vec2d size, Color color, Vec2d screenSize, Vec2d rounding, double rot){
    super(position, size, color, screenSize);
    this.rounding = rounding;
    this.degrees = Math.toRadians(rot);
  }




  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    midEl.setAttribute("id", "Rectangle");
    return el;
  }

  @Override
  public void onDraw(GraphicsContext g){
    if (pureStroke){
      g.save();
      g.setStroke(this.strColor); //there is currently a problem with my rounding
      g.setLineWidth(this.strokeWidth);
      g.strokeRoundRect(getPosition().x, getPosition().y, getSize().x, getSize().y, rounding.x,
          rounding.y);
      g.restore();

    } else {
      if (strokeWidth > 0){
        g.setStroke(this.strColor); //there is currently a problem with my rounding
        g.setLineWidth(this.strokeWidth);
        g.strokeRoundRect(getPosition().x, getPosition().y, getSize().x, getSize().y, rounding.x,
            rounding.y);

      }

      g.setFill(getColor());
      g.fillRect(getPosition().x, getPosition().y, getSize().x, getSize().y);


    }



  }
}
