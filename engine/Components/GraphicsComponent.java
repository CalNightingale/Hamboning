package engine.Components;

import engine.UILibrary.UICircle;
import engine.UILibrary.UIElement;
import engine.UILibrary.UIImage;
import engine.UILibrary.UIPolygon;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GraphicsComponent implements Component<GraphicsComponent>{
  private Image sprite;
  private UIImage img;
  private String spritePath;
  private TransformComponent tc;
  private Vec2d ss;
  private UIElement element;


  public GraphicsComponent(TransformComponent tc, String imagePath, Vec2d screenSize){
    this.spritePath = imagePath;
    this.tc = tc;
    this.ss = screenSize;

  }

  public GraphicsComponent(Element data, TransformComponent tc, Vec2d screenSize){
    this.tc = tc;
    this.ss = screenSize;
    Node ip = data.getElementsByTagName("imagePath").item(0);
    if (ip != null){
      this.spritePath = ip.getTextContent();
    }

    Node el = data.getElementsByTagName("ShapeEl").item(0);
    if (el != null){
      Element shapeEl = (Element) el;
      String shapeType = shapeEl.getAttribute("id");

      switch(shapeType){
        case "Rectangle":
          this.element = new UIRectangle(shapeEl, screenSize);

          break;
        case "Circle":
          this.element = new UICircle(shapeEl, screenSize);

          break;
        case "Polygon":
          this.element = new UIPolygon(shapeEl, screenSize);
          break;
        default:
          System.out.println("bad news drawing shapes in load (graphicsComponent)");
          break;
      }
    }


  }

  public GraphicsComponent(TransformComponent tc, Vec2d ss, UIElement element){
    this.tc = tc;
    this.ss = ss;
    this.element = element;

  }

  @Override
  public Element serialize(Element el) {
    Document doc = el.getOwnerDocument();
    el.setAttribute("id", "GraphicsComponent");

    if (this.element != null){
      Element shape = doc.createElement("ShapeEl");
      shape = this.element.serialize(shape);
      el.appendChild(shape);
    } else {
      Element imagePath = doc.createElement("imagePath");
      imagePath.setTextContent(this.spritePath);
      el.appendChild(imagePath);
    }


    return el;
  }

  public void setShapePos(Vec2d pos){
    if (this.element != null){
      this.element.setPosition(pos);
    }
  }

  public void setElement(UIElement el){this.element = el;}

  @Override
  public void onTick(long nanos) {

  }

  @Override
  public void onDraw(GraphicsContext g) {
    if (this.element != null) {
      //this.element.setPosition(this.element.getPosition().plus(tc.getPosition()));
      //this.element.setPosition(tc.getPosition());
      //this.element.setSize(tc.getSize());
      this.element.onDraw(g);
    }


  }

  @Override
  public void onLateTick() {

  }


  @Override
  public CompEnum getTag() {
    return CompEnum.Graphics;
  }

  public Vec2d getPosition(){return this.tc.getPosition();}

  public void setPosition(Vec2d position){this.tc.setPos(position);}

  public void onResize(Vec2d newSize, double aspect){
    if (this.img != null){
      this.img.onResize(newSize, aspect);
    } else {
      this.element.onResize(newSize, aspect);
    }



  }
}

