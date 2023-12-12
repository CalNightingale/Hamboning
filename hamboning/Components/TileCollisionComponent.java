package hamboning.Components;

import engine.Components.CollisionComponent;
import engine.Components.CompEnum;
import engine.Components.TransformComponent;
import engine.GameObject;
import engine.GameWorld;
import engine.Shapes.Collision;
import engine.Shapes.Shape;
import hamboning.HamboningConstants;
import hamboning.HamboningWorld;
import org.w3c.dom.Element;

public class TileCollisionComponent extends CollisionComponent {

  public TileCollisionComponent(Shape s, GameObject o) {
    super(s, o);
  }

  public TileCollisionComponent(Element data, GameObject o, GameWorld gameWorld){
    super(data, o.tc);
  }

  @Override
  public void collide(Collision coll) {
    TransformComponent otherTC = coll.other.getTransform();
    //System.out.println("this static" + coll.thisShape.isStatic());
    //System.out.println("other static" + coll.otherShape.isStatic());

    if (coll.thisShape.isStatic()){
      this.o.tc.setPos(this.o.tc.getPosition().plus(coll.mtv.reflect()));
    }
    if (coll.thisShape.getTag() == "leaves"){ // can do this with everything

      if (coll.otherShape.getTag() == "mordecai") {
        this.o.getGW().addToRemovals(coll.other);
        HamboningWorld hw = (HamboningWorld) this.o.getGW();
        hw.addToScore(HamboningConstants.LEAF_SCORE_AMT);

      }
      // }
    }
    if (coll.thisShape.getTag() == "tables"){
      this.o.tc.setPos(this.o.tc.getPosition().plus(coll.mtv.sdiv(2).reflect()));
      otherTC.setPos(otherTC.getPosition().plus(coll.mtv.sdiv(2)));
    }

  }
}
