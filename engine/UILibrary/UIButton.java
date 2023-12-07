package engine.UILibrary;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javax.swing.JButton;
import javax.swing.JComponent;


public class UIButton extends UIElement{
  private Clickable clickAction;
  private boolean isClicked = false;
  private Color color;
  private final String text;
  private Vec2d rounding = new Vec2d(0);

  private final String font;
  private final int fontSize;
  private Color textColor;

  public UIButton(Vec2d pos, Vec2d siz, Color butCol, Vec2d screenSize, String font, int fontSize, Color textCol, String text, Vec2d rounding){
    super(pos, siz, butCol, screenSize);
    this.textColor = textCol;
    this.text = text;
    this.rounding = rounding;
    this.font = font;
    this.fontSize = fontSize;
    this.setupButton();
  }

  public UIButton(Vec2d pos, Vec2d siz, Color butCol, Vec2d screenSize, Color textCol, String text, Vec2d rounding){
    this(pos, siz, butCol, screenSize, "Arial", (int) (siz.x/ text.length()), textCol, text, rounding);
  }

  public void setupButton(){
    UIRectangle buttonContainer = new UIRectangle(getPosition(), getSize(), getColor(), getScreenSize(), this.rounding);
    this.addChild(buttonContainer);
    Vec2d textLoc = new Vec2d(getPosition().x + getSize().x / 2, getPosition().y + getSize().y /2);
    UIText buttonText = new UIText(textLoc, new Vec2d(this.fontSize), this.text, this.font, this.textColor, getScreenSize());
    this.addChild(buttonText);
  }

  @Override
  public void onMouseClicked(MouseEvent e){
    super.onMouseClicked(e);
    if (clickAction != null && isPointInside(new Vec2d(e.getX(), e.getY()))) {
      clickAction.onClick();
    }
    isClicked = true;
  }

  public void setTextColor(Color color){this.textColor = color;}
  public Color getTextColor(){return this.textColor;}

  public boolean isClicked(){return this.isClicked;}

  public void setClickAction(Clickable action) {
    this.clickAction = action;
  }





}
