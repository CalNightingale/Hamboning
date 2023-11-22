package engine.Components;

import engine.support.Vec2d;

public enum MovementDir {
  UP{
    public Vec2d getDir(){
      return new Vec2d(0, -1);
    }
  }, DOWN{
    public Vec2d getDir(){
      return new Vec2d(0, 1);
    }
  }, LEFT{
    public Vec2d getDir(){
      return new Vec2d(-1, 0);
    }
  }, RIGHT{
    public Vec2d getDir(){
      return new Vec2d(1, 0);
    }
  }, NONE{
    public Vec2d getDir(){
      return new Vec2d(0, 0);
    }
  }, UPLEFT{
    public Vec2d getDir(){
      return new Vec2d(-1, -1).normalize();
    }
  }, UPRIGHT{
    public Vec2d getDir(){
      return new Vec2d(1, -1).normalize();
    }
  }, DOWNLEFT{
    public Vec2d getDir(){
      return new Vec2d(-1, 1).normalize();
    }
  }, DOWNRIGHT{
    public Vec2d getDir(){
      return new Vec2d(1, 1).normalize();
    }
  };
  public abstract Vec2d getDir();

};
