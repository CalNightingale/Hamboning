package engine.BehaviorTree;

import com.sun.istack.internal.NotNull;

public class ConditionNode implements BehaviorTreeNode{
  public final Blackboard blackboard;

  public ConditionNode(Blackboard blackboard) {
    this.blackboard = blackboard;
  }

  public ConditionNode(Blackboard blackboard, @NotNull Composite parent) {
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
