package engine.AILibrary;

import engine.Components.MovementDir;
import engine.support.Vec2i;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Pathfinding {
  private Node currentNode;
  public Pathfinding() {

  }

  public List<Vec2i> aStarPathfinding(Vec2i start, Vec2i goal, Map map) {
    PriorityQueue<Node> openList = new PriorityQueue<>();
    Set<Vec2i> closedList = new HashSet<>();

    Node startNode = new Node(start);
    startNode.g = 0;
    startNode.h = start.dist2(goal); // Assuming Vec2i has a distance method or use your own heuristic.
    startNode.f = startNode.g + startNode.h;

    openList.add(startNode);

    while (!openList.isEmpty()) {
      this.currentNode = openList.poll();
      closedList.add(currentNode.position);

      if (currentNode.position.equals(goal)) {
        return reconstructPath(currentNode);
      }

      for (MovementDir dir : map.getNeighbors(currentNode.position)) {
        Vec2i neighborPos = getNewPosition(currentNode.position, dir); // Implement a method that, given a Vec2i and MovementDir, returns the next Vec2i.

        if (closedList.contains(neighborPos) || map.getVal(neighborPos) == 0) {
          continue;
        }

        Node neighborNode = new Node(neighborPos);
        neighborNode.g = currentNode.g + 1;
        neighborNode.h = neighborPos.dist2(goal);
        neighborNode.f = neighborNode.g + neighborNode.h;
        neighborNode.parent = currentNode;

        boolean shouldAdd = true;
        for (Node node : openList) {
          if (node.position.equals(neighborPos) && node.f < neighborNode.f) {
            shouldAdd = false;
            break;
          }
        }

        if (shouldAdd) {
          openList.add(neighborNode);
        }
      }
    }

//    System.out.println("emptied list");
//    System.out.println(reconstructPath(currentNode));
    System.out.println("couldn't find path");
//    System.out.println("start" + start);
//    System.out.println("end" + goal);

    return null;
  }

  private List<Vec2i> reconstructPath(Node node) {
    List<Vec2i> path = new ArrayList<>();
    while (node != null) {
      path.add(0, node.position);  // Always add to the front to reverse the path.
      node = node.parent;
    }
    //System.out.println(path);
    return path;
  }

  private Vec2i getNewPosition(Vec2i position, MovementDir dir) {
    switch(dir){
      case UP:
        return position.plus(new Vec2i(0, -1));
      case DOWN:
        return position.plus(new Vec2i(0, 1)); // I think this should be +1, not -1
      case LEFT:
        return position.plus(new Vec2i(-1, 0));
      case RIGHT:
        return position.plus(new Vec2i(1, 0));
      case UPLEFT:
        return position.plus(new Vec2i(-1, -1));
      case UPRIGHT:
        return position.plus(new Vec2i(1, -1));
      case DOWNLEFT:
        return position.plus(new Vec2i(-1, 1));
      case DOWNRIGHT:
        return position.plus(new Vec2i(1, 1));
      default:
        return position; // For the NONE case and as a general fallback
    }
  }

}
