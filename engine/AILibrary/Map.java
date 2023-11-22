package engine.AILibrary;

import engine.Components.MovementDir;
import engine.support.Vec2d;
import engine.support.Vec2i;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Map {
  private int[][] levelMap;
  private Vec2d initCharPos;
  public Map(char[][] map){
    int cols = map[0].length;
    levelMap = new int[map.length][cols];
    for (int i = 0; i < map.length; i++){
      for (int j = 0; j < cols; j++){
        switch(map[i][j]){
          case('F'):
            levelMap[i][j] = 0;
            break;
          case('C'):
            levelMap[i][j] = 1;
            initCharPos = new Vec2d(i, j);
            break;
          case('G'):
            levelMap[i][j] = 1;
            break;
          default:
            break;
        }


      }
    }


  }

  /**
   *gives you all of the neighbors that you could go to (could instead do the neighbors you can't go to)
   */
  public List<MovementDir> getNeighbors(Vec2i pos){
    List<MovementDir> neighbors = new ArrayList<>();
      if (levelMap[pos.x+1][pos.y] == 1){
        neighbors.add(MovementDir.RIGHT);
      } if (levelMap[pos.x-1][pos.y] == 1){
        neighbors.add(MovementDir.LEFT);
      } if(levelMap[pos.x][pos.y+1] == 1){
        neighbors.add(MovementDir.DOWN);
      } if(levelMap[pos.x][pos.y-1] == 1){
        neighbors.add(MovementDir.UP);
      }

      if (levelMap[pos.x+1][pos.y+1] == 1 && neighbors.contains(MovementDir.DOWN) && neighbors.contains(MovementDir.RIGHT)){
        neighbors.add(MovementDir.DOWNRIGHT);
      } if (levelMap[pos.x-1][pos.y+1] == 1 && neighbors.contains(MovementDir.DOWN) && neighbors.contains(MovementDir.LEFT)){
        neighbors.add(MovementDir.DOWNLEFT);
      } if (levelMap[pos.x+1][pos.y-1] == 1 && neighbors.contains(MovementDir.UP) && neighbors.contains(MovementDir.RIGHT)){
        neighbors.add(MovementDir.UPRIGHT);
      } if (levelMap[pos.x-1][pos.y-1] == 1 && neighbors.contains(MovementDir.UP) && neighbors.contains(MovementDir.LEFT)){
        neighbors.add(MovementDir.UPLEFT);
      }

    //printNeighbors(pos);
    //System.out.println("neighbor one" + neighbors);



    List<MovementDir> neighbors2 = new ArrayList<>();
    for (int i = -1; i < 2; i++){
      for (int j = -1; j < 2; j++){
        int testNeighbor = levelMap[pos.x + j][pos.y + i];
        if (testNeighbor == 1){
          switch(j){
            case(-1):
              if (i == -1){
                neighbors2.add(MovementDir.UPLEFT);
              } else if (i == 0){
                neighbors2.add(MovementDir.LEFT);
              } else {
                neighbors2.add(MovementDir.DOWNLEFT);
              }

              break;
            case(0):
              if (i == -1){
                neighbors2.add(MovementDir.UP);

              } else if (i == 1){
                neighbors2.add(MovementDir.DOWN);

              }

              break;
            case(1):
              if (i == -1){
                neighbors2.add(MovementDir.UPRIGHT);

              } else if (i == 0){
                neighbors2.add(MovementDir.RIGHT);

              } else {
                neighbors2.add(MovementDir.DOWNRIGHT);

              }
              break;

          }
        }

      }
    }
    //System.out.println("neighbors2" + neighbors2);

    return neighbors;

  }

  public int[][] getMap(){return levelMap;}
  public int getVal(Vec2i pos){return levelMap[pos.x][pos.y];}

  public void printMap() {
    for (int y = 0; y < levelMap.length; y++) {
      for (int x = 0; x < levelMap[0].length; x++) {
        System.out.print(levelMap[x][y]);
      }
      System.out.println();
    }
  }


  public void printNeighbors(Vec2i pos){
    for (int y = -1; y < 2; y++) {
      for (int x = -1; x < 2; x++) {
        System.out.print(levelMap[x + pos.x][y + pos.y]);
      }
      System.out.println();
    }

  }

}
