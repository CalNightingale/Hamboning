package engine.Components;

import engine.GameObject;
import engine.GameWorld;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.w3c.dom.Element;

public class ComponentRegistry {
  private final Map<String, ComponentFactory> customComponentFactories = new HashMap<>();

  @FunctionalInterface
  public interface ComponentFactory {
    Component create(Element element, GameObject go, GameWorld gw);

  }


  public void registerComponentFactory(String componentId, ComponentFactory factory) {
    customComponentFactories.put(componentId, factory);
  }

  public Component createComponent(String componentId, Element data, GameObject gameObject, GameWorld gameWorld) {
    if (customComponentFactories.containsKey(componentId)) {
      return customComponentFactories.get(componentId).create(data, gameObject, gameWorld);
    }
    throw new IllegalArgumentException("Component factory for " + componentId + " not found");
  }

}
