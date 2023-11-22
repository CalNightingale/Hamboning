package engine.BehaviorTree;

public class ConditionNode implements BehaviorTreeNode{
  public final Blackboard blackboard;

  public ConditionNode(Blackboard blackboard) {
    this.blackboard = blackboard;
  }

  public ConditionNode(Blackboard blackboard, Composite parent) {
    this(blackboard);
    parent.addChild(this);
  }


  @Override
  public Status update(long seconds) {
    return Status.FAIL;
  }

  @Override
  public void reset() {

  }
}
