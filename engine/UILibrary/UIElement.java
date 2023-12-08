package engine.UILibrary;

import engine.Components.TransformComponent;
import engine.support.Vec2d;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class UIElement {

  private Vec2d position;
  private Vec2d size;
  public Color color;
  private List<UIElement> children = new ArrayList<>();
  private Vec2d windowSize;
  boolean visible;

  public UIElement(Vec2d position, Vec2d size, Color color, Vec2d screenSize) {
    this.position = position;
    this.size = size;
    this.color = color;
    this.windowSize = screenSize;
    this.visible = true;
  }

  public UIElement(Vec2d position, Vec2d size, Vec2d screenSize){
    this.position = position;
    this.size = size;
    this.color = Color.BLACK;
    this.windowSize = screenSize;
    this.visible = true;
  }

  public UIElement(Element el, Vec2d screenSize){
    this.position = new Vec2d(el.getAttribute("position"));
    this.size = new Vec2d(el.getAttribute("size"));
    this.color = Color.web(el.getAttribute("color"));
    this.windowSize = screenSize;
    this.visible = Boolean.parseBoolean(el.getAttribute("visible"));
  }

  public void addChild(UIElement el){
    this.children.add(el);
  }

  public Vec2d getScreenSize(){return this.windowSize;}
  public void clearChildren(){
    this.children.clear();
  }

  public boolean isPointInside(Vec2d point) {
    return point.x >= position.x && point.x <= position.x + size.x &&
        point.y >= position.y && point.y <= position.y + size.y;
  }

  public Element serialize(Element el){
    el.setAttribute("position", this.position.toString());
    el.setAttribute("size", this.size.toString());
    el.setAttribute("color", this.color.toString());
    el.setAttribute("visible", Boolean.toString(this.visible));
    return el;
  }


  public void setPosition(Vec2d position) {
    this.position = position;
  }
  public void setVisible(boolean visible) { this.visible = visible; }

  public Vec2d getPosition(){
    return this.position;
  }

  public Vec2d getSize(){
    return this.size;
  }

  public void setSize(Vec2d size) {
    this.size = size;
  }

  public void setColor(Color color) {this.color = color;}

  public Color getColor() {return this.color;}




  public void onMouseMoved(MouseEvent e){
    for (UIElement child: this.children){
      child.onMouseMoved(e);
    }

  }

  public void onTick(long nanos) {
    for (UIElement child: this.children){
      child.onTick(nanos);
    }
  }

  public void onDraw(GraphicsContext g) {
    if (!visible) return;
    for (UIElement child: this.children){
      child.onDraw(g);
    }
  }

  public void onMouseClicked(MouseEvent e){
    for (UIElement child: this.children){
      child.onMouseClicked(e);
    }
  }



  public void onResize(Vec2d windowSize, double aspect) {

//    System.out.println(this.getClass().getName());
//    System.out.println(this.children);
    double testX = windowSize.y * aspect;

    if (testX <= windowSize.x){
      windowSize = new Vec2d((windowSize.y * aspect), windowSize.y);
    } else {
      windowSize = new Vec2d(windowSize.x, windowSize.x * (1/aspect));
    }

    double aspectRatioX = windowSize.x / this.windowSize.x;
    double aspectRatioY = windowSize.y / this.windowSize.y;



    this.size = new Vec2d(this.size.x * aspectRatioX, this.size.y * aspectRatioY);
    this.position = new Vec2d(this.position.x * aspectRatioX, this.position.y * aspectRatioY);
    for (UIElement child: this.children){
      child.onResize(windowSize, aspect);

    }

    this.windowSize = windowSize;


  }

  public void onMousePressed(MouseEvent e){
  }

  public void onKeyPressed(KeyEvent e){}

  public void onKeyReleased(KeyEvent e){}

  public void onMouseWheelMoved(ScrollEvent e){}


  public void onMouseReleased(MouseEvent e){}

  public void onMouseDragged(MouseEvent e){}








}
