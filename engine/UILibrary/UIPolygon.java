package engine.UILibrary;

import Nin.NinConstants;
import engine.HelperFunctions;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.w3c.dom.Element;

public class UIPolygon extends UIElement{
Vec2d[] points;
  public UIPolygon(Color color, Vec2d screenSize, Vec2d ... points) {
    super(new Vec2d(0), new Vec2d(0), color, screenSize);
    double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
    this.points = points;
    for(Vec2d v : points) {
      minX = Double.min(minX, v.x);
      minY = Double.min(minY, v.y);
      maxX = Double.max(maxX, v.x);
      maxY = Double.max(maxY, v.y);
    }

    super.setPosition(new Vec2d(minX, minY));
    setSize(new Vec2d(maxX-minX, maxY-minY));
  }

  public UIPolygon(Element el, Vec2d ss){
    super(el, ss);
    String pointsArray = el.getTextContent();
    this.points = HelperFunctions.parseVec2dArray(pointsArray);
    double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
    for(Vec2d v : points) {
      minX = Double.min(minX, v.x);
      minY = Double.min(minY, v.y);
      maxX = Double.max(maxX, v.x);
      maxY = Double.max(maxY, v.y);
    }

    super.setPosition(new Vec2d(minX, minY));
    setSize(new Vec2d(maxX-minX, maxY-minY));
  }


  public void setPoints(Vec2d[] points){
    System.out.println("setting points");
    this.points = points;}


  @Override
  public void setPosition(Vec2d newPos){
    Vec2d minVec = getPosition();
    Vec2d change = newPos.minus(minVec);

    Vec2d[] newPoints = new Vec2d[points.length];
    int i = 0;
    for (Vec2d v : points){
      newPoints[i] = v.plus(change);
      i++;
    }
    this.points = newPoints;
    super.setPosition(newPos);
  }

  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    midEl.setTextContent(HelperFunctions.arrayToString(points));
    midEl.setAttribute("id", "Polygon");
    return midEl;
  }



  @Override
  public void setSize(Vec2d newSiz){
    //haven't done anything with this
    super.setSize(newSiz);
  }

  @Override
  public boolean isPointInside(Vec2d point){
    //would implement dif implementation
    return false;
  }

  @Override
  public void onDraw(GraphicsContext g){
    double[] xPoints = new double[points.length];
    double[] yPoints = new double[points.length];

    for (int i = 0; i < points.length; i++) {
      xPoints[i] = points[i].x;
      yPoints[i] = points[i].y;
    }

    g.setFill(getColor());
    //g.fillRect(0.25, 0.25, 0.3, 0.3);
    g.fillPolygon(xPoints, yPoints, points.length);



  }

}
