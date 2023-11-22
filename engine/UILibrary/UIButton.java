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
  private double pixRatio = 1.1172; // meaning 1 font size -> 1.1172 pixels
  private Clickable clickAction;
  private boolean isClicked = false;
  private Color color;
  private String text;
  private Vec2d rounding = new Vec2d(0);
  private Vec2d padding = new Vec2d(10);

  private Font font = new Font("Arial", 1);
  private Color textColor = Color.BLACK;


  public UIButton(Vec2d pos, Vec2d siz, Color butCol, Vec2d screenSize, Color textCol, String text, Vec2d rounding, Vec2d padding, String font){
    super(pos, siz, butCol, screenSize);
    this.textColor = textCol;
    this.text = text;
    this.rounding = rounding;
    this.padding = padding;
    this.font = new Font(font, 1);
    this.setupButton();
  }

  public UIButton(Vec2d pos, Vec2d siz, Color butCol, Vec2d screenSize, Color textCol, String text, Vec2d rounding){
    super(pos, siz, butCol, screenSize);
    this.textColor = textCol;
    this.text = text;
    this.rounding = rounding;
    this.padding = new Vec2d(7);
    this.setupButton();
  }

  public void setupButton(){
    UIRectangle buttonContainer = new UIRectangle(getPosition(), getSize(), getColor(), getScreenSize(), this.rounding);
    this.addChild(buttonContainer);

    Vec2d textLoc = new Vec2d(getPosition().x + getSize().x / 2, getPosition().y + getSize().y /2);
    double maxHeight = getSize().y - (2 * padding.y);
    double maxLength = getSize().x - (2 * padding.x);

    Font tallFont = new Font(this.font.getName(), maxHeight / pixRatio);
    if (new FontMetrics(this.text, tallFont).width < maxLength){
      this.font = new Font(this.font.getName(), maxHeight / pixRatio);
    } else {
      boolean loopCon = true;
      while (loopCon){
        FontMetrics fm = new FontMetrics(this.text, this.font);
        if (fm.width < maxLength){
          this.font = new Font(this.font.getName(), this.font.getSize()+1);
        } else {
          loopCon = false;
        }
      }

    }

    UIText buttonText = new UIText(textLoc, new Vec2d(this.font.getSize()), this.text, this.font.getName(), this.textColor,getScreenSize());
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
