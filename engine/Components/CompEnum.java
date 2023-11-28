package engine.Components;

import engine.GameObject;
import engine.support.Vec2d;
import java.nio.file.Path;
import org.w3c.dom.Element;

public enum CompEnum {

  Graphics {
    public GraphicsComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new GraphicsComponent(data, tc, ss);
    }
  },
  Draggable {
    public DraggableComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new DraggableComponent(data, tc);
    }

  }, Clickable{
    public ClickableComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new ClickableComponent(data, tc);
    }

  }, Collision {
    public CollisionComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new CollisionComponent(data, tc);
    }

  }, Input {
    public InputComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new InputComponent(data, tc);
    }

  }, Moveable {
    public Component<MoveableComponent> createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new MoveableComponent(data, tc);
    }

  }, Center {
    public CenterComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new CenterComponent(data, tc);
    }

  }, Projectile {
    public ProjectileComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new ProjectileComponent(data, tc);
    }

  }, Pathfinding {
    public PathfindingComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new PathfindingComponent(data, tc);
    }

  }, Physics {
    public PhysicsComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new PhysicsComponent(data, tc);
    }

  }, Sprite {
    public SpriteComponent createComponent(Element data, TransformComponent tc, Vec2d ss) {
      return new SpriteComponent(data, tc, ss);
    }

  }, Decay {
    public DecayComponent createComponent(Element data, TransformComponent tc, Vec2d ss){
      return new DecayComponent(data, tc, ss);
    }
  }, Sound {
    public SoundComponent createComponent(Element data, TransformComponent tc, Vec2d ss){
      return new SoundComponent(data, tc, ss);
    }
  };


 public abstract Component createComponent(Element element, TransformComponent tc, Vec2d ss);
}

