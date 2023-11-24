package engine;

import engine.UILibrary.UIElement;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import java.security.Key;
import java.util.ConcurrentModificationException;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

/**
 * Going to have some sort of basic information that all screens will contain?
 *
 */
public class Screen {
  private List<UIElement> containedEls = new ArrayList<>();
  private Vec2d screenSize;
  private double aspect;

  //size variable
  public Screen(Application app, Vec2d screenSize){
    this.screenSize = screenSize;
    this.aspect = screenSize.x / screenSize.y;
    //app.addScreen(this);



    //gets size in the constructor

  }

  public void setScreenSize(Vec2d ss){
    this.screenSize = ss;
  }

  public Vec2d getScreenSize(){return this.screenSize;}

  public double getAspect(){return this.aspect;}

  public void onTick(long nanosSincePreviousTick){
    for(UIElement el:containedEls){
      el.onTick(nanosSincePreviousTick);
    }



  }

  public void onMouseClicked(MouseEvent e){
    Vec2d point = new Vec2d(e.getX(), e.getY());
    try {
      for(UIElement el:containedEls){
        if (el.isPointInside(point)){
          el.onMouseClicked(e);
        }
      }

    } catch (ConcurrentModificationException b){
      System.out.println("concurrent modification error");
    }

  }

  public void addElements(UIElement el){
    containedEls.add(el);
  }

  public void removeEl(UIElement el){
    if (containedEls.contains(el)){
      System.out.println("removing element");
      containedEls.remove(el);
    }
  }

  public void addElements(List<UIElement> els){
    for(UIElement el: els){
      containedEls.add(el);
    }
  }

  public void onDraw(GraphicsContext g){
    for(UIElement el:containedEls){
      el.onDraw(g);
    }


  }

  public void onMouseMoved(MouseEvent e){
    //System.out.println("reached screen" + this);
    for(UIElement el:containedEls){
      el.onMouseMoved(e);
    }
  }


  public void onResize(Vec2d newSize){
    this.screenSize = newSize;
    for (UIElement el: containedEls){
      el.onResize(newSize, this.aspect);
    }


  }

  public void onMousePressed(MouseEvent e){
    for (UIElement el: containedEls){
      el.onMousePressed(e);
    }
  }

  public void onKeyPressed(KeyEvent e){
    for (UIElement el: containedEls){
      el.onKeyPressed(e);
    }
  }

  public void onKeyReleased(KeyEvent e){
    for (UIElement el: containedEls){
      el.onKeyReleased(e);
    }
  }

  public void onMouseWheelMoved(ScrollEvent e){
    for (UIElement el: containedEls){
      el.onMouseWheelMoved(e);
    }
  }


  public void onMouseReleased(MouseEvent e){
    for (UIElement el: containedEls){
      el.onMouseReleased(e);
    }

  }

  public void onMouseDragged(MouseEvent e){
    for (UIElement el: containedEls){
      el.onMouseDragged(e);
    }
  }



  /**
   * also will be like onMouseClick etc.
   */

}
