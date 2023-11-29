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

  private void updateMovementDirections(KeyEvent e, boolean newSeting) {
    switch (e.getCode()) {
      case UP -> this.mc.setUP(newSeting);
      case DOWN -> this.mc.setDOWN(newSeting);
      case LEFT -> this.mc.setLEFT(newSeting);
      case RIGHT -> this.mc.setRIGHT(newSeting);
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
