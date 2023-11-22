package engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MapBuilder {
  private final int MAP_WIDTH;
  private final int MAP_HEIGHT;
  private final int BORDER_LAYERS;
  private final Random rand;
  private final char[][] map;
  private final int MAX_DEPTH = 4;
  private final char WALL = 'F';
  private final char FLOOR = 'G';
  private final char MAINCHAR = 'C';
  private final char WATER = 'W';
  private final char EXIT = 'X';
  private HashSet<Room> roomList = new HashSet<>();
  private HashSet<Room> subroomList = new HashSet<>();

  private final int MIN_ROOM_WIDTH = 3;  // Define as per your requirements
  private final int MIN_ROOM_HEIGHT = 3; // Define as per your requirements

  public MapBuilder(int width, int height, long seed, int borderLayers) {
    this.MAP_WIDTH = width;
    this.MAP_HEIGHT = height;
    this.BORDER_LAYERS = borderLayers;
    this.rand = new Random(seed);
    this.map = new char[MAP_WIDTH][MAP_HEIGHT];

    // Initialize map with forest tiles
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        map[x][y] = WALL;
      }
    }
  }


  public char[][] getMap(){return this.map;}

  public void setCharPos(){
    System.out.println(MAP_HEIGHT);
    System.out.println(BORDER_LAYERS);
    boolean charSet = false;
    boolean exitSet = false;
    for (int y = 0; y < MAP_WIDTH; y++) {
      for (int x = 0; x < MAP_HEIGHT; x++) {
        if (map[x][y] == FLOOR && !charSet){
          map[x][y] = MAINCHAR;
          charSet = true;
        } else if (map[x][y] == FLOOR && !exitSet){
          if (y > (MAP_WIDTH - BORDER_LAYERS - 4)){
            if (rand.nextInt(10) > 7 || (map[x][y+1] == WALL && map[x+1][y] == WALL)){
              map[x][y] = EXIT;
              System.out.println("made exit at "+ x + " " + y);
              return;
            }
          }
        }
      }
    }

  }


  private void createRoom(Room room) {
    for (int y = room.y; y < room.height + room.y; y++) {
      for (int x = room.x; x < room.width + room.x; x++) {
        map[x][y] = FLOOR;
      }
    }



  }

  private void createSubRoom(Room room){
    for (int y = room.y; y < room.height + room.y; y++) {
      for (int x = room.x; x < room.width + room.x; x++) {
        map[x][y] = WATER;
      }
    }

  }

  private boolean split(int depth, Room room, boolean splittingX) {
    if (depth == 0){
      return false;
    }
    if (splittingX){
      if (room.width > (2 * MIN_ROOM_WIDTH + 1)){ // keeps the room large enough to split
        //choose an axis inside the room
        int randomAxis = room.x + MIN_ROOM_WIDTH + this.rand.nextInt((room.x + room.width -  2 * MIN_ROOM_WIDTH) - (room.x)-1);
        //System.out.println("chosen random x axis" + randomAxis);

        Room leftRoom = new Room(room.x, room.y, randomAxis - room.x, room.height, room);
        Room rightRoom = new Room(randomAxis+1, room.y, room.width + room.x - randomAxis -1,room.height, room);

        room.addChild(leftRoom);
        room.addChild(rightRoom);

//        System.out.println("leftRoom");
//        leftRoom.printRoom();
//        System.out.println("right room");
//        rightRoom.printRoom();

        split(depth-1, leftRoom, false);
        split(depth-1, rightRoom, false);



      } else {
        //System.out.println("room too skinny");
        return true;
      }


    } else {
      if (room.height > (2 * MIN_ROOM_HEIGHT + 1)){ // keeps the room large enough to split
        //choose an axis inside the room
        int randomAxis = room.y + MIN_ROOM_HEIGHT + this.rand.nextInt((room.y + room.height -  2 * MIN_ROOM_HEIGHT) - (room.y)-1);
        //System.out.println("chosen random y axis" + randomAxis);

        Room leftRoom = new Room(room.x, room.y, room.width, randomAxis - room.y, room);
        Room rightRoom = new Room(room.x, randomAxis+1, room.width,room.height+room.y - randomAxis -1, room);

//        System.out.println("leftRoom");
//        leftRoom.printRoom();
//        System.out.println("right room");
//        rightRoom.printRoom();

        room.addChild(leftRoom);
        room.addChild(rightRoom);

        split(depth-1, leftRoom, true);
        split(depth-1, rightRoom, true);

      } else {
        //System.out.println("room too short");
        return true;
      }

    }



    //starting out xStart = 4, xEnd = 20, yStart = 4, yEnd = 20, splitting X true




    return true;

  }

  public void generateMap() {
    Room root = new Room(BORDER_LAYERS, BORDER_LAYERS, MAP_WIDTH - (2 * BORDER_LAYERS), MAP_WIDTH - (2 * BORDER_LAYERS), null);
    split(3, root,  true);
    createSpaceList(root);
    for (Room rm: roomList){
      //createRoom(rm);
      Room subroom = rm.generateRandomSubRoom(rm.parent);
      subroomList.add(subroom);
      createRoom(subroom);

      //System.out.println("outer room");
      //rm.printRoom();

      //System.out.println("inner room");
      //subroom.printRoom();
    }
    connectUpwards(roomList);
    setCharPos();



  }

  public void connectUpwards(HashSet<Room> roomL){
    HashSet<Room> processedParents = new HashSet<>();

    for (Room leaf: roomL){
      Room parent = leaf.parent;
      if (parent == null || processedParents.contains(parent)) continue;

      List<Room> siblings = parent.children;

      createRoom(createCorridors(siblings.get(0), siblings.get(1)));

      processedParents.add(parent);

    }

    if (!processedParents.isEmpty()) {
      connectUpwards((processedParents));
    }

  }

  public Room createCorridors(Room room1, Room room2) {
    // Calculate the center points of each room
    int center1x = room1.x + room1.width / 2;
    int center1y = room1.y + room1.height / 2;
    int center2x = room2.x + room2.width / 2;
    int center2y = room2.y + room2.height / 2;

    // Check if the corridor is horizontal or vertical
    boolean isHorizontal = center1y == center2y;

    // Generate a random width for the corridor, between 1 and 3
    int corridorWidth = 1 + rand.nextInt(3); // Random number between 1 and 3
    //int corridorWidth = 1;

    int corridorX, corridorY, corridorLength;

    if (isHorizontal) {
      corridorX = Math.min(center1x, center2x);
      corridorY = center1y - corridorWidth / 2; // Center the corridor on the y-axis
      corridorLength = Math.abs(center1x - center2x);
      return new Room(corridorX, corridorY, corridorLength, corridorWidth, room1); // horizontal corridor
    } else {
      corridorX = center1x - corridorWidth / 2; // Center the corridor on the x-axis
      corridorY = Math.min(center1y, center2y);
      corridorLength = Math.abs(center1y - center2y);
      return new Room(corridorX, corridorY, corridorWidth, corridorLength, room2); // vertical corridor
    }
  }


  public void createSpaceList(Room room){
    if (room.getChildren().size() == 0){
      roomList.add(room);
    } else {
      for (Room rm: room.getChildren()){
        createSpaceList(rm);

      }
    }
  }

  public void saveToFile(String filepath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        for (int x = 0; x < MAP_WIDTH; x++) {
          writer.write(map[x][y]);
        }
        writer.newLine();
      }
    }
  }

  public void printMap() {
    for (int y = 0; y < MAP_HEIGHT; y++) {
      for (int x = 0; x < MAP_WIDTH; x++) {
        System.out.print(map[x][y]);
      }
      System.out.println();
    }
  }


  class Room {
    public int x, y, width, height;
    public List<Room> children = new ArrayList<>();
    public Room parent;

    public Room(int x, int y, int width, int height, Room parent) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.parent = parent;
    }

    public void addChild(Room room){children.add(room);}
    public Room getParent(){return this.parent;}

    public List<Room> getChildren(){return this.children;}

    public void printRoom(){
      System.out.println("room x" + this.x);
      System.out.println("room y " + this.y);
      System.out.println("room width" + this.width);
      System.out.println("room height"+ this.height);
      System.out.println( );
    }


    public Room generateRandomSubRoom(Room parent) {

      // Calculate minimum dimensions based on the constraint that the new room
      // should be at least half the size of the parent room.
      int minWidth = this.width / 2 + (this.width % 2 == 1 ? 1 : 0);
      int minHeight = this.height / 2 + (this.height % 2 == 1 ? 1 : 0);

      // Calculate the range of possible widths and heights for the new room.
      int widthRange = this.width - minWidth;
      int heightRange = this.height - minHeight;

      // Generate random width and height within the allowed ranges.
      int randomWidth = minWidth + (widthRange > 0 ? rand.nextInt(widthRange) : 0);
      int randomHeight = minHeight + (heightRange > 0 ? rand.nextInt(heightRange) : 0);

      // Calculate the maximum possible x and y coordinates for the top-left corner
      // of the new room, ensuring the new room is fully contained within the current room.
      int maxX = this.x + this.width - randomWidth;
      int maxY = this.y + this.height - randomHeight;

      // Generate random x and y coordinates within the allowed ranges.
      int randomX = this.x + (maxX > this.x ? rand.nextInt(maxX - this.x) : 0);
      int randomY = this.y + (maxY > this.y ? rand.nextInt(maxY - this.y) : 0);

      // Create the new room with the calculated random properties.
      Room newRoom = new Room(randomX, randomY, randomWidth, randomHeight, parent);

      // Return the new room.
      return newRoom;
    }
  }

}
