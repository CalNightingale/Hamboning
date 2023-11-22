package engine.BehaviorTree;

public class SelectorNode extends Composite{
  public SelectorNode() {
    super();
  }

  @Override
  public Status update(long nanos) {
    // if a previously running child exists, reset it
    if (this.lastRunning != null) {
      this.lastRunning.reset();
      this.lastRunning = null;
    }
    // always start from beginning
    for (BehaviorTreeNode child : this.children) {
      Status childStatus = child.update(nanos);
      switch (childStatus) {
        case FAIL:
          // do nothing, on to next
          break;
        case SUCCESS:
          // if success, we're done; return success
          return childStatus;
        case RUNNING:
          // set last running and return
          this.lastRunning = child;
          return childStatus;
      }
    }
    // if we reach here, all children have failed; return fail
    return Status.FAIL;
  }

  @Override
  public void reset() {
    this.lastRunning = null;
  }
}