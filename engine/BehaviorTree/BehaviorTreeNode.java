package engine.BehaviorTree;

public interface BehaviorTreeNode {
  Status update(long seconds);
  void reset();

}
