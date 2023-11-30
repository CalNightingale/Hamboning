package hamboning.Components;

import engine.Components.MoveableComponent;
import engine.GameObject;

public class CartMoveableComponent extends MoveableComponent {
    private final GameObject character;
    public CartMoveableComponent(GameObject cart, double moveAmount, GameObject character) {
        super(cart, moveAmount);
        this.character = character;
    }

    @Override
    public void onTick(long nanos) {
        super.onTick(nanos);
        // if moving, set character position to cart position
        if (this.getNumDirs() > 0) {
            character.tc.setPos(this.o.tc.getPosition());
        }
    }
}
