package engine.UILibrary;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UIText extends UIElement {
  private String text;
  private Font font;
  private Color color;

  public UIText(Vec2d position, Vec2d size, String text, String fontNameOrPath, Color color, Vec2d screenSize){
    super(position, size, color, screenSize);
    this.text = text;
    // Load the font
    if (fontNameOrPath.toLowerCase().endsWith(".ttf")) {
      // It's a path to a .ttf file
      try {
        FileInputStream fontStream = new FileInputStream(fontNameOrPath);
        this.font = Font.loadFont(fontStream, getSize().x);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Failed to find font file: " + fontNameOrPath);
      }
    } else {
      // It's a standard font name
      this.font = new Font(fontNameOrPath, getSize().x);
    }
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
    super.onDraw(g);
    if (!this.visible) return;
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
