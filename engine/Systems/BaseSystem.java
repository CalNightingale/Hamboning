package engine.Systems;

import engine.GameObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BaseSystem<T extends BaseSystem<T>> {
  public List<GameObject> objList = new ArrayList<>();
  private SystemEnum tag;
  public BaseSystem(){

  }


  public void addObject(GameObject o){
    this.objList.add(o);
  }

  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){

    Element objList = (Element) el.getElementsByTagName("ObjList").item(0);

    String[] UUIDS = objList.getTextContent().split("\\s*,\\s*");
    for (String uuidString : UUIDS) {
      try {
        if (uuidString.length() > 1){
          UUID uuid = UUID.fromString(uuidString);
          GameObject obj = gameObjectMap.get(uuid);
          if (obj != null){
            addObject(obj);
          }

        }


      } catch (IllegalArgumentException e) {
        System.err.println("Invalid UUID string: " + uuidString);
      }
    }
    //would fill in here for oList
  }

  public Element serialize(Element el){
    // Check if the layer is not null
      // Use a StringBuilder to construct the content string
      StringBuilder sb = new StringBuilder();
      Document doc = el.getOwnerDocument();
      Element objListEl = doc.createElement("ObjList");

      // Append each GameObject's hash code to the StringBuilder
      for (GameObject gameObject : objList) {
        UUID id = gameObject.getId();
        sb.append(id).append(","); // Separate hash codes with a comma
      }

      // Remove the trailing comma and space if necessary
      if (sb.length() > 0) {
        sb.setLength(sb.length() - 1);
      }

      // Set the text content of the Layer element to the StringBuilder's content
      objListEl.setTextContent(sb.toString());
      el.appendChild(objListEl);
      return el;
  }

  public void removeObject(GameObject o){
    if (objList.contains(o)){
      objList.remove(o);
    }
  }


  public void setTag(SystemEnum se){this.tag = se;}

  public SystemEnum getTag(){return this.tag;}

  public abstract void onDraw(GraphicsContext g);

  public abstract void onTick(long nanos);

}