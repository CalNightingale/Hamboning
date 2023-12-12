package engine.Systems;

import engine.Components.CompEnum;
import engine.Components.SoundComponent;
import engine.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class SoundSystem extends BaseSystem<SoundSystem>{
    private boolean muted;
    public SoundSystem(){
        super();
        setTag(SystemEnum.Sound);
        muted = false;
    }
    @Override
    public void onDraw(GraphicsContext g) {

    }

    public void toggleMute() {
        // flip whether muted or not
        muted = !muted;
        for (GameObject o : objList) {
            SoundComponent sc = o.getComponent(CompEnum.Sound);
            sc.toggle(muted);
        }
    }

    @Override
    public void onTick(long nanos) {
        if (muted) return;
        for (GameObject o : objList) {
            SoundComponent sc = o.getComponent(CompEnum.Sound);
            if (sc == null) {
                throw new RuntimeException("Object with no sound component in SoundSystem: " + o);
            }
            sc.onTick(nanos);
        }
    }
}
