package engine;

import engine.Systems.BaseSystem;
import engine.Systems.SystemEnum;
import engine.support.Vec2d;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SaveLoad {
  private GameWorld gw;

  public SaveLoad(){


  }


  public void load(String filePath, GameWorld gameWorld){

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      Document doc = docBuilder.parse(filePath);
      doc.getDocumentElement().normalize();

      Node vpNode = doc.getElementsByTagName("Viewport").item(0);
      gameWorld.getVP().load((Element) vpNode);

      HashMap<UUID, GameObject> gameObjectMap = new HashMap<>();

      Element objects = (Element) doc.getElementsByTagName("GameObjects").item(0);
      NodeList objList = objects.getElementsByTagName("Object");
      for (int i = 0; i < objList.getLength(); i++) {
        Node node = objList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
          Element element = (Element) node;
          GameObject gameObject = new GameObject(element, gameWorld);
          gameWorld.addObjects(gameObject);
          gameObjectMap.put(gameObject.getId(), gameObject);

        }
      }

      NodeList sysList = doc.getElementsByTagName("System");
      HashMap<SystemEnum, BaseSystem<? extends BaseSystem>> gwSysList = gameWorld.systems;

      for (int i = 0; i < sysList.getLength(); i++) {
        Node node = sysList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE){
          Element element = (Element) node;
          String systemId = element.getAttribute("id");
          try {
            if (systemId.length() > 1){
              SystemEnum sysEnum = SystemEnum.valueOf(systemId.replace("System",""));
              if (sysEnum != null){ //should never be null
                BaseSystem<?> system = gwSysList.get(sysEnum);
                if (system != null){
                  system.initialize(element, gameObjectMap);
                }

              }

            }

          } catch (IllegalArgumentException e){
            System.out.println("exception");
          }




        }
      }



    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void saveGameState(String filePath, GameWorld gameWorld) throws Exception {
    this.gw = gameWorld;
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

    // Create the root of the XML
    Document document = documentBuilder.newDocument();
    Element root = document.createElement("GameWorld");
    document.appendChild(root);

    // Serialize the game world size
    Element gwSizeElement = document.createElement("GameWorldSize");
    gwSizeElement.setAttribute("width", String.valueOf(gameWorld.getGWSize().x));
    gwSizeElement.setAttribute("height", String.valueOf(gameWorld.getGWSize().y));
    root.appendChild(gwSizeElement);

    Element viewPort = document.createElement("Viewport");
    viewPort = gameWorld.getVP().serialize(viewPort);
    root.appendChild(viewPort);


    // Serialize game objects
    Element gameObjectsElement = document.createElement("GameObjects");
    root.appendChild(gameObjectsElement);

    for (GameObject gameObject : gameWorld.oList) { // Access should be changed to a getter method in production code.
      Element gameObjectElement = document.createElement("Object");
      gameObjectElement = gameObject.serialize(gameObjectElement); // Ensure GameObject has a serialize method
      gameObjectsElement.appendChild(gameObjectElement);
    }

    // Serialize systems - this may be tricky and system-dependent, here's a simplistic approach
    Element systemsElement = document.createElement("Systems");
    root.appendChild(systemsElement);
    for (SystemEnum systemEnum : gameWorld.systems.keySet()) { // Access should be changed to a getter method in production code.
      BaseSystem<?> system = gameWorld.systems.get(systemEnum);

      Element systemElement = document.createElement("System");
      systemElement = system.serialize(systemElement); // Each system should implement a serialize method
      if (systemElement != null){
        systemsElement.appendChild(systemElement);
      }

    }

    // Write the content into XML file
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");

    // Set the amount of indentation (e.g., 2 spaces)
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

    DOMSource source = new DOMSource(document);

    StreamResult result = new StreamResult(new File(filePath));
    transformer.transform(source, result);
  }


  public GameObject createGameObject(Element element){

    Vec2d pos = new Vec2d(Double.parseDouble(element.getAttribute("posX")), Double.parseDouble(element.getAttribute("posY")));
    Vec2d siz = new Vec2d(Double.parseDouble(element.getAttribute("sizX")), Double.parseDouble(element.getAttribute("sizY")));
    GameObject go = new GameObject(pos, siz, this.gw);
    return go;
  }

}
