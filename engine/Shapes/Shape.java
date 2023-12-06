package engine.Shapes;

import engine.Components.MovementDir;
import engine.support.Vec2d;
import engine.Components.TransformComponent;
import engine.GameObject;
import org.w3c.dom.Element;

public abstract class Shape {
  public abstract boolean isStatic();
  public abstract String getTag();
  public abstract Vec2d collides(Shape s);
  public abstract Vec2d collidesCircle(Circle c);
  public abstract Vec2d collidesAAB(AAB aab);
  public abstract Vec2d collidesPolygon(Polygon poly);
  public abstract Vec2d collidesPoint(Vec2d point);
  public abstract void setPosition(TransformComponent tc);
  public abstract Element serialize(Element el);
  public abstract RaycastResult rayCast(Ray ray, Vec2d dir, GameObject o);

  public abstract Vec2d getCenter();
}
