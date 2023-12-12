package engine.Shapes;

import engine.GameObject;
import engine.support.Vec2d;

public class Collision {
  public GameObject other;
  public GameObject thisObj;
  public Vec2d mtv;
  public Shape thisShape;
  public Shape otherShape;

  public Collision(GameObject thisObj, GameObject other, Vec2d mtv, Shape thisShape, Shape otherShape){
    this.thisObj = thisObj;
    this.other = other;
    this.mtv = mtv;
    this.thisShape = thisShape;
    this.otherShape = otherShape;

  }


}
