package engine.AILibrary;

import engine.support.Vec2i;

class Node implements Comparable<Node> {
  Vec2i position;
  Node parent;
  double g;
  double h;
  double f;

  public Node(Vec2i position) {
    this.position = position;
  }

  @Override
  public int compareTo(Node other) {
    return Double.compare(this.f, other.f);
  }
}
