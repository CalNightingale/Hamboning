package engine;


import engine.Components.CompEnum;
import engine.Components.MoveableComponent;
import engine.Components.MovementDir;
import engine.support.Vec2d;

public class Projectile extends GameObject{

  public MovementDir currDir;
  public Projectile(Vec2d position, Vec2d size, GameWorld gw, MovementDir currDir) {
    super(position, size, gw);
    this.currDir = currDir;
  }

  @Override
  public void onTick(long nanos){
    super.onTick(nanos);
    MoveableComponent mc = this.getComponent(CompEnum.Moveable);
    if (mc != null){
      mc.setDir(true, this.currDir);
    }
  }



}
