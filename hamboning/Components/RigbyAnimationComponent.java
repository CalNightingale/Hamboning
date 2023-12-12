package hamboning.Components;

import engine.Components.AnimationComponent;
import engine.Components.CompEnum;
import engine.Components.MovementDir;
import engine.Components.SpriteComponent;
import engine.Components.TransformComponent;
import engine.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RigbyAnimationComponent extends AnimationComponent {
  public int rowsRight;
  public int colsRight;
  public int rowsNone;
  public int colsNone;
  private long timeElapsed = 0;
  private long FRAME_DURATION;
  private int currRow = 0;
  private int currCol = 0;
  private int frameCount;
  public int rows;
  public int cols;
  public SpriteComponent mordSC;
  public Vec2d originalPos;

  public RigbyAnimationComponent(Vec2d position, Vec2d size,
      Color color, Vec2d screenSize,
      TransformComponent tc, boolean absolute, int rows, int cols,
      double framedur, GameObject mord) {
    super(position, size, color, screenSize, tc, absolute, rows, cols, framedur);
    this.originalPos = position;
    this.FRAME_DURATION = (long) Math.round(framedur * 1000000000);
    this.originalPos = position;

    this.mordSC = mord.getComponent(CompEnum.Sprite);
    this.rowsRight = 1;
    this.colsRight = 3;
    this.rowsNone = 2;
    this.colsNone = 8;


  }

  public MovementDir getCurrDir(){
    return mordSC.currDir;
  }

  @Override
  public void onTick(long nanos){
    MovementDir cd = getCurrDir();
    if (cd == MovementDir.NONE){
      originalPos = new Vec2d(0,512);
      this.FRAME_DURATION = (long) Math.round(0.2 * 1000000000);


    } else if (cd == MovementDir.RIGHT ||cd == MovementDir.LEFT || cd == MovementDir.UP){
      originalPos = new Vec2d(0,128);
      this.FRAME_DURATION = (long) Math.round(0.4 * 1000000000);

    } else {
      originalPos = new Vec2d(64);
      this.FRAME_DURATION = (long) Math.round(0.5 * 1000000000);

    }

    timeElapsed += nanos;
    if (timeElapsed >= FRAME_DURATION){
      if (cd == MovementDir.NONE){
        // Update current row and column
        currCol = (currCol + 1) % this.colsNone;

        if (currCol == 0) {
          currRow = (currRow + 1) % this.rowsNone;
        }

        // Calculate new xPos and yPos for the sprite based on the original starting position
        double xPos = this.originalPos.x + currCol * getSize().x;
        double yPos = this.originalPos.y + currRow * getSize().y;

        // Set the new position for the sprite
        setPosition(new Vec2d(xPos, yPos));

      } else if (cd == MovementDir.RIGHT || cd == MovementDir.LEFT || cd == MovementDir.UP){
        // Update current row and column
        currRow = (currRow + 1) % this.rowsRight;

        if (currRow == 0) {
          currCol = (currCol + 1) % this.colsRight;
        }

        // Calculate new xPos and yPos for the sprite based on the original starting position
        double xPos = this.originalPos.x + currCol * getSize().x;
        double yPos = this.originalPos.y + currRow * getSize().y;

        // Set the new position for the sprite
        setPosition(new Vec2d(xPos, yPos));
      } else if (cd == MovementDir.DOWN){
        if (getPosition().x == 64){
          setPosition(new Vec2d(192, 128));
        } else {
          setPosition(new Vec2d(64));
        }

      }
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
    if ((getCurrDir() == MovementDir.LEFT)){
      g.drawImage(this.sprite, getPosition().x + getSize().x, getPosition().y, -getSize().x, getSize().y,
          tc.getPosition().x, tc.getPosition().y, tc.getSize().x, tc.getSize().y);

    } else {
      g.drawImage(this.sprite, getPosition().x, getPosition().y, getSize().x, getSize().y,
          tc.getPosition().x, tc.getPosition().y, tc.getSize().x, tc.getSize().y);
    }

  }
}
