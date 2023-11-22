package engine.Shapes;

import engine.GameObject;
import engine.support.Vec2d;

public class RaycastResult {
  private Ray ray;
  private Vec2d intersection;
  private Shape intShape;
  private double distance;
  private GameObject intO;

  public RaycastResult(Ray ray, Vec2d intersection, Shape shape, double distance, GameObject o){
    this.ray = ray;
    this.intersection = intersection;
    this.intShape = intShape;
    this.distance = distance;
    this.intO = o;
  }

  public void setIntO(GameObject o){this.intO = o;}
  public GameObject getIntO(){return intO;}

  public double getDist(){return this.distance;}

  public Shape getShape(){return this.intShape;}

  public Ray getRay(){return this.ray;}
}
