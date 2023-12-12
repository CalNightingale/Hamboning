package engine;
import engine.Components.*;
import engine.Shapes.AAB;
import engine.Shapes.Collision;
import engine.Systems.BaseSystem;
import engine.Systems.SystemEnum;
import engine.UILibrary.UIRectangle;
import engine.support.Vec2d;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Affine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GameObject {
  private HashMap<CompEnum, Component> compList = new HashMap<>();
  public TransformComponent tc;
  private Vec2d screenSize;
  private GameWorld gw;
  private UUID id;



  public GameObject(Vec2d position, Vec2d size, GameWorld gw){
    this.tc = new TransformComponent(position, size, this);
    this.gw = gw;
    this.id = UUID.randomUUID();
  }


  public GameObject(Element data, GameWorld gw){
    this.gw = gw;

    Vec2d pos = new Vec2d(data.getAttribute("position"));
    Vec2d size = new Vec2d(data.getAttribute("size"));

    this.tc = new TransformComponent(pos, size, this);
    this.id = UUID.fromString(data.getAttribute("id"));

    NodeList components = data.getElementsByTagName("Component");
    for (int i = 0; i < components.getLength(); i++) {
      Node node = components.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE){
        Element element = (Element) node;
        String componentID = element.getAttribute("id");
        Component newComponent;
        try {
          CompEnum compEnum = CompEnum.valueOf(componentID.replace("Component",""));
          if (compEnum == CompEnum.Collision){
            newComponent = new CollisionComponent(element, this);
          } else if (compEnum == CompEnum.Moveable) {
            newComponent = new MoveableComponent(element, this);
          }  else if (compEnum == CompEnum.Projectile) {
            newComponent = new ProjectileComponent(element, this);
          }else {
            newComponent = compEnum.createComponent(element, this.tc, gw.getScreenSize());
          }

        } catch (IllegalArgumentException e){
          newComponent = gw.cr.createComponent(componentID, element, this, gw);
        }

        if (newComponent != null){
          this.addComponent(newComponent);

        }

        }

      }
    }

  public UUID getId() {
    return id;
  }

  public Element serialize(Element el){
    Document doc = el.getOwnerDocument();
    el.setAttribute("position", this.tc.getPosition().toString());
    el.setAttribute("size", this.tc.getSize().toString());
    el.setAttribute("id", this.id.toString());

    for (Map.Entry<CompEnum, Component> entry : compList.entrySet()) {
      Element compEl = doc.createElement("Component");
      compEl = entry.getValue().serialize(compEl);
      if (compEl != null){
        el.appendChild(compEl);
      }

    }








    return el;
  }

  public<T extends Component<T>> void addComponent(Component c){
    compList.put(c.getTag(), c);
  }

  public <T extends Component<T>> void removeComponent(Component c){

  }

  public GameWorld getGW(){return this.gw;}

  public HashMap<CompEnum, Component> getList(){return this.compList;}

  public <T extends Component<T>> T getComponent(CompEnum tag){
    if (compList.containsKey(tag)){
      return (T) compList.get(tag);
    }
    return null;
  }

  public Component getComp(CompEnum tag){
    if (compList.containsKey(tag)){
      return compList.get(tag);
    }
    return null;
  }

  public TransformComponent getTransform(){
    return this.tc;

  }

  public void setTransform(Affine transform){
  }


  public void onTick(long nanos){
    InputComponent ic = this.getComponent(CompEnum.Input);
    if (ic != null){
      ic.onTick(nanos);
    }
    MoveableComponent mc = this.getComponent(CompEnum.Moveable);
    if (mc != null){
      mc.onTick(nanos);
    }

    CenterComponent cc = this.getComponent(CompEnum.Center);
    if (cc != null){
      cc.onTick(nanos);
    }

    SpriteComponent sc = this.getComponent(CompEnum.Sprite);
    if (sc != null){
      sc.onTick(nanos);
    }

    DecayComponent dc = this.getComponent(CompEnum.Decay);
    if (dc != null){
      dc.onTick(nanos);
    }

    PathfindingComponent pc = this.getComponent(CompEnum.Pathfinding);
    if (pc != null){
      pc.onTick(nanos);
    }

    PhysicsComponent physc = this.getComponent(CompEnum.Physics);
    if (physc != null){
      physc.onTick(nanos);
    }
  }

  public void onResize(Vec2d newSize, double aspect){
    GraphicsComponent gc = this.getComponent(CompEnum.Graphics);
    if (gc != null){
      gc.onResize(newSize, aspect);
    }

  }


  public void onDraw(GraphicsContext g){
    GraphicsComponent gc = this.getComponent(CompEnum.Graphics);
    if (gc != null){
      gc.onDraw(g);
    }
  }

  public void onMouseDragged(Vec2d point){
    DraggableComponent dc = this.getComponent(CompEnum.Draggable);
      if (dc != null){
        dc.onMouseDragged(point);
      }


  }



  public void onMousePressed(Vec2d point){
    DraggableComponent dc = this.getComponent(CompEnum.Draggable);
      if (dc != null){
        dc.onMousePressed(point);
      }


  }

  public void onKeyPressed(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    InputComponent ic = this.getComponent(CompEnum.Input);
    if (ic != null){
      ic.onKeyPressed(e, inputMap);
    }
  }

  public void onKeyReleased(KeyEvent e, HashMap<KeyCode, Boolean> inputMap){
    InputComponent ic = this.getComponent(CompEnum.Input);
    if (ic != null){
      ic.onKeyReleased(e, inputMap);
    }

  }

  public void handleCollision(Collision coll){
    CollisionComponent cc = this.getComponent(CompEnum.Collision);
    cc.collide(coll);

    PhysicsComponent thisPC = this.getComponent(CompEnum.Physics);//platform
    if (thisPC != null){
      thisPC.collide(coll);
    }

  }

//  public void onKeyPressed

  public void initialize(Element element){

  }



  public void onLateTick(){}




}
