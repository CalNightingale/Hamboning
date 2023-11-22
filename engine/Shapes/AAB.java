package engine.Shapes;

import engine.Components.TransformComponent;
import engine.GameObject;
import engine.Interval;
import engine.support.Vec2d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;

public class AAB extends Shape{
  protected Vec2d topLeft;
  protected Vec2d size;
  public boolean isStatic;

  public AAB(Vec2d topLeft, Vec2d size, boolean isStatic) {
    this.isStatic = isStatic;
    this.topLeft = topLeft;
    this.size = size;
  }

  public AAB(Element el){
    this.isStatic = Boolean.parseBoolean(el.getAttribute("isStatic"));
    this.topLeft = new Vec2d(el.getAttribute("topLeft"));
    this.size = new Vec2d(el.getAttribute("size"));
  }


  /////

  public boolean isStatic(){return this.isStatic;}
  public Vec2d getTopLeft() {
    return topLeft;
  }

  public Vec2d getSize() {
    return size;
  }

  public Interval xProject(AAB s){
    return new Interval(s.getTopLeft().x, s.getSize().x + s.getTopLeft().x);
  }

  public Interval yProject(AAB s){
    return new Interval(s.getTopLeft().y, s.getSize().y + s.getTopLeft().y);
  }

  @Override
  public Vec2d collides(Shape s) {
    return s.collidesAAB(this);
  }

  @Override
  public Vec2d collidesCircle(Circle s2) {

    Interval s1X = xProject(this);
    Interval s1Y = yProject(this);
    if (this.containsPoint(new Vec2d(s2.getCenter().x, s2.getCenter().y))){
      Vec2d mtvs[] = new Vec2d[4];

      mtvs[0] = new Vec2d(s2.getCenter().x, s1Y.getMin());
      mtvs[1] = new Vec2d(s2.getCenter().x, s1Y.getMax());
      mtvs[2] = new Vec2d(s1X.getMin(), s2.getCenter().y);
      mtvs[3] = new Vec2d(s1X.getMax(), s2.getCenter().y);

      HashMap<Integer, Vec2d> indexDir = new HashMap();
      indexDir.put(0, new Vec2d(0,1));
      indexDir.put(1, new Vec2d(0,-1));
      indexDir.put(2, new Vec2d(1,0));
      indexDir.put(3, new Vec2d(-1,0));

      int minIndex = 0;
      double minDist = mtvs[0].dist(s2.getCenter());
      for (int i = 1; i < mtvs.length; i++){

        double dist = mtvs[i].dist(s2.getCenter());
        if (dist < minDist){
          minDist = dist;
          minIndex = i;
        }
      }

      Vec2d p = mtvs[minIndex];
      return indexDir.get(minIndex).smult(s2.getRadius() + s2.getCenter().dist(p));
    } else {
      //min x point is
      double pointX = Math.min(Math.max(s2.getCenter().x, this.getTopLeft().x), this.getTopLeft().x + this.getSize().x);
      double pointY = Math.min(Math.max(s2.getCenter().y, this.getTopLeft().y),
          this.getTopLeft().y + this.getSize().y);
      // do point-circle collision
      return s2.collidesPoint(new Vec2d(pointX, pointY));
    }
  }


  public boolean containsPoint(Vec2d point){
    return (point.x > topLeft.x && point.x < (topLeft.x + size.x) && point.y > topLeft.y && point.y < (topLeft.y + size.y));
  }

