package engine;

import engine.UILibrary.UIElement;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.w3c.dom.Element;

public class Viewport extends UIElement {

  private Vec2d startingDragPoint;


  private GameWorld gw;
  private double zoom;
  private Affine gameToScreen;
  private Affine screenToGame;
  private Vec2d pt = new Vec2d(0,0);
  private double panScale = 5;
  private double scrollFactor = .1;
  private UIRectangle border;
  private Vec2d gameCenter;
  private Vec2d initPos;



  public Viewport(Vec2d position, Vec2d size, Vec2d screenSize, GameWorld gw) {
    super(position, size, screenSize);
    this.gw = gw;
    this.border = new UIRectangle(getPosition(), getSize(), getColor(), getScreenSize(), new Vec2d(0),
        3, Color.BLACK, true);
    addChild(this.border);
    this.zoom = computeMinScale();
    this.gameCenter = new Vec2d(this.getPosition().x + this.getSize().x/2,
        this.getPosition().y + this.getSize().y/2);
    this.gameToScreen = this.gameToScreenCompute(this.zoom);
    this.screenToGame = getInvertedAffine();

  }

  @Override
  public Element serialize(Element el){
    el.setAttribute("zoom", Double.toString(zoom));
    el.setAttribute("gameCenter", gameCenter.toString());
    el.setAttribute("pan-translate", pt.toString());
    el.setAttribute("position", getPosition().toString());
    el.setAttribute("size", getSize().toString());
    el.setAttribute("screenSize", getScreenSize().toString());
    return el;
  }

  public void load(Element el){
    //set all the values
  }

  public void setInitialPos(Vec2d initPos){this.initPos = initPos;}

  public void setPan(Vec2d newPan){
    Vec2d translationInGameSpace = newPan.minus(initPos);
    // Scale the game-space translation to screen-space units
    this.pt = translationInGameSpace.smult(-this.zoom);
    this.gameToScreen = this.gameToScreenCompute(this.zoom);
  }

  private Affine gameToScreenCompute(double zoom){
    Affine gameToScreen = new Affine();

    gameToScreen.appendTranslation(this.getPosition().x + this.getSize().x / 2,
        this.getPosition().y + this.getSize().y / 2);

    // Translate in screen space, taking zoom into account
    gameToScreen.appendTranslation(this.pt.x, this.pt.y);

    gameToScreen.appendScale(zoom, zoom);

    gameToScreen.appendTranslation(-(this.gw.getGWSize().x / 2), -(this.gw.getGWSize().y / 2));

    return gameToScreen;
  }

  private double computeMinScale(){
    double scaleX = getSize().x / this.gw.getGWSize().x;
    double scaleY = getSize().y / this.gw.getGWSize().y;

    return Math.max(scaleY, scaleX);
  }




  private Affine screenToGameCompute(){
    Affine screenToGame = new Affine();
    screenToGame.appendTranslation((this.gw.getGWSize().x/2), this.gw.getGWSize().y/2);
    screenToGame.appendScale(-this.zoom, -this.zoom);
    screenToGame.appendTranslation(-this.pt.x, -this.pt.y);
    screenToGame.appendTranslation(-(this.getPosition().x + this.getSize().x/2),
        -(this.getPosition().y + this.getSize().y/2));

    return screenToGame;
  }

  private Affine getInvertedAffine(){
    gameToScreen = gameToScreenCompute(this.zoom);
    Affine sTG = new Affine(gameToScreen);

    try {
      sTG.invert();

    } catch (NonInvertibleTransformException e) {
      e.printStackTrace();
    }
    return sTG;
  }


  @Override
  public void onTick(long nanos){
    super.onTick(nanos);
    this.gw.onTick(nanos);
  }

  @Override
  public void onDraw(GraphicsContext g) {

    g.save();
    g.beginPath();
    g.rect(this.border.getPosition().x, this.border.getPosition().y, this.border.getSize().x, this.border.getSize().y);
    g.closePath();
    g.clip();


    g.transform(this.gameToScreen);

    gw.onDraw(g);
    g.restore();
    super.onDraw(g);

  }

