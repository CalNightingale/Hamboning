package hamboning.Components;

import engine.Components.CompEnum;
import engine.Components.InputComponent;
import engine.Components.MoveableComponent;
import engine.Components.TransformComponent;
import engine.GameObject;
import engine.GameWorld;
import java.util.HashMap;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Element;

public class CharacterInputComponent  extends InputComponent {
  private GameObject o;
  private MoveableComponent mc;
  private TransformComponent tc;
  private GameWorld gw;

  private final Runnable cartFunc;
  public CharacterInputComponent(GameObject o, GameWorld gw, Runnable cartFunc){
    this.gw = gw;
    this.o = o;
    this.mc = this.o.getComponent(CompEnum.Moveable);
    this.tc = this.o.getTransform();
    this.cartFunc = cartFunc;
  }

  public CharacterInputComponent(Element data, GameObject o, GameWorld gameWorld){
    super(data, o.tc);
    this.mc = o.getComponent(CompEnum.Moveable);
    this.tc = o.getTransform();
    this.cartFunc = null;

  }

  private void updateMovementDirections(KeyEvent e, boolean newSetting) {
    switch (e.getCode()) {
      case UP: {
        this.mc.setUP(newSetting);
        break;
      }
      case DOWN: {
        this.mc.setDOWN(newSetting);
        break;
      }
      case LEFT: {
        this.mc.setLEFT(newSetting);
        break;
      }
      case RIGHT: {
        this.mc.setRIGHT(newSetting);
        break;
      }
    }
  }

  public void onKeyReleased(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    super.onKeyReleased(e,inputMap);
    updateMovementDirections(e, false);
  }
  public void onKeyPressed(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    super.onKeyPressed(e, inputMap);
    updateMovementDirections(e, true);
    // get into cart if interact button pressed
    if (e.getCode() == KeyCode.E){
      cartFunc.run();
    }
  }

}
