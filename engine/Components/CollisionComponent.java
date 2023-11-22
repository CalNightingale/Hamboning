package engine.Components;

import engine.GameObject;
import engine.Shapes.AAB;
import engine.Shapes.Circle;
import engine.Shapes.Collision;
import engine.Shapes.Polygon;
import engine.Shapes.Shape;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CollisionComponent implements Component<CollisionComponent>{
  private Shape s;
  private GameObject o;
  public CollisionComponent(Shape s, GameObject o){
    this.o = o;
    this.s = s;

  }
  public CollisionComponent(Element data, TransformComponent tc){

  }

  public CollisionComponent(Element data, GameObject o){
    this.o = o;
    Node shapeNode = data.getElementsByTagName("Shape").item(0);
    Element shapeEl = (Element) shapeNode;
    String shapeType = shapeEl.getAttribute("id");
    switch (shapeType){
      case "AAB":
        this.s = new AAB(shapeEl);
        break;
      case "Circle":
        this.s = new Circle(shapeEl);
        break;
      case "Polygon":
        this.s = new Polygon(shapeEl);
        break;
      default:
        System.out.println("not good for collision shape creation");
    }

  }


  @Override
  public Element serialize(Element el) {
    Document doc = el.getOwnerDocument();
    el.setAttribute("id", "CollisionComponent");

    Element shapeEl = doc.createElement("Shape");
    shapeEl = this.s.serialize(shapeEl);
    el.appendChild(shapeEl);

    return el;
  }


  public void collide(Collision coll){
//    System.out.println("colliding in object");
//    System.out.println("this obj" + this.s);
//    System.out.println("other obj" + coll.otherShape);
    TransformComponent otherTC = coll.other.getTransform();

    //System.out.println("mtv " + coll.mtv);
    if (this.s.isStatic()){
      otherTC.setPos(otherTC.getPosition().plus(coll.mtv.reflect()));
    } else if (coll.otherShape.isStatic()){
      this.o.tc.setPos(this.o.tc.getPosition().plus(coll.mtv));
    } else {
      this.o.tc.setPos(this.o.tc.getPosition().plus(coll.mtv.sdiv(2).reflect()));
      otherTC.setPos(otherTC.getPosition().plus(coll.mtv.sdiv(2)));
    }

  }

  public void setPosition(){this.s.setPosition(o.getTransform());}

  public Vec2d isColliding(Shape s2){return s.collides(s2);}

  public Vec2d isColliding(CollisionComponent c){
    Shape s2 = c.getShape();
    return this.s.collides(s2);
  }


  public void setObject(GameObject o){this.o = o;}
  public Shape getShape(){return this.s;}

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
  public CompEnum getTag() {
    return CompEnum.Collision;
  }
}
