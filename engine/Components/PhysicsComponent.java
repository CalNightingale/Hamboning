package engine.Components;

import engine.Shapes.Collision;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PhysicsComponent implements Component<PhysicsComponent>{
  public double mass;
  public Vec2d vel = new Vec2d(0);;
  private Vec2d impulse = new Vec2d(0);;
  private Vec2d force = new Vec2d(0);;
  private TransformComponent tc;
  private static final double PHYSICS_TIME_STEP = .02;  // 60 updates per second
  private double accumulatedTime = 0.0;
  public double restitution;
  private boolean isGrounded = false;

  private final double dragX = 0.01;

  public PhysicsComponent(double mass, double restitution, TransformComponent tc){
    this.mass = mass;
    this.tc = tc;
    this.restitution = restitution;


  }

  public PhysicsComponent(Element data, TransformComponent tc){
    this.tc = tc;
    this.mass = Double.parseDouble(data.getAttribute("mass"));
    this.restitution = Double.parseDouble(data.getAttribute("restitution"));
    this.force = new Vec2d(data.getAttribute("force"));
    this.vel = new Vec2d(data.getAttribute("velocity"));
    this.impulse = new Vec2d(data.getAttribute("impulse"));
    this.isGrounded = Boolean.parseBoolean(data.getAttribute("isGrounded"));

  }

  @Override
  public Element serialize(Element el) {
    el.setAttribute("id", "PhysicsComponent");
    el.setAttribute("mass", Double.toString(this.mass));
    el.setAttribute("restitution", Double.toString(this.restitution));
    el.setAttribute("velocity", this.vel.toString());
    el.setAttribute("force", this.force.toString());
    el.setAttribute("impulse", this.impulse.toString());
    el.setAttribute("isGrounded", Boolean.toString(isGrounded));


    return el;
  }



  public void applyGravity(double gravity){
      force = force.plus(new Vec2d(0, gravity * mass));


  }

  public void applyForce(Vec2d f) {
    force = force.plus(f);
  }

  public void applyImpulse(Vec2d p) {
    impulse = impulse.plus(p);
  }


  @Override
  public void onTick(long nanos) {
    //System.out.println(this.vel);
    //System.out.println("grounded" + this.isGrounded);
    if (Math.abs(this.vel.y) < 0.1) {
      this.isGrounded = true;
    } else {
      this.isGrounded = false;
    }



    double secTime = nanos / 1_000_000_000.0;
    accumulatedTime += secTime;
    if (accumulatedTime > 0.5){
      accumulatedTime =0;
    }
    if (accumulatedTime >= PHYSICS_TIME_STEP){
      fixedTimeStepUpdate(PHYSICS_TIME_STEP);
      accumulatedTime -= PHYSICS_TIME_STEP;
    }



  }

  private void fixedTimeStepUpdate(double deltaTime) {

    Vec2d addedVelo = (force.sdiv(((float) mass))).smult(deltaTime);
    vel = (vel.plus(addedVelo));
    Vec2d addedImpulse = impulse.sdiv((float) mass);

//    if (this.mass == 5.0){
//      System.out.println("velo" + vel);
//      System.out.println("added impulse" + impulse);
//      System.out.println("impulse" + impulse);
//    }

    if (Math.abs(vel.x) > dragX){
      if ( vel.x > 0){
        vel = new Vec2d(vel.x - dragX, vel.y);
      } else {
        vel = new Vec2d(vel.x + dragX, vel.y);
      }
    }
    vel = (vel.plus(addedImpulse));
    tc.setPos(tc.getPosition().plus(vel.smult(deltaTime)));
    force = new Vec2d(0);
    impulse = new Vec2d(0);
  }

  public boolean isGrounded(){return this.isGrounded;
  }

  public void collide(Collision coll){
    PhysicsComponent otherPC = coll.other.getComponent(CompEnum.Physics);//character
    TransformComponent otherTC = coll.other.getTransform();

    //System.out.println("obj location before" + coll.other.getTransform().getPosition());
    //System.out.println("obj size" + coll.other.getTransform().getSize());
    //System.out.println("mtv" + coll.mtv);
    //System.out.println("MTV" + coll.mtv);
    //System.out.println("other object velocity " + otherPC.vel); //this one is the character
    double impulseA = 0;
    double impulseB = 0;

    Vec2d velA = this.vel;
    Vec2d velB = otherPC.vel;

    double massA = this.mass;
    double massB = otherPC.mass;
    double fracA = massB / (massA + massB);
    double fracB = massA / (massA + massB);

    double restA = this.restitution;
    double restB = otherPC.restitution;

    Vec2d mtvNorm = coll.mtv.normalize();

    double COR = Math.sqrt(restA * restB);

    double uA = velA.dot(mtvNorm);
    double uB = velB.dot(mtvNorm);

    if (coll.thisShape.isStatic()){
      impulseB = massB * (uA - uB) * (1 + COR);
      fracA = 0;
      fracB = 1;
    } else if (coll.otherShape.isStatic()){
      impulseA = massA * (uB - uA) * (1 + COR);
      fracA = 1;
      fracB = 0;
    } else {
      impulseA = (massA * massB * (uB - uA) * (1+COR))/(massA + massB);
      impulseB = (massA * massB * (uA - uB) * (1+COR))/(massA + massB);
    }

    Vec2d imA = mtvNorm.smult(impulseA);
    Vec2d imB = mtvNorm.smult(impulseB);



    //System.out.println("A impulse" + imA);
    //System.out.println("b impulse" + imB);
    //translate by MTV proportional to mass

//    Vec2d posMTV = coll.mtv;
//    if (coll.mtv.y < 0){
//      posMTV = new Vec2d(coll.mtv.x, -coll.mtv.y);
//    } else if (coll.mtv.x < 0){
//      posMTV = new Vec2d(-posMTV.x, posMTV.y);
//
//    }

//
////there is certainly a much more efficient way to do this
//    if (coll.otherShape.isStatic()){
//      //System.out.println("A");
//      this.tc.setPos(this.tc.getPosition().plus(posMTV.smult(fracA)));
//      otherTC.setPos(otherTC.getPosition().minus((posMTV.smult( fracB))));
//    } else {
//      if (coll.mtv.y < 0 && this.tc.getPosition().y < otherTC.getPosition().y
//          || coll.mtv.y > 0 && this.tc.getPosition().y > otherTC.getPosition().y) {
//        //System.out.println("B");
//        this.tc.setPos(new Vec2d(this.tc.getPosition().x, this.tc.getPosition().y + coll.mtv.y * fracA));
//        otherTC.setPos(new Vec2d(otherTC.getPosition().x, otherTC.getPosition().y - coll.mtv.y * fracB));
//      } else if (coll.mtv.y > 0 && this.tc.getPosition().y < otherTC.getPosition().y
//          || coll.mtv.y < 0 && this.tc.getPosition().y > otherTC.getPosition().y) {
//        //System.out.println("C");
//        this.tc.setPos(new Vec2d(this.tc.getPosition().x, this.tc.getPosition().y - coll.mtv.y * fracA));
//        otherTC.setPos(new Vec2d(otherTC.getPosition().x, otherTC.getPosition().y + coll.mtv.y * fracB));
//      } else {
//        //System.out.println("bad news");
//      }
//
//      if (coll.mtv.x < 0 && this.tc.getPosition().x < otherTC.getPosition().x
//          || coll.mtv.x > 0 && this.tc.getPosition().x > otherTC.getPosition().x) {
//        //System.out.println("D");
//        this.tc.setPos(new Vec2d(this.tc.getPosition().x + coll.mtv.x * fracA, this.tc.getPosition().y));
//        otherTC.setPos(new Vec2d(otherTC.getPosition().x - coll.mtv.x * fracB, otherTC.getPosition().y));
//      } else if (coll.mtv.x > 0 && this.tc.getPosition().x < otherTC.getPosition().x
//          || coll.mtv.x < 0 && this.tc.getPosition().x > otherTC.getPosition().x) {
//        //System.out.println("E");
//        this.tc.setPos(new Vec2d(this.tc.getPosition().x - coll.mtv.x * fracA, this.tc.getPosition().y));
//        otherTC.setPos(new Vec2d(otherTC.getPosition().x + coll.mtv.x * fracB, otherTC.getPosition().y));
//      } else {
//        //System.out.println("bad news for x");
//      }
//
//
//
//
//
//    }

//    if (coll.mtv.x != 0){
//      System.out.println(coll.thisShape);
//      System.out.println(posMTV);
//      System.out.println("mtv" + coll.mtv);
//
//    }

    //System.out.println("obj location after" + coll.other.getTransform().getPosition());

    applyImpulse(imA);
    otherPC.applyImpulse(imB);
  }


  @Override
  public void onDraw(GraphicsContext g) {

  }

  @Override
  public void onLateTick() {

  }



  @Override
  public CompEnum getTag() {
    return CompEnum.Physics;
  }
}
