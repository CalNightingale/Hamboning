package engine.Systems;


import engine.Components.TransformComponent;
import engine.GameObject;
import engine.support.Vec2d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GraphicsSystem extends BaseSystem<GraphicsSystem>{
  public List<GameObject>[] oLayers = new ArrayList[3];
  public GraphicsSystem(){
    super();
    setTag(SystemEnum.Graphics);
    for (int i = 0; i < 3; i++){
      oLayers[i] = new ArrayList<GameObject>();
    }
  }

  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap) {
    NodeList layers = el.getElementsByTagName("Layer");
    for (int i = 0; i < 3; i++) {
      oLayers[i] = new ArrayList<GameObject>();
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
              oLayers[i].add(obj);
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
    el.setAttribute("id", "GraphicsSystem");

    Element layersElement = doc.createElement("Layers");
    for (int i = 0; i < oLayers.length; i++) {
      // Create an element for the layer
      Element layerElement = doc.createElement("Layer");
      layerElement.setAttribute("id", String.valueOf(i)); // Set the layer ID as an attribute

      // Check if the layer is not null
      if (oLayers[i] != null) {
        // Use a StringBuilder to construct the content string
        StringBuilder sb = new StringBuilder();

        // Append each GameObject's hash code to the StringBuilder
        for (GameObject gameObject : oLayers[i]) {
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
    oLayers[0].add(o);
  }

  public void addObjectToLayer(GameObject o, int layerIndex) {
    if (layerIndex >= 0 && layerIndex < oLayers.length) {
      oLayers[layerIndex].add(o);
    } else {
      System.out.println("bad layer");
    }
  }

  @Override
  public void removeObject(GameObject o){
    for (List<GameObject> layer : oLayers) {
      if (layer != null) {
        if (layer.contains(o)){
          layer.remove(o);
          break;

        }
      }
    }

  }

  @Override
  public void onDraw(GraphicsContext g) {
    for (List<GameObject> objList: oLayers){
      for(GameObject o: objList){
        o.onDraw(g);
      }
    }


  }

  @Override
  public void onTick(long nanos) {


  }



  public void onResize(Vec2d newSize, double aspect){
    for (List<GameObject> objList: oLayers){
      for(GameObject o: objList){
        o.onResize(newSize, aspect);
      }
    }

  }

  public boolean isInside(Vec2d point, TransformComponent tc){

    return point.x >= tc.getPosition().x && point.x <= tc.getPosition().x + tc.getSize().x &&
        point.y >= tc.getPosition().y && point.y <= tc.getPosition().y + tc.getSize().y;

  }

  public void onMouseDragged(Vec2d point){
    outerLoop:
    for (int i = oLayers.length - 1; i >= 0; i--) {
      for (int j = oLayers[i].size() - 1; j >= 0; j--) {
        if (isInside(point, oLayers[i].get(j).getTransform())){
          oLayers[i].get(j).onMouseDragged(point);
          break outerLoop;
        }
      }
    }
  }


  public void onMousePressed(Vec2d point){
    outerLoop:
    for (int i = oLayers.length - 1; i >= 0; i--) {
      for (int j = oLayers[i].size() - 1; j >= 0; j--) {
        if (isInside(point, oLayers[i].get(j).getTransform())){
          oLayers[i].get(j).onMousePressed(point);
          break outerLoop;
        }
      }
    }

  }
}
