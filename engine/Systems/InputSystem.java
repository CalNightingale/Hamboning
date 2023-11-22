package engine.Systems;


import engine.GameObject;
import java.util.HashMap;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InputSystem extends BaseSystem<InputSystem>{
  public HashMap<KeyCode, Boolean> inputMap = new HashMap<>();


  public InputSystem(){
    super();
    //register all of the available keys
    inputMap.put(KeyCode.UP, false);
    inputMap.put(KeyCode.DOWN, false);
    inputMap.put(KeyCode.LEFT, false);
    inputMap.put(KeyCode.RIGHT, false);
    inputMap.put(KeyCode.W, false);
    inputMap.put(KeyCode.A, false);
    inputMap.put(KeyCode.S, false);
    inputMap.put(KeyCode.SPACE, false);
    inputMap.put(KeyCode.C, false);


    setTag(SystemEnum.Input);

  }

  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){
    super.initialize(el, gameObjectMap);

    Element inputMap = (Element) el.getElementsByTagName("InputMap").item(0);
    String inputContents = inputMap.getTextContent();
    inputContents = inputContents.substring(1, inputContents.length() - 1);

    String[] contents = inputContents.split("\\s*,\\s*");

    for (int i = 0; i < contents.length; i++){
      String[] inputPair = contents[i].split("\\s*=\\s*");
      boolean inputBoolean = Boolean.parseBoolean(inputPair[1]);
      KeyCode inputCode = KeyCode.getKeyCode(inputPair[0]);

      this.inputMap.put(inputCode, inputBoolean);
    }





  }


  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    midEl.setAttribute("id", "InputSystem");
    Document doc = el.getOwnerDocument();
    Element inputs = doc.createElement("InputMap");
    inputs.setTextContent(inputMap.toString());
    midEl.appendChild(inputs);
    return midEl;
  }

  @Override
  public void onDraw(GraphicsContext g) {

  }



  @Override
  public void onTick(long nanos) {
    for (GameObject obj: objList){
      obj.onTick(nanos);
    }
  }

  public void onKeyPressed(KeyEvent e){
    registerKey(e.getCode(), true);
    for (GameObject obj: objList){
      obj.onKeyPressed(e, inputMap);
    }

  }

  public void onKeyReleased(KeyEvent e){
    registerKey(e.getCode(), false);
    for (GameObject obj: objList){
      obj.onKeyReleased(e, inputMap);
    }

  }

  public void registerKey(KeyCode kc, Boolean pressed){
    inputMap.put(kc, pressed);
  }


}
