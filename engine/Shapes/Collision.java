package engine.Shapes;

import engine.GameObject;
import engine.support.Vec2d;

public class Collision {
  public GameObject other;
  public Vec2d mtv;
  public Shape thisShape;
  public Shape otherShape;

  public Collision(GameObject other, Vec2d mtv, Shape thisShape, Shape otherShape){
    this.other = other;
    this.mtv = mtv;
    this.thisShape = thisShape;
    this.otherShape = otherShape;

  }


}
