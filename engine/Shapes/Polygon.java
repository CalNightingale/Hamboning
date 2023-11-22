package engine.Shapes;

import Nin.NinConstants;
import engine.Components.TransformComponent;
import engine.GameObject;
import engine.HelperFunctions;
import engine.Interval;
import engine.support.Vec2d;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

public class Polygon extends Shape {
  public boolean isStatic;
  protected Vec2d[] points;
  Vec2d min;
  Vec2d max;

  public Polygon(boolean isStatic, Vec2d... points ){
    this.isStatic = isStatic;
    this.points = points;
    double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
    for(Vec2d v : points) {
      minX = Double.min(minX, v.x);
      minY = Double.min(minY, v.y);
      maxX = Double.max(maxX, v.x);
      maxY = Double.max(maxY, v.y);
    }
    min = new Vec2d(minX, minY);
    max = new Vec2d(maxX, maxY);

  }

  public Polygon(Element el){
    this.isStatic = Boolean.parseBoolean(el.getAttribute("isStatic"));
    String pointsArray = el.getTextContent();
    this.points = HelperFunctions.parseVec2dArray(pointsArray);
    double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
    for(Vec2d v : points) {
      minX = Double.min(minX, v.x);
      minY = Double.min(minY, v.y);
      maxX = Double.max(maxX, v.x);
      maxY = Double.max(maxY, v.y);
    }
    min = new Vec2d(minX, minY);
    max = new Vec2d(maxX, maxY);


  }

  public int getNumPoints() {
    return points.length;
  }

  public Vec2d getPoint(int i) {
    return points[i];
  }

