package engine.UILibrary;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UIText extends UIElement {
  private String text;
  private Font font;
  private Color color;

  public UIText(Vec2d position, Vec2d size, String text, String font, Color color, Vec2d screenSize){
    super(position, size, color, screenSize);
    this.text = text;
    this.font = new Font(font, getSize().x);
    this.color = color;
  }

  public Vec2d getTextPosition(){
    FontMetrics textInfo = new FontMetrics(this.text, this.font);
    double textWidth = textInfo.width;
    double textHeight = textInfo.height;

//    System.out.println(textHeight);
//    System.out.println(size.x);

    //System.out.println(this.text);

    double textX = getPosition().x - (textWidth / 2.0) ;
    double textY = getPosition().y + (textHeight / 3.0);
    return new Vec2d(textX, textY);

  }
  @Override
  public void onDraw(GraphicsContext g){
    Vec2d textPos = this.getTextPosition();



    g.setFill(color);
    g.setFont(this.font); // Adjust font size as needed
    g.fillText(this.text, textPos.x, textPos.y);


  }

  public void setFont(String font) {
    this.font = new Font(font, this.font.getSize());
  }

  public void setFont(double size, boolean absolute){
    if (!absolute){
      this.font = new Font(this.font.getName(), size);
    } else {
      FontMetrics textInfo = new FontMetrics(this.text, this.font);
      while (textInfo.height <= size){
        this.font = new Font(this.font.getName(), this.font.getSize() + 1);
        System.out.println("increasing "+ this.font.getSize());
      }

    }

  }


  public void setText(String text) {
    this.text = text;
  }

  @Override
  public void onResize(Vec2d newSize, double aspect){
    super.onResize(newSize, aspect);
    setFont(getSize().x, false);


  }


}
