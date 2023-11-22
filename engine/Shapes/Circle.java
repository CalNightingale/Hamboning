package engine.Shapes;

import engine.Components.TransformComponent;
import engine.GameObject;
import engine.support.Vec2d;
import org.w3c.dom.Element;

public class Circle extends Shape{
  protected Vec2d center;
  protected float radius;
  public boolean isStatic;

  public Circle(Vec2d center, float radius, boolean isStatic) {
    this.isStatic = isStatic;
    this.center = center;
    this.radius = radius;
  }

  public Circle(Vec2d topLeft, Vec2d size, boolean isStatic){
    Vec2d halfSize = size.smult(0.5);
    this.center = topLeft.plus(halfSize);
    this.radius = (float) halfSize.x;
    this.isStatic = isStatic;




  }

  public Circle(Element el){
    this.isStatic = Boolean.parseBoolean(el.getAttribute("isStatic"));
    this.center = new Vec2d(el.getAttribute("center"));
    this.radius = Float.parseFloat(el.getAttribute("radius"));

  }

  /////

  public boolean isStatic(){return this.isStatic;}
  public Vec2d getCenter() {
    return center;
  }

  public float getRadius() {
    return radius;
  }

  @Override
  public Vec2d collides(Shape s) {
    return s.collidesCircle(this);
  }

  @Override
  public Vec2d collidesCircle(Circle s2) {
    double mag = this.getRadius() + s2.getRadius() - this.getCenter().dist(s2.getCenter());
    if (mag >0){
      Vec2d dirVec = new Vec2d(this.getCenter().x - s2.getCenter().x, this.getCenter().y - s2.getCenter().y).normalize();
      return dirVec.smult(mag);
    } else {
      return null;
    }
  }


  @Override
  public Vec2d collidesPolygon(Polygon poly){
    return poly.collidesCircle(this);
  }

  @Override
  public Vec2d collidesPoint(Vec2d s2){
    if (this.getCenter().dist(s2) <= this.getRadius()){
      Vec2d dirVec = new Vec2d(s2.x - this.getCenter().x ,  s2.y - this.getCenter().y).normalize();
      return dirVec.smult(this.getRadius() - this.getCenter().dist(s2));

    } else {
      return null;
    }
  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "Circle");
    el.setAttribute("center", this.center.toString());
    el.setAttribute("radius", Double.toString(this.radius));
    el.setAttribute("isStatic", Boolean.toString(this.isStatic));

    return el;
  }

  @Override
  public RaycastResult rayCast(Ray ray, Vec2d dir, GameObject o) {
    Vec2d toCenter = this.center.minus(ray.getLocation());
    double L = toCenter.dot(ray.getDir());

    if (L < 0) {
      return null; // The projection point is behind the ray's start.
    }

    double distanceSquared = toCenter.mag2() - L * L;
    double radiusSquared = this.radius * this.radius;

    if (distanceSquared > radiusSquared) {
      return null; // The projection point is outside the circle.
    }

    double x = Math.sqrt(radiusSquared - distanceSquared);
    double intersectionDistance = L - x;

    if (intersectionDistance < 0) {
      intersectionDistance = L + x; // Intersection behind the ray's start
    }

    if (intersectionDistance < 0) {
      return null; // Intersection still behind the ray's start
    }

    Vec2d intersectionPoint = ray.getLocation().plus(ray.getDir().smult(intersectionDistance));
    return new RaycastResult(ray, intersectionPoint, this, intersectionDistance, o);
  }

  @Override
  public Vec2d collidesAAB(AAB aab) {
    return aab.collidesCircle(this);
  }

  @Override
  public void setPosition(TransformComponent tc) {
    this.radius = (float) tc.getSize().x / 2;
    this.center = new Vec2d(tc.getPosition().x + tc.getSize().x / 2, tc.getPosition().y + tc.getSize().y / 2);

  }
}
