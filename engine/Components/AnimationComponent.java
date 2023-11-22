package engine.Components;

import engine.GameObject;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class AnimationComponent extends SpriteComponent{
  private int rows;
  private int cols;
  private long timeElapsed = 0;
  private long FRAME_DURATION;
  private int currRow = 0;
  private int currCol = 0;
  private int frameCount;
  public Vec2d originalPos;


  public AnimationComponent(Vec2d position, Vec2d size,
      Color color, Vec2d screenSize,
      TransformComponent tc, boolean absolute, int rows, int cols, double framedur) {
    super(position, size, color, screenSize, tc, absolute);
    this.FRAME_DURATION = (long) Math.round(framedur * 1000000000);
    //System.out.println(this.FRAME_DURATION);
    this.originalPos = position;
    this.rows = rows;
    this.cols = cols;


  }

  public AnimationComponent(Element data, TransformComponent tc, Vec2d ss){
    super(data, tc, ss);

  }


  public void setOriginalPos(Vec2d originalPos){this.originalPos = originalPos;}
  @Override
  public void onTick(long nanos){
    timeElapsed += nanos;
    if (timeElapsed >= FRAME_DURATION){

      // Update current row and column
      currRow = (currRow + 1) % this.rows;

      if (currRow == 0) {
        currCol = (currCol + 1) % this.cols;
      }

      // Calculate new xPos and yPos for the sprite based on the original starting position
      double xPos = this.originalPos.x;
      double yPos = this.originalPos.y + currRow * getSize().y;

      // Set the new position for the sprite
      setPosition(new Vec2d(xPos, yPos));

      // Debugging prints
//      System.out.println("xPos: " + xPos);
//      System.out.println("YPos: " + yPos);
//      System.out.println("currRow: " + currRow);
//      System.out.println("currCol: " + currCol);
//      System.out.println(" ");

      // Reset elapsed time
      timeElapsed = 0;
    }
  }

  @Override
  public Element serialize(Element el) {
    return null;
  }

//  @Override
//  public CompEnum getTag() {
//    return CompEnum.ANIMATION;
//  }
}
