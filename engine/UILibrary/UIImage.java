package engine.UILibrary;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class UIImage extends UIElement{
  private Image image;
  public UIImage(Vec2d position, Vec2d size, Color color, Vec2d screenSize, Image image){
    super(position, size, color, screenSize);
    this.image = image;

  }

  @Override
  public void onDraw(GraphicsContext g){
    g.drawImage(image, getPosition().x, getPosition().y, getSize().x, getSize().y);
  }

}
