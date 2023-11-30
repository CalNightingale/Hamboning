package engine.Components;

import engine.GameObject;
import engine.support.Vec2d;

public class TransformComponent {
  private Vec2d pos;
  private Vec2d size;
  private GameObject o;

  public TransformComponent(Vec2d position, Vec2d size, GameObject o){
    this.pos = position;
    this.size = size;
    this.o = o;

  }



  public Vec2d getPosition(){return this.pos;}

  public Vec2d getSize(){return this.size;}

  public Vec2d getCenter(){return this.pos.plus(this.size.smult(0.5));}

  public void setPos(Vec2d pos){
    this.pos = pos;
    CollisionComponent cc = this.o.getComponent(CompEnum.Collision);
    if (cc != null){
      cc.getShape().setPosition(this);
    }
//    GraphicsComponent gc = this.o.getComponent(CompEnum.Graphics);
//    if (gc != null){
//      gc.setShapePos(pos); //work around for polygons not the cleanest
//    }

  }

  public void setSize(Vec2d size){this.size = size;}

}
