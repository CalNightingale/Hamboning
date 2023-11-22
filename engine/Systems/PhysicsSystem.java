package engine.Systems;

import engine.Components.CompEnum;
import engine.Components.PhysicsComponent;
import engine.GameObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PhysicsSystem extends BaseSystem<PhysicsSystem>{
  public HashMap<GameObject, Pair<Double, Boolean>> gravMap = new HashMap<>();

  public PhysicsSystem(){
    super();
    setTag(SystemEnum.Physics);

  }


  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){
    super.initialize(el, gameObjectMap);
    NodeList gameObjects = el.getElementsByTagName("GameObject");

    for (int i = 0; i < gameObjects.getLength(); i++){
      Element objEl = (Element) gameObjects.item(i);
      String objID = objEl.getAttribute("id");
      UUID uuid = UUID.fromString(objID);
      GameObject obj = gameObjectMap.get(uuid);


      String content = objEl.getTextContent();
      content = content.substring(1, content.length() - 1); // Remove the opening and closing brackets
      String[] contents = content.split("\\s*,\\s*");

      double gravAmount = Double.parseDouble(contents[0]);
      boolean isStatic = Boolean.parseBoolean(contents[1]);

      gravMap.put(obj, new Pair<>(gravAmount, isStatic));

    }





  }

  @Override
  public Element serialize(Element el){
    Element midEl = super.serialize(el);
    Document doc = el.getOwnerDocument();
    midEl.setAttribute("id", "PhysicsSystem");

    Element gravityMapElement = doc.createElement("GravityMap");

    // Iterate over the HashMap entries
    for (Map.Entry<GameObject, Pair<Double, Boolean>> entry : gravMap.entrySet()) {
      // Create an element for each GameObject entry
      Element gameObjectElement = doc.createElement("GameObject");

      // Use the hash code of the GameObject as an attribute (or a unique ID if you have one)
      gameObjectElement.setAttribute("id", String.valueOf(entry.getKey().getId()));

      // Retrieve the Pair values
      Pair<Double, Boolean> values = entry.getValue();
      Double gravityValue = values.getKey();
      Boolean isStatic = values.getValue();

      // Format the Pair as text content: (gravityValue, isStatic)
      String content = String.format("(%s, %s)", gravityValue, isStatic);
      gameObjectElement.setTextContent(content);

      // Append the GameObject element to the GravityMap element
      gravityMapElement.appendChild(gameObjectElement);
    }
    midEl.appendChild(gravityMapElement);

    return midEl;
  }

  public void addPhysicsObject(GameObject o, double grav, boolean isStatic){
    super.addObject(o);
    gravMap.put(o, new Pair(grav, isStatic));
  }

  @Override
  public void addObject(GameObject o){
    super.addObject(o);
    gravMap.put(o, new Pair(0, false));
  }

  @Override
  public void removeObject(GameObject o){
    super.removeObject(o);
    if (gravMap.containsKey(o)){
      gravMap.remove(o);
    }
  }

  @Override
  public void onDraw(GraphicsContext g) {

  }



  @Override
  public void onTick(long nanos) {
    //System.out.println("ticking in physSys");
    //apply gravity
    for (GameObject o: objList){
      PhysicsComponent pc = o.getComponent(CompEnum.Physics);


      if (pc != null && !gravMap.get(o).getValue()){ //if there is a physics component and object isn't static
        pc.applyGravity(gravMap.get(o).getKey());
      }
      o.onTick(nanos);
    }

  }
}