  @Override
  public Vec2d collidesAAB(AAB s2) {

    Interval s1X = xProject(this);
    Interval s2X = xProject(s2);
    Interval s1Y = yProject(this);
    Interval s2Y = yProject(s2);

    if (s1X.overlap(s2X) && s1Y.overlap(s2Y)){
      //System.out.println("shape1" + this.topLeft + " " + this.size);
      //System.out.println("shape2" + s2.topLeft + " " + s2.size);
      //check minimum between left right up down
      Vec2d[] mtvs = new Vec2d[4];

      mtvs[0] = new Vec2d(0, s2Y.getMin()-s1Y.getMax());//up
      mtvs[1] = new Vec2d(0, s2Y.getMax()-s1Y.getMin());//down
      mtvs[2] = new Vec2d(s2X.getMin()-s1X.getMax(), 0);//left
      mtvs[3] = new Vec2d(s2X.getMax()-s1X.getMin(), 0);//right

      int minIndex = 0;
      double minMag = mtvs[0].mag();
      for (int i = 1; i < mtvs.length; i++) {
        if (mtvs[i].mag() < minMag) {
          minMag = mtvs[i].mag();
          minIndex = i;
        }
      }
      return mtvs[minIndex].reflect();
    } else {
      return null;
    }
  }

  @Override
  public Vec2d collidesPolygon(Polygon poly){
    return poly.collidesAAB(this);
  }

  @Override
  public Vec2d collidesPoint(Vec2d s2){
    Interval s1X = this.xProject(this);
    Interval s1Y = this.yProject(this);
    if (this.containsPoint(s2)){

      Vec2d[] mtvs = new Vec2d[4];
      HashMap<Integer, Vec2d> indexDir = new HashMap();
      indexDir.put(0, new Vec2d(0,1));
      indexDir.put(1, new Vec2d(0,-1));
      indexDir.put(2, new Vec2d(1,0));
      indexDir.put(3, new Vec2d(-1,0));

      mtvs[0] = new Vec2d(s2.x, s1Y.getMax());
      mtvs[1] = new Vec2d(s1X.getMin(), s2.y);
      mtvs[2] = new Vec2d(s2.x, s1Y.getMin());
      mtvs[3] = new Vec2d(s1X.getMax(), s2.y);

      int minIndex = 0;
      Vec2d minVec = mtvs[0];
      for (int i = 1; i < mtvs.length; i++){
        if (mtvs[i].mag() < minVec.mag()){
          minVec = mtvs[i];
          minIndex = i;
        }
      }
      return indexDir.get(minIndex).smult(s2.dist(minVec));

    } else {
      return null;
    }
  }

  @Override
  public void setPosition(TransformComponent tc) {
    this.topLeft = tc.getPosition();
    this.size = tc.getSize();

  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "AAB");
    el.setAttribute("topLeft", this.topLeft.toString());
    el.setAttribute("size", this.size.toString());
    el.setAttribute("isStatic", Boolean.toString(this.isStatic));

    return el;
  }

  @Override
  public RaycastResult rayCast(Ray ray, Vec2d dir, GameObject o) {
    RaycastResult closestResult = null;
    double closestDistance = Double.MAX_VALUE;


    Vec2d topRight = new Vec2d(topLeft.x + size.x, topLeft.y);
    Vec2d bottomLeft = new Vec2d(topLeft.x, topLeft.y + size.y);
    Vec2d bottomRight = new Vec2d(topLeft.x + size.x, topLeft.y + size.y);

// Define the edges
    Vec2d[] aabEdges = new Vec2d[8];
    aabEdges[0] = topLeft;     // Top edge start
    aabEdges[1] = topRight;    // Top edge end
    aabEdges[2] = topRight;    // Right edge start
    aabEdges[3] = bottomRight; // Right edge end
    aabEdges[4] = bottomRight; // Bottom edge start
    aabEdges[5] = bottomLeft;  // Bottom edge end
    aabEdges[6] = bottomLeft;  // Left edge start
    aabEdges[7] = topLeft;
    for (int i = 0; i < aabEdges.length; i += 2) {
      RaycastResult result = ray.checkEdgeIntersection(aabEdges[i], aabEdges[i + 1], this, o);
      if (result != null && result.getDist() < closestDistance) {
        closestResult = result;
        closestDistance = result.getDist();
      }
    }
    return closestResult;
  }

  @Override
  public Vec2d getCenter() {
    return null;
  }

}
