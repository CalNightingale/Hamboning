package hamboning.Components;

import engine.Components.CompEnum;
import engine.Components.MoveableComponent;
import engine.Components.MovementDir;
import engine.GameObject;
import engine.support.Vec2d;
import hamboning.HamboningConstants;

import java.util.LinkedList;

public class RigbyMoveComponent extends MoveableComponent {
    private final GameObject mordecai;
    private final LinkedList<Vec2d> pathToFollow;
    private final int tickDelay = 10;

    public RigbyMoveComponent(GameObject o, double moveAmount, GameObject mordecai) {
        super(o, moveAmount);
        this.mordecai = mordecai;
        this.pathToFollow = new LinkedList<>();
        System.out.println("adding " + mordecai.tc.getPosition());
        this.pathToFollow.add(mordecai.tc.getPosition());
    }

    @Override
    public void onTick(long nanos) {
        double DIST_THRESHOLD = 0.5;
        Vec2d currentMordecaiPosition = mordecai.tc.getPosition();
        // If mordecai is close enough to rigby to not move, skip (and clear path)
        if (currentMordecaiPosition.dist(this.tc.getPosition()) < DIST_THRESHOLD) {
            this.clearPath();
            return;
        }
        // If mordecai has moved, add his new location to the path to follow
        Vec2d lastMordecaiPos = pathToFollow.getLast();
        if (currentMordecaiPosition != lastMordecaiPos) {
            pathToFollow.add(currentMordecaiPosition);
        }
        // If rigby is far enough away, trigger movement
        if (pathToFollow.getLast().dist(this.tc.getPosition()) > DIST_THRESHOLD) {
            // Get the next position for Rigby to move towards
            Vec2d nextPosition = pathToFollow.getFirst();
            // Move Rigby towards the next position in the path
            if (moveRigbyTowards(nextPosition, nanos)) {
                // remove this path position if we reached it
                pathToFollow.poll();
            }
        }
    }

    public void clearPath() {
        this.pathToFollow.clear();
        pathToFollow.add(mordecai.tc.getPosition());
    }

    private boolean moveRigbyTowards(Vec2d targetPosition, long nanosSincePreviousTick) {
        Vec2d rigbyPosition = this.tc.getPosition();

        double dist = rigbyPosition.dist(targetPosition);
        if (dist > 0) {
            Vec2d moveDir = targetPosition.minus(rigbyPosition).normalize();
            double moveAmt = moveSpeed * nanosSincePreviousTick / 1e9;
            Vec2d translationV = moveDir.smult(moveAmt);
            Vec2d teleportV = targetPosition.minus(rigbyPosition);
            // we can move to exactly where we want to go
            if (teleportV.mag() < translationV.mag()) {
                this.tc.setPos(targetPosition);
                return true;
            } else {
                this.tc.setPos(this.tc.getPosition().plus(translationV));
            }
            return false;
        }
        return true;
    }
}
