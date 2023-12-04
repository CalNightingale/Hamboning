package engine.Systems;

import engine.Components.CompEnum;
import engine.Components.DecayComponent;
import engine.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class DecaySystem extends BaseSystem<DecaySystem>{
    public DecaySystem(){
        super();
        setTag(SystemEnum.Decay);
    }
    @Override
    public void onDraw(GraphicsContext g) {

    }

    @Override
    public void onTick(long nanos) {
        for (GameObject o : objList) {
            DecayComponent dc = o.getComponent(CompEnum.Decay);
            dc.onTick(nanos);
        }
    }
}
