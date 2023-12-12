package engine.Systems;
import engine.Shapes.Collision;
import engine.support.Vec2d;
import engine.Components.CollisionComponent;
import engine.Components.CompEnum;
import engine.GameObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CollisionSystem extends BaseSystem<CollisionSystem>{
  public List<GameObject>[] collideLayers = new ArrayList[4];
  public CollisionSystem(){
    this.setTag(SystemEnum.Collision);
    for (int i = 0; i < 4; i++){
      collideLayers[i] = new ArrayList<GameObject>();
    }

  }

  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){
    this.setTag(SystemEnum.Collision);
    NodeList layers = el.getElementsByTagName("Layer");
    for (int i = 0; i < 4; i++) {
      collideLayers[i] = new ArrayList<GameObject>();
      Element layerEl = (Element) layers.item(i);
      String layerContents = layerEl.getTextContent();
      if (layerContents != null
          && layerContents.length() > 1) { //not sure what the result is of an empty list
        String[] UUIDS = layerContents.split("\\s*,\\s*");
        for (String uuidString : UUIDS) {
          try {
            UUID uuid = UUID.fromString(uuidString);
            GameObject obj = gameObjectMap.get(uuid);
            if (obj != null) {
              collideLayers[i].add(obj);
            }

          } catch (IllegalArgumentException e) {
            System.err.println("Invalid UUID string: " + uuidString);
          }
        }
      }
    }
  }

  @Override
  public Element serialize(Element el){
    Document doc = el.getOwnerDocument();
    el.setAttribute("id", "CollisionSystem");

    Element layersElement = doc.createElement("Layers");
    for (int i = 0; i < collideLayers.length; i++) {
      // Create an element for the layer
      Element layerElement = doc.createElement("Layer");
      layerElement.setAttribute("id", String.valueOf(i)); // Set the layer ID as an attribute

      // Check if the layer is not null
      if (collideLayers[i] != null) {
        // Use a StringBuilder to construct the content string
        StringBuilder sb = new StringBuilder();

        // Append each GameObject's hash code to the StringBuilder
        for (GameObject gameObject : collideLayers[i]) {
          UUID id = gameObject.getId();
          sb.append(id).append(","); // Separate hash codes with a comma
        }

        // Remove the trailing comma and space if necessary
        if (sb.length() > 0) {
          sb.setLength(sb.length() - 1);
        }

        // Set the text content of the Layer element to the StringBuilder's content
        layerElement.setTextContent(sb.toString());
      }
      layersElement.appendChild(layerElement);
    }
    el.appendChild(layersElement);
    return el;
  }

  @Override
  public void addObject(GameObject o){
    collideLayers[0].add(o);
  }


  public Integer getObjectLayer(GameObject o){
    for (int i = 0; i < collideLayers.length; i++) {
      if (collideLayers[i].contains(o)){
        return i;
      }
    }
    return null;
  }
  @Override
  public void removeObject(GameObject o){
    for (List<GameObject> layer : collideLayers) {
      if (layer != null && layer.remove(o)) {
        // If the object was found and removed, break out of the loop.
        break;
      }
    }
  }

  public void addObjectToLayer(GameObject o, int layerIndex) {
    if (layerIndex >= 0 && layerIndex < collideLayers.length) {
      collideLayers[layerIndex].add(o);
    } else {
      System.out.println("bad layer");
    }
  }

  public void printLayers() {
    for (int i = 0; i < collideLayers.length; i++) {
      List<GameObject> layer = collideLayers[i];
      System.out.println("Layer " + i + ":");

      if (layer == null || layer.isEmpty()) {
        System.out.println("  [empty]");
      } else {
        for (GameObject obj : layer) {
          System.out.println("  " + obj); // Calls obj.toString() implicitly
        }
      }
    }
  }


  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onTick(long nanos) {
    // Check collisions within each layer, skipping layer 0
    for (int layerIndex = 1; layerIndex < collideLayers.length; layerIndex++){
      List<GameObject> layer = collideLayers[layerIndex];
      if (layer == null) continue;

      for (int i = 0; i < layer.size(); i++){

        GameObject currObject = layer.get(i);
        CollisionComponent ca = currObject.getComponent(CompEnum.Collision);
        for (int j = i + 1; j < layer.size(); j++){
          GameObject otherObject = layer.get(j);
          CollisionComponent cb = otherObject.getComponent(CompEnum.Collision);
          Vec2d collisionVec = ca.isColliding(cb);
          if (collisionVec != null){
            if (collisionVec.mag() != 0){

              Collision collision = new Collision(currObject, otherObject, collisionVec, ca.getShape(), cb.getShape());
              currObject.handleCollision(collision);

            }
            //System.out.println("is colliding within the same layer!");
          }
        }
      }
    }

    // Check collisions between objects in layer 0 and objects in all other layers
    List<GameObject> baseLayer = collideLayers[0];
    for (int layerIndex = 1; layerIndex < collideLayers.length; layerIndex++) {
      List<GameObject> otherLayer = collideLayers[layerIndex];
      for (GameObject baseObject : baseLayer) {
        CollisionComponent baseComponent = baseObject.getComponent(CompEnum.Collision);
        for (GameObject otherObject : otherLayer) {
          CollisionComponent otherComponent = otherObject.getComponent(CompEnum.Collision);
          //System.out.println(baseComponent.getShape().getCenter());
          //System.out.println(otherComponent.getShape().getCenter());

          Vec2d collisionVec = baseComponent.isColliding(otherComponent);
          if (collisionVec != null) {
            if(collisionVec.mag() != 0){
              Collision collision = new Collision(otherObject, baseObject, collisionVec, baseComponent.getShape(), otherComponent.getShape());
              otherObject.handleCollision(collision);
              //System.out.println("currObject" + baseObject.getClass().getName() + baseObject.getTransform().getPosition());
              //System.out.println("otherObject" + otherObject.getClass().getName() + otherObject.getTransform().getPosition());

            }
            //System.out.println("is colliding with layer 0!");

          }
        }
      }
    }
  }




  public void onMouseReleased(MouseEvent e){
    //change to onTick


  }






}
