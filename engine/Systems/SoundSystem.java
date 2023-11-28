package engine.Systems;

import engine.Components.CompEnum;
import engine.Components.SoundComponent;
import engine.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class SoundSystem extends BaseSystem<SoundSystem>{
    public SoundSystem(){
        super();
        setTag(SystemEnum.Sound);
    }
    @Override
    public void onDraw(GraphicsContext g) {

    }

    @Override
    public void onTick(long nanos) {
        for (GameObject o : objList) {
            SoundComponent sc = o.getComponent(CompEnum.Sound);
            if (sc == null) {
                throw new RuntimeException("Object with no sound component in SoundSystem: " + o);
            }
            sc.onTick(nanos);
        }
    }
}
