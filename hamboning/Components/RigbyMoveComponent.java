package hamboning.Components;

import engine.Components.CompEnum;
import engine.Components.MoveableComponent;
import engine.Components.MovementDir;
import engine.GameObject;
import engine.support.Vec2d;

import java.util.LinkedList;

public class RigbyMoveComponent extends MoveableComponent {
    private final GameObject mordecai;
    private final LinkedList<Vec2d> pathToFollow;
    private final int tickDelay = 10;

    public RigbyMoveComponent(GameObject o, double moveAmount, GameObject mordecai) {
        super(o, moveAmount);
        this.mordecai = mordecai;
        this.pathToFollow = new LinkedList<>();
    }

    @Override
    public void onTick(long nanos) {
        // Record Mordecai's current position
        Vec2d currentMordecaiPosition = mordecai.tc.getPosition();
        if (!pathToFollow.isEmpty()) {
            Vec2d lastMordecaiPos = pathToFollow.getLast();
            if (currentMordecaiPosition != lastMordecaiPos) {
                pathToFollow.add(currentMordecaiPosition);
            }
        } else {
            pathToFollow.add(currentMordecaiPosition);
        }

        // Check if Rigby needs to start moving
        if (pathToFollow.size() > tickDelay) {
            // Get the next position for Rigby to move towards
            Vec2d nextPosition = pathToFollow.poll();

            // Move Rigby towards the next position in the path
            moveRigbyTowards(nextPosition, nanos);
        }
    }

    private void moveRigbyTowards(Vec2d targetPosition, long nanosSincePreviousTick) {
        Vec2d rigbyPosition = this.tc.getPosition();

        double dist = rigbyPosition.dist(targetPosition);
        if (dist > 0) {
            Vec2d moveDir = targetPosition.minus(rigbyPosition).normalize();
            double moveAmt = moveSpeed * nanosSincePreviousTick / 1e9;
            this.tc.setPos(this.tc.getPosition().plus(moveDir.smult(moveAmt)));
        }
    }
}
