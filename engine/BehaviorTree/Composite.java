package engine.BehaviorTree;

import java.util.ArrayList;
import java.util.List;

public abstract class Composite implements BehaviorTreeNode {
  protected final List<BehaviorTreeNode> children;
  protected BehaviorTreeNode lastRunning;
  public Composite() {
    this.children = new ArrayList<BehaviorTreeNode>();
    this.lastRunning = null;
  }

  public void addChild(int index, BehaviorTreeNode child) {
    if (this.children.contains(child)) {
      throw new RuntimeException("Attempted to add a child that already existed");
    }
    this.children.add(index, child);
  }

  public void addChild(BehaviorTreeNode child) {
    this.addChild(this.children.size(), child);
  }
}
