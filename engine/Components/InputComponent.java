package engine.Components;

import java.util.HashMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Element;

public class InputComponent implements Component<InputComponent>{
  private HashMap<KeyCode, Boolean> currInput = new HashMap<>();
  public InputComponent(){}

  public InputComponent(Element data, TransformComponent tc){
    String inputContents = data.getTextContent();

    inputContents = inputContents.substring(1, inputContents.length() - 1);

    if (inputContents.length() > 1){
      String[] contents = inputContents.split("\\s*,\\s*");

      for (int i = 0; i < contents.length; i++){
        String[] inputPair = contents[i].split("\\s*=\\s*");
        boolean inputBoolean = Boolean.parseBoolean(inputPair[1]);
        KeyCode inputCode = KeyCode.getKeyCode(inputPair[0]);

        currInput.put(inputCode, inputBoolean);
      }

    }



  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "InputComponent");
    el.setTextContent(currInput.toString());
    return el;
  }


  //implement on key released type stuff as well


  @Override
  public void onTick(long nanos) {

  }

  public void onKeyPressed(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    this.currInput = inputMap;
  };

  public void onKeyReleased(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    this.currInput = inputMap;
  };

  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onLateTick() {

  }



  @Override
  public CompEnum getTag() {
    return CompEnum.Input;
  }
}
