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

  public CharacterInputComponent(GameObject o, GameWorld gw){
    this.gw = gw;
    this.o = o;
    this.mc = this.o.getComponent(CompEnum.Moveable);
    this.tc = this.o.getTransform();

  }

  public void onKeyReleased(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    super.onKeyReleased(e,inputMap);
    if (e.getCode() == KeyCode.UP)this.mc.setUP(false);
    if (e.getCode() == KeyCode.DOWN)this.mc.setDOWN(false);
    if (e.getCode() == KeyCode.LEFT)this.mc.setLEFT(false);
    if (e.getCode() == KeyCode.RIGHT)this.mc.setRIGHT(false);

  }
  public void onKeyPressed(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    super.onKeyPressed(e, inputMap);

    if (e.getCode() == KeyCode.UP)this.mc.setUP(true);
    if (e.getCode() == KeyCode.DOWN)this.mc.setDOWN(true);
    if (e.getCode() == KeyCode.LEFT)this.mc.setLEFT(true);
    if (e.getCode() == KeyCode.RIGHT)this.mc.setRIGHT(true);
    if (e.getCode() == KeyCode.SPACE){
    }
  }

}
