package engine.BehaviorTree;

public class SequenceNode extends Composite {
  public SequenceNode() {
    super();
  }

  @Override
  public Status update(long nanos) {
    // run the last running node
    int curIndex = this.children.indexOf(this.lastRunning);
    // edge case: no last running; start at beginning
    if (curIndex == -1) curIndex = 0;
    for (int i = curIndex; i < this.children.size(); i++) {
      // run child
      BehaviorTreeNode childToRun = this.children.get(i);
      Status childStatus = childToRun.update(nanos);
      // choose behavior based on child result
      switch (childStatus) {
        case FAIL:
          // if child fails, return fail
          this.reset();
          return childStatus;
        case SUCCESS:
          // if child succeeds, keep going
          break;
        case RUNNING:
          this.lastRunning = this.children.get(i);
          return childStatus;
      }
    }
    // if we get here, all children succeeded and sequence is complete
    this.reset();
    return Status.SUCCESS;
  }

  @Override
  public void reset() {
    this.lastRunning = null;
    for (BehaviorTreeNode child : this.children) {
      child.reset();
    }
  }
}