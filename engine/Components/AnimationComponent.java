package engine.Components;

import engine.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class AnimationComponent extends SpriteComponent{
  public int rowsRight;
  public int colsRight;
  public int rowsNone;
  public int colsNone;
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
    this.rowsRight = rows;
    this.colsRight = cols;
    this.rowsNone = 2;
    this.colsNone = 8;


  }

  public AnimationComponent(Element data, TransformComponent tc, Vec2d ss){
    super(data, tc, ss);

  }


  public void setOriginalPos(Vec2d originalPos){this.originalPos = originalPos;}
  @Override
  public void onTick(long nanos){
    if (rowsRight == 1 && colsRight == 1){

    } else {
      timeElapsed += nanos;
      if (timeElapsed >= FRAME_DURATION){

        if (currDir == MovementDir.NONE){
          // Update current row and column
          currCol = (currCol + 1) % this.colsNone;

          if (currCol == 0) {
            currRow = (currRow + 1) % this.rowsRight;
          }

          // Calculate new xPos and yPos for the sprite based on the original starting position
          double xPos = this.originalPos.x + currCol * getSize().x;
          double yPos = this.originalPos.y + currRow * getSize().y;

          // Set the new position for the sprite
          setPosition(new Vec2d(xPos, yPos));

        } else {
          // Update current row and column
          currRow = (currRow + 1) % this.rowsRight;

          if (currRow == 0) {
            currCol = (currCol + 1) % this.colsRight;
          }

          // Calculate new xPos and yPos for the sprite based on the original starting position
          double xPos = this.originalPos.x;
          double yPos = this.originalPos.y + currRow * getSize().y;

          // Set the new position for the sprite
          setPosition(new Vec2d(xPos, yPos));

    }



      }



      System.out.println(getPosition());
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
  public void onDraw(GraphicsContext g) {
    if ((this.getCurrDir() == MovementDir.LEFT)){
      g.drawImage(this.sprite, getPosition().x, getPosition().y, getSize().x, getSize().y,
          tc.getPosition().x, tc.getPosition().y, tc.getSize().x, tc.getSize().y);


    } else {
      g.drawImage(this.sprite, getPosition().x + getSize().x, getPosition().y, -getSize().x, getSize().y,
          tc.getPosition().x, tc.getPosition().y, tc.getSize().x, tc.getSize().y);


    }


  }


  @Override
  public Element serialize(Element el) {
    Element midEl = super.serialize(el);
    midEl.setAttribute("framerate", "skdfjs"); //serialize this
    return midEl;
  }

//  @Override
//  public CompEnum getTag() {
//    return CompEnum.ANIMATION;
//  }
}