  public void onMousePressed(MouseEvent e) {
    Vec2d point = new Vec2d(e.getX(), e.getY());
    //System.out.println(point + " screen space point");
    //System.out.println(screenTG(point) + " game space point");

    this.gw.onMousePressed(screenTG(point));

  }

  public void onMouseDragged(MouseEvent e) {
    Vec2d point = new Vec2d(e.getX(), e.getY());

    this.gw.onMouseDragged(screenTG(point));

  }

  private Vec2d screenTG(Vec2d point){
    Point2D gamePointA = screenToGame.transform(point.x, point.y);
    return new Vec2d(gamePointA.getX(), gamePointA.getY());

  }

  @Override
  public void onMouseReleased(MouseEvent e){
    this.gw.onMouseReleased(e);
  }

  public void onMouseWheelMoved(ScrollEvent e){
    double baseZoomFactor = 1.05;  // base factor
    double scaleFactor = 0.001;   // control the sensitivity of zoom changes based on deltaY. Adjust as needed.
    double dynamicZoomFactor = 1 + e.getDeltaY() * scaleFactor;

    if(dynamicZoomFactor > baseZoomFactor) {
      dynamicZoomFactor = baseZoomFactor;  // Cap to max zoom in factor
    } else if(dynamicZoomFactor < 1/baseZoomFactor) {
      dynamicZoomFactor = 1/baseZoomFactor;  // Cap to max zoom out factor
    }

    double proposedZoom = this.zoom * dynamicZoomFactor;
    GameWorld testWorld = new GameWorld(this.gw.getGWSize(), getScreenSize(), true);

    if (isValidPanPosition(KeyCode.P, testWorld, proposedZoom)){
      this.zoom = Math.max(proposedZoom, computeMinScale());
      this.gameToScreen = this.gameToScreenCompute(this.zoom);

    }





  }




  public void onKeyPressed(KeyEvent e){
    this.pt = getNewPanPosition(e.getCode());
    this.gameToScreen = this.gameToScreenCompute(this.zoom);
    this.gw.onKeyPressed(e);
    //screenToGameSpace = screenToGameCompute();
  }

  public void onKeyReleased(KeyEvent e){
    this.gw.onKeyReleased(e);
  }



  private Vec2d getNewPanPosition(KeyCode direction) {
    Vec2d proposedPan = new Vec2d(pt.x, pt.y);

    if (isValidPanPosition(direction, this.gw, this.zoom)){
      switch (direction) {
        case I:
          proposedPan.y += panScale;
          break;
        case K:
          proposedPan.y -= panScale;
          break;
        case J:
          proposedPan.x += panScale;
          break;
        case L:
          proposedPan.x -= panScale;
          break;
        default:
          return pt;
      }
      return proposedPan;

    }
    return pt;
  }

  private boolean isValidPanPosition(KeyCode dir, GameWorld gw, double zoom) {
    Vec2d halfGame = gw.getGWSize().smult(zoom * 0.5);

    switch (dir){
      case L:
        return this.gameCenter.x + this.pt.x + halfGame.x - (getSize().x+ getPosition().x) >= panScale;

      case I:
        return halfGame.y - (getSize().y / 2 + this.pt.y) >= panScale;

      case J:
        return halfGame.x - (getSize().x / 2 + this.pt.x) >= panScale;

      case K:
        return this.gameCenter.y + this.pt.y + halfGame.y - (getSize().y+ getPosition().y) >= panScale;

      case P:
        return ((this.gameCenter.x + this.pt.x + halfGame.x - (getSize().x+ getPosition().x) >= panScale) &&
            (halfGame.y - (getSize().y / 2 + this.pt.y) >= panScale) &&
            (halfGame.x - (getSize().x / 2 + this.pt.x) >= panScale) &&
            (this.gameCenter.y + this.pt.y + halfGame.y - (getSize().y+ getPosition().y) >= panScale));
      default:
        return false;
    }

  }

  @Override
  public void onResize(Vec2d newSize, double aspect){
    super.onResize(newSize, aspect);
    this.zoom = computeMinScale();
    this.gameToScreen = this.gameToScreenCompute(this.zoom);

  }










}