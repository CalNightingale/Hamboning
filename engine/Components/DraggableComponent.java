package engine.Components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Element;

public class DraggableComponent implements Component<DraggableComponent> {
  private GraphicsComponent gc;
  private CollisionComponent cc;
  private Vec2d initialMousePosition;  // Store where the mouse was first pressed
  private Vec2d initialObjectPosition; // Store the object's initial position when mouse was first pressed
  private boolean isDragging = false;

  public DraggableComponent(GraphicsComponent gc) {


    this.gc = gc;
  }

  public DraggableComponent(GraphicsComponent gc, CollisionComponent cc){
    this.gc = gc;
    this.cc = cc;
  }

  public DraggableComponent(Element data, TransformComponent tc){

  }

  public void onMousePressed(Vec2d point) {
    this.isDragging = true;
    initialMousePosition = point;  // Remember where the mouse was pressed
    initialObjectPosition = gc.getPosition();  // Remember the object's position at the time
  }

  public void onMouseDragged(Vec2d point) {
    if (isDragging && initialMousePosition != null) {  // Ensure the mouse was pressed first
      // Calculate the distance the mouse has moved
      Vec2d distanceMoved = point.minus(initialMousePosition);

      // Add that distance to the object's initial position
      Vec2d newPosition = initialObjectPosition.plus(distanceMoved);

      // Update the object's position
      this.gc.setPosition(newPosition);

      if (this.cc != null){
        this.cc.setPosition();
      }

    }
  }

  public void stopDragging(){this.isDragging = false;}

  @Override
  public void onTick(long nanos) {

  }

  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onLateTick() {

  }


  @Override
  public Element serialize(Element el) {
    return null;
  }

  @Override
  public CompEnum getTag() {
    return CompEnum.Draggable;
  }
}
