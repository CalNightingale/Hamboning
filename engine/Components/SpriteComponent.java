package engine.Components;

import engine.UILibrary.UIElement;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SpriteComponent extends UIElement implements Component<SpriteComponent>{
  private Image sprite;
//  private Vec2d spriteScreenPos;
//  private Vec2d spriteScreenSize;
  private TransformComponent tc;
  private boolean absoluteSize;
  public MovementDir currDir = MovementDir.NONE;

  public SpriteComponent(Vec2d position, Vec2d size, Color color, Vec2d screenSize, TransformComponent tc, boolean absolute){
    super(position, size, color, screenSize);
    this.tc = tc;
    this.absoluteSize = absolute;

  }



  public SpriteComponent(Element data, TransformComponent tc, Vec2d ss){
    super(data, ss);
    this.tc = tc;
    this.absoluteSize = Boolean.parseBoolean(data.getAttribute("abs"));

  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "SpriteComponent");
    el.setAttribute("topLeft", getPosition().toString());
    el.setAttribute("inFileSize", getSize().toString());
    el.setAttribute("position", tc.getPosition().toString());
    el.setAttribute("size", tc.getSize().toString());
    el.setAttribute("abs", Boolean.toString(absoluteSize));

    return el;
  }

//  public SpriteComponent(UIElement element, Vec2d screenSize, TransformComponent tc, boolean absolute){
//    super(element.getPosition(), element.getSize(), element.color, screenSize);
//    this.element = element;
//    this.tc = tc;
//    this.absoluteSize = absolute;
//    this.preBuiltElement = true;
//
//  }

  public void setDir(MovementDir dir){this.currDir = dir;}
  public MovementDir getCurrDir(){return this.currDir;}

  public void setImage(Image sprite){
    this.sprite = sprite;

    //sets the aspect ratio relative to the size of the box
    if (!this.absoluteSize){
      double sizeX = tc.getSize().x;
      double sizeY = tc.getSize().y;
      double aspect = getSize().x / getSize().y;
      if (aspect < 1){
        tc.setSize(new Vec2d(sizeX * aspect, tc.getSize().y));
      } else {
        tc.setSize(new Vec2d(tc.getSize().x, sizeY / aspect ));
      }
    }
    //this.setSpriteCanvas();


  }


  @Override
  public void onTick(long nanos) {


  }

  @Override
  public void onDraw(GraphicsContext g) {
//    if (getPosition().x != 0){
//      System.out.println(getPosition() + "pos");
//      System.out.println(tc.getPosition() + "tc pos");
//    }
      g.drawImage(this.sprite, getPosition().x, getPosition().y, getSize().x, getSize().y,
          tc.getPosition().x, tc.getPosition().y, tc.getSize().x, tc.getSize().y);





      //here is where I can edit this






  }

  @Override
  public void onLateTick() {

  }



  @Override
  public CompEnum getTag() {
    return CompEnum.Sprite;
  }
}
