package engine.BehaviorTree;


public abstract class ActionNode implements BehaviorTreeNode {

  public final Blackboard blackboard;

  public ActionNode(Blackboard blackboard) {
    this.blackboard = blackboard;
  }

  public ActionNode(Blackboard blackboard, Composite parent) {
    this(blackboard);
    parent.addChild(this);
  }

  /**
   * To be implemented gameside. Do an action, return Success or fail depending on result,
   * or return running if still going. By default, return FAIL
   * @param seconds time to do action
   * @return result of action
   */
  @Override
  public Status update(long seconds) {
    return Status.FAIL;
  }

  @Override
  public void reset() {

  }
}