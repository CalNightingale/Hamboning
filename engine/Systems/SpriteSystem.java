package engine.Systems;

import engine.Components.CompEnum;
import engine.Components.SpriteComponent;
import engine.GameObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SpriteSystem extends BaseSystem<SpriteSystem>{
  private HashMap<GameObject, Image> sprites = new HashMap<>();
  private HashMap<Image, String> tempmap = new HashMap<>();
  private HashMap<String, Image> temp2 = new HashMap<>();
  public SpriteSystem(){
    super();
    setTag(SystemEnum.Sprite);

  }


  @Override
  public void initialize(Element el, HashMap<UUID, GameObject> gameObjectMap){
    super.initialize(el, gameObjectMap);
    //would fill in here for oList
    NodeList spriteRootNL = el.getElementsByTagName("Sprites");
    Element spriteRoot = (Element) spriteRootNL.item(0);

    NodeList sprites = spriteRoot.getElementsByTagName("Sprite");

    for (int i = 0; i < sprites.getLength(); i++){
      Element objEl = (Element) sprites.item(i);
      String objID = objEl.getAttribute("id");
      System.out.println(objID + "spriteSystem");
      UUID uuid = UUID.fromString(objID);
      GameObject obj = gameObjectMap.get(uuid);
      String imPath = objEl.getTextContent();

      loadSprite(obj, imPath); //here is where it's not working



    }

  }


  public void registerImage(String key, String filePath){
    System.out.println("registering image");
    Image spriteImage = new Image(filePath);
    this.tempmap.put(spriteImage, key);
    this.temp2.put(key, spriteImage);
  }

  public Image getImage(String key){
    return temp2.get(key);
  }
  @Override
  public Element serialize(Element el){

    Element midEl = super.serialize(el);
    midEl.setAttribute("id", "SpriteSystem");
    Document doc = el.getOwnerDocument();

    Element spriteMap = doc.createElement("Sprites");

    for (Map.Entry<GameObject, Image> sprite: sprites.entrySet()){
      Element gameObjectElement = doc.createElement("Sprite");
      gameObjectElement.setAttribute("id", String.valueOf(sprite.getKey().getId()));

      gameObjectElement.setTextContent(tempmap.get(sprite.getValue()));
    }
    midEl.appendChild(spriteMap);
    return midEl;

  }


  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onTick(long nanos) {

  }

//  public Image getImage(String key){
//    return this.tempmap.get(key);
//  }

  public void addAndLoad(GameObject o, String filePath){
    super.addObject(o);
    this.loadSprite(o, filePath);
  }

//  public void registerImage(String key, String filePath){
//    Image spriteImage = new Image(filePath);
//    this.tempmap.put(key, spriteImage);
//  }

  public Image loadSprite(GameObject o, String filePath){
    Image spriteImage = new Image(filePath);
    this.sprites.put(o, spriteImage);
    SpriteComponent sc = o.getComponent(CompEnum.Sprite); //make sure to set image on load
    this.tempmap.put(spriteImage, filePath);
    sc.setImage(spriteImage);
    return spriteImage;
  }

  public void loadSprite(GameObject o, Image im){
    this.sprites.put(o, im);
    SpriteComponent sc = o.getComponent(CompEnum.Sprite);
    sc.setImage(im);

  }

}
