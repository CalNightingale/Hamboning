package engine.Shapes;

import engine.GameObject;
import engine.support.Vec2d;
import java.util.HashMap;

public class Ray {
  private Vec2d loc;
  private Vec2d dir;
  public Ray(Vec2d location, Vec2d direction){
    this.loc = location;
    this.dir = direction.normalize();

  }

  public Vec2d getLocation(){return this.loc;}

  public Vec2d getDir(){return this.dir;}

  public RaycastResult checkEdgeIntersection(Vec2d a, Vec2d b, Shape s, GameObject o) {
    Vec2d m = b.minus(a).normalize();
    Vec2d n = new Vec2d(-m.y, m.x); // Perpendicular to m

    Vec2d p0 = this.loc;
    Vec2d d = this.dir;
    Vec2d p1 = a;
    Vec2d p2 = b;

    double denominator = n.dot(d);
    if (Math.abs(denominator) < 1e-6) {
      return null; // Ray is parallel to the segment
    }

    double t = n.dot(p1.minus(p0)) / denominator;
    if (t < 0) {
      return null; // Intersection point is behind the ray's start
    }

    Vec2d intersectionPoint = p0.plus(d.smult(t));
    double u = m.dot(intersectionPoint.minus(p1)) / m.dot(p2.minus(p1));
    if (u < 0 || u > 1) {
      return null; // Intersection point is not within the segment
    }

    double distance = p0.minus(intersectionPoint).mag();
    return new RaycastResult(this, intersectionPoint,s, distance, o);
  }

//  public void setMinLength(double len){
//    if (length > len){
//      length = len;
//    }
//  }
//
//  public void setMinLength(double len, Shape s){
//    if (length > len){
//      length = len;
//      setIntObject(s);
//    }
//  }
//
//  public void setIntObject(Shape s){
//    this.intObject = s;
//
//  }
//
//  public double getLength(){return this.length;}
//
//  public Shape getIntObject(){return intObject;}
//
}