  @Override
  public boolean isStatic() {
    return this.isStatic;
  }


  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "Polygon");
    el.setAttribute("isStatic", Boolean.toString(this.isStatic));
    el.setTextContent(HelperFunctions.arrayToString(points));

    return el;
  }

  @Override
  public RaycastResult rayCast(Ray ray, Vec2d dir, GameObject o) {
    RaycastResult closestResult = null;
    double closestDistance = Double.MAX_VALUE;

    for (int i = 0; i < points.length; i++) {
      Vec2d a = points[i];
      Vec2d b = points[(i + 1) % points.length]; // Loop back to the first point
      RaycastResult result = ray.checkEdgeIntersection(a, b, this, o);
      if (result != null && result.getDist() < closestDistance) {
        closestResult = result;
        closestDistance = result.getDist();
      }
    }

    return closestResult;
  }

  @Override
  public Vec2d collides(Shape s) {
    return s.collidesPolygon(this);
  }


  @Override
  public Vec2d collidesCircle(Circle c) {
    Vec2d center = c.getCenter();
    double radius = c.getRadius();
    Vec2d mtv = null;
    double minDist = Double.POSITIVE_INFINITY;

    // Check each edge of the polygon
    for (int i = 0; i < getNumPoints(); i++) {
      Vec2d p1 = getPoint(i);
      Vec2d p2 = getPoint((i + 1) % getNumPoints());

      // Find the closest point on the edge to the circle's center
      Vec2d closestPoint = closestPointOnLineSegment(p1, p2, center);

      // Check the distance between the closest point and the center
      double dist = center.dist(closestPoint);
      if (dist < radius) {
        Vec2d overlap = center.minus(closestPoint);
        double overlapLength = radius - dist;
        Vec2d potentialMTV = overlap.normalize().smult(overlapLength);

        // Update the MTV if this overlap is smaller
        if (potentialMTV.mag() < minDist) {
          minDist = potentialMTV.mag();
          mtv = potentialMTV;
        }
      }
    }

    return mtv;
  }

  private Vec2d closestPointOnLineSegment(
      Vec2d p1, Vec2d p2, Vec2d point) {
    Vec2d segment = p2.minus(p1);
    double t = point.minus(p1).dot(segment) / segment.dot(segment);
    t = Math.max(0, Math.min(1, t));
    return p1.plus(segment.smult(t));
  }

  @Override
  public Vec2d collidesAAB(AAB aab) {
    List<Vec2d> axes = new ArrayList<>();

		// Get the axes from the AABShape's sides
		axes.add(new Vec2d(1, 0)); // Horizontal axis
		axes.add(new Vec2d(0, 1)); // Vertical axis

		// Get perpendicular axes for the PolygonShape
		addPerpendiculars(this, axes);

		double minMagnitude = Double.POSITIVE_INFINITY;
		Vec2d mtv = null;

		for (Vec2d axis : axes) {
			// Project each shape onto the axis
			Interval proj1 = projectOntoAxis(aab, axis);
			Interval proj2 = projectOntoAxis(this, axis);

			// Calculate the 1D MTV for the projections
			Double mtv1d = intervalMTV(proj1, proj2);
			if (mtv1d == null) {
				// The projections don't overlap, so the shapes are not colliding
				return null;
			}

			// Keep track of the smallest overlapping MTV
			if (Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.smult(mtv1d); // Multiply the axis by the MTV magnitude to get the 2D MTV
			}
		}

		// If the loop completes, the shapes are colliding and we return the smallest MTV
		return mtv;
  }

  	private void addPerpendiculars(Polygon shape, List<Vec2d> axes) {
		int numPoints = shape.getNumPoints();
		for (int i = 0; i < numPoints; i++) {
			Vec2d p1 = shape.getPoint(i);
			Vec2d p2 = shape.getPoint((i+1) % numPoints); // Get the next vertex, or loop back to the start

			// Compute edge from p1 to p2
			Vec2d edge = p2.minus(p1);

			// Get the perpendicular vector: (-y, x)
			Vec2d normal = new Vec2d(-edge.y, edge.x);

			// Normalize the perpendicular vector before adding to the list of axes
			axes.add(normal.normalize());
		}
	}

  @Override
  public Vec2d collidesPolygon(Polygon poly) {
    List<Vec2d> axes = new ArrayList<>();

		// Get perpendicular axes for both polygons
		addPerpendiculars(poly, axes);
		addPerpendiculars(this, axes);

		double minMagnitude = Double.POSITIVE_INFINITY;
		Vec2d mtv = null;

		for (Vec2d axis : axes) {
			// Project each shape onto the axis
			Interval proj1 = projectOntoAxis(poly, axis);
			Interval proj2 = projectOntoAxis(this, axis);

			// Calculate the 1D MTV for the projections
			Double mtv1d = intervalMTV(proj1, proj2);
			if (mtv1d == null) {
				// The projections don't overlap, so the shapes are not colliding
				return null;
			}

			// Keep track of the smallest overlapping MTV
			if (Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.smult(mtv1d); // Multiply the axis by the MTV magnitude to get the 2D MTV
			}
		}

		// If the loop completes, the shapes are colliding and we return the smallest MTV
		return mtv;
  }

  @Override
  public Vec2d collidesPoint(Vec2d point) {
    Vec2d mtv = null;
		Double minMagnitude = Double.POSITIVE_INFINITY;

		// 1. Iterate over the edges of the PolygonShape
		for (int i = 0; i < getNumPoints(); i++) {
			// Find the edge
			Vec2d p1 = getPoint(i);
			Vec2d p2 = getPoint((i + 1) % getNumPoints()); // wrap around for the last point

			// Find the normal to the edge (we'll use this as the projection axis)
			Vec2d edge = p2.minus(p1);
			Vec2d axis = new Vec2d(-edge.y, edge.x).normalize();

			// 2. Project the PolygonShape and the point onto this axis
			Interval projS1 = projectOntoAxis(this, axis);
			Interval projS2 = new Interval(point.dot(axis), point.dot(axis)); // projection of a point is just a point

			// 3. Calculate the MTV for this axis
			Double mtv1d = intervalMTV(projS1, projS2);
			if (mtv1d == null) {
				return null; // no overlap on this axis, so the point is outside the polygon
			}

			// Update the MTV if this one is smaller
			if (Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.smult(mtv1d);
			}
		}

		// After checking all axes, if we found an MTV, then there is a collision
		if (mtv != null && minMagnitude < Double.POSITIVE_INFINITY) {
			return mtv;
		}
		return null;
  }

  @Override
  public void setPosition(TransformComponent tc) {
    Vec2d minVec = this.min;
    Vec2d change = tc.getPosition().minus(minVec);

    Vec2d[] newPoints = new Vec2d[points.length];
    int i = 0;
    for (Vec2d v : points){
      newPoints[i] = v.plus(change);
      i++;
    }
    this.points = newPoints;
    min = min.plus(change);
    max = max.plus(change);

  }

  @Override
  public Vec2d getCenter() {
    return min.plus(max).sdiv(2);
  }



  private Interval projectOntoAxis(Polygon shape, Vec2d axis) {
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;

    // Project each point of the shape onto the axis and find the min and max
    for (int i = 0; i < shape.getNumPoints(); i++) {
      double projection = shape.getPoint(i).dot(axis);
      min = Math.min(min, projection);
      max = Math.max(max, projection);
    }
    return new Interval(min, max);
  }

  private Interval projectOntoAxis(AAB shape, Vec2d axis) {
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;

    // Extract corners of the AABShape
    Vec2d topLeft = shape.getTopLeft();
    Vec2d bottomRight = topLeft.plus(shape.getSize());
    Vec2d topRight = new Vec2d(bottomRight.x, topLeft.y);
    Vec2d bottomLeft = new Vec2d(topLeft.x, bottomRight.y);

    Vec2d[] corners = {
        topLeft,
        topRight,
        bottomLeft,
        bottomRight
    };

    for (Vec2d corner : corners) {
      double projection = corner.dot(axis);
      min = Math.min(min, projection);
      max = Math.max(max, projection);
    }

    return new Interval(min, max);
  }


  private Double intervalMTV(Interval a, Interval b) {
    Double aRight = b.getMax() - a.getMin();
    Double aLeft  = a.getMax() - b.getMin();

    if (aLeft < 0 || aRight < 0) {
      return null; // No overlap between intervals
    }

    if (aRight < aLeft) {
      return aRight;
    } else {
      return -aLeft;
    }
  }
}
