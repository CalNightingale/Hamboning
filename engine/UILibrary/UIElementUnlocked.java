package engine.UILibrary;

import engine.support.Vec2d;
import javafx.scene.paint.Color;

public class UIElementUnlocked extends UIElement{
  private Vec2d screenSize = new Vec2d(960.0, 540.0);
  public UIElementUnlocked(Vec2d position, Vec2d size, Color color, Vec2d screenSize){
    super(position, size, color, screenSize);

  }

//  @Override
//  public void onResize(Vec2d newSize){
//    double aspectRatioX = newSize.x / screenSize.x;
//    double aspectRatioY = newSize.y / screenSize.y;
//
//    this.size = new Vec2d(this.size.x * aspectRatioX, this.size.y * aspectRatioY);
//    this.position = new Vec2d(this.position.x * aspectRatioX, this.position.y * aspectRatioY);
//
//    screenSize = newSize;
//
//
//  }

}
