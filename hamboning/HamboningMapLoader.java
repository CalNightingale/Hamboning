package hamboning;

import engine.AILibrary.Map;
import engine.Components.GraphicsComponent;
import engine.Components.SpriteComponent;
import engine.Components.TransformComponent;
import engine.GameObject;
import engine.GameWorld;
import engine.Systems.CollisionSystem;
import engine.Systems.GraphicsSystem;
import engine.Systems.InputSystem;
import engine.Systems.SpriteSystem;
import engine.support.Vec2d;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class HamboningMapLoader {
  private SpriteSystem s;
  private GraphicsSystem g;
  private CollisionSystem c;
  private InputSystem i;
  private Vec2d mapSize;
  private GameWorld gw;
  public Vec2d tileWorldSize;
  private static Vec2d tilePixelSize = new Vec2d(16);
  private final int BORDER_LAYERS = 4;
  private Random random;

  public HamboningMapLoader(SpriteSystem s, GraphicsSystem g, CollisionSystem c, InputSystem i, Vec2d mapSize, GameWorld gw, long seed){
    this.gw = gw;
    this.s = s;
    this.g = g;
    this.c = c;
    this.i = i;
    this.mapSize = mapSize;
    this.random = new Random(seed);


    //register all of the images here

    this.s.registerImage("grass1","hamboning/assets/MapAssets/hamboning-darkgrass.png" );
    this.s.registerImage("grass2","hamboning/assets/MapAssets/hamboning-lightgrass.png" );
    this.s.registerImage("grass3","hamboning/assets/MapAssets/hamboning-mixedgrass.png" );
    this.s.registerImage("grass4","hamboning/assets/MapAssets/hamboning-morelightgrass.png" );

    this.s.registerImage("dirt1","hamboning/assets/MapAssets/hamboning-dirt.png" );
    this.s.registerImage("dirt2","hamboning/assets/MapAssets/hamboning-darkdirt.png" );
    this.s.registerImage("dirt3","hamboning/assets/MapAssets/hamboning-mixeddirt.png" );

    this.s.registerImage("water","hamboning/assets/MapAssets/hamboning-sky1.png");
    this.s.registerImage("edge","hamboning/assets/MapAssets/hamboning-border.png");
    this.s.registerImage("border","hamboning/assets/MapAssets/hamboning-sky2.png");

    this.s.registerImage("tree","hamboning/assets/MapAssets/hamboning-tree.png" );
    this.s.registerImage("tree2","hamboning/assets/MapAssets/hamboning-secondtree.png" );

    this.s.registerImage("rock","hamboning/assets/MapAssets/hamboning-rock.png" );
    this.s.registerImage("rock2","hamboning/assets/MapAssets/hamoning-secondrock.png" );
    this.s.registerImage("shrub","hamboning/assets/MapAssets/hamboning-shrub.png" );
    this.s.registerImage("house","hamboning/assets/MapAssets/hamboning-house.png" );
    this.s.registerImage("skips","hamboning/assets/MapAssets/hamboning-skipshouse.png" );
    this.s.registerImage("snack","hamboning/assets/MapAssets/hamboning-snackbar.png" );
    this.s.registerImage("tables","hamboning/assets/MapAssets/hamboning-tablesChairs.png" );
    this.s.registerImage("playground","hamboning/assets/MapAssets/hamboning-playground.png" );
    this.s.registerImage("trailer","hamboning/assets/MapAssets/hamboning-trailer.png" );
    this.s.registerImage("weeds","hamboning/assets/MapAssets/hamboning-weed.png" );
    this.s.registerImage("leaves","hamboning/assets/MapAssets/hamboning-leaves.png" );



    loadMap();







  }

  public void loadMap() {
    this.tileWorldSize = new Vec2d(this.mapSize.x / HamboningConstants.TILE_AMT, this.mapSize.y / HamboningConstants.TILE_AMT);

    try (BufferedReader reader = new BufferedReader(new FileReader("hamboning/assets/parkMapLevel1.txt"))) {
      String line;
      int rowCount = 0;  // Keep track of how many rows have been read

      while ((line = reader.readLine()) != null) {

        for (int colCount = 0; colCount < HamboningConstants.TILE_AMT; colCount++) {
          createTileFromChar(line.charAt(colCount), rowCount, colCount);
        }
        rowCount++;
      }

    } catch (IOException e) {
      // Handle exceptions - file not found, etc.
      e.printStackTrace();
    }

    try (BufferedReader reader = new BufferedReader(new FileReader("hamboning/assets/parkMapLevel2.txt"))) {
      String line;
      int rowCount = 0;  // Keep track of how many rows have been read

      while ((line = reader.readLine()) != null) {

        for (int colCount = 0; colCount < HamboningConstants.TILE_AMT; colCount++) {
          createAuxEls(line.charAt(colCount), rowCount, colCount);
        }
        rowCount++;
      }

    } catch (IOException e) {
      // Handle exceptions - file not found, etc.
      e.printStackTrace();
    }
  }

  private void createAuxEls(char charAt, int rowCount, int colCount) {
    switch (charAt) {
      case 'S':
        createShrub(rowCount, colCount);
        break;
      case 'T':
        createTree(rowCount, colCount);
        break;
      case 'R':
        createRock(rowCount, colCount);
        break;
      case 'C':
        createHouse(rowCount, colCount);
        break;
      case 'D':
        createSnackBar(rowCount, colCount);
        break;
      case 'A':
        createPlayground(rowCount, colCount);
        break;
      case 'B':
        createSkips(rowCount, colCount);
        break;
      case 'F':
        createTrailer(rowCount, colCount);
        break;
      case 'E':
        createTables(rowCount, colCount);
        break;
      case 'Q':
        int randInt = this.random.nextInt(30);
        if (randInt < 2){
          createWeeds(rowCount, colCount);
        } else if (randInt > 28){
          createLeaves(rowCount, colCount);
        }
        break;
      default:
        createDefaultTile(rowCount, colCount);
        break;
    }

  }

//      this.s.registerImage("tree","hamboning/assets/MapAssets/hamboning-tree.png" );
//    this.s.registerImage("rock","hamboning/assets/MapAssets/hamboning-rock.png" );
//    this.s.registerImage("shrub","hamboning/assets/MapAssets/hamboning-shrub.png" );
//    this.s.registerImage("house","hamboning/assets/MapAssets/hamboning-house.png" );
//    this.s.registerImage("skips","hamboning/assets/MapAssets/hamboning-skipshouse.png" );
//    this.s.registerImage("snack","hamboning/assets/MapAssets/hamboning-snackbar.png" );
//    this.s.registerImage("tables","hamboning/assets/MapAssets/hamboning-tablesChairs.png" );
//    this.s.registerImage("playground","hamboning/assets/MapAssets/hamboning-playground.png" );
//    this.s.registerImage("trailer","hamboning/assets/MapAssets/hamboning-trailer.png" );
//    this.s.registerImage("weeds","hamboning/assets/MapAssets/hamboning-weed.png" );
//    this.s.registerImage("leaves","hamboning/assets/MapAssets/hamboning-leaves.png" );

  private void createHouse(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(7 * tileWorldSize.x, 5 * tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(50, 41),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("house"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }

  private void createShrub(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(30, 17),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("shrub"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);

  }

  private void createTree(int rowCount, int colCount) {
    double randDouble = this.random.nextDouble();
    if (randDouble > 0.5) {
      Vec2d location = new Vec2d(0, 0);
      GameObject tile = new GameObject(
          new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y),
          new Vec2d(tileWorldSize.x, 2 * tileWorldSize.y), this.gw);

      SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(51, 62),
          Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
      tile.addComponent(spriteComponent);
      s.addObject(tile);
      s.loadSprite(tile, s.getImage("tree"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(),
          this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 1);
    } else {
      Vec2d location = new Vec2d(0, 0);
      GameObject tile = new GameObject(
          new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y),
          new Vec2d(tileWorldSize.x, 2 * tileWorldSize.y), this.gw);

      SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(90, 127),
          Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
      tile.addComponent(spriteComponent);
      s.addObject(tile);
      s.loadSprite(tile, s.getImage("tree2"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(),
          this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 1);


    }
  }

  private void createRock(int rowCount, int colCount) {
    double randDouble = this.random.nextDouble();
    if (randDouble > 0.5) {
      Vec2d location = new Vec2d(0,0);
      GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(2 * tileWorldSize.x, 1.5 * tileWorldSize.y), this.gw);

      SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(17,8),
          Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
      tile.addComponent(spriteComponent);
      s.addObject(tile);
      s.loadSprite(tile, s.getImage("rock"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 1);

    } else {
      Vec2d location = new Vec2d(0,0);
      GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(2 * tileWorldSize.x,  tileWorldSize.y), this.gw);

      SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(60,17),
          Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
      tile.addComponent(spriteComponent);
      s.addObject(tile);
      s.loadSprite(tile, s.getImage("rock2"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 1);
    }

  }

  private void createSnackBar(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(4 * tileWorldSize.x, 3 * tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(113,58),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("snack"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }

  private void createSkips(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(5 * tileWorldSize.x, 4 * tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(343,202),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("skips"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }

  private void createPlayground(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(5 * tileWorldSize.x, 5 * tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(200,81),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("playground"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }

  private void createTrailer(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(6 * tileWorldSize.x, 3 * tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(200, 86),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("trailer"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }

  private void createTables(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(tileWorldSize.x, tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(70,48),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("tables"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }

  private void createLeaves(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(tileWorldSize.x, tileWorldSize.y), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(100,87),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("leaves"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);

  }

  private void createWeeds(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), new Vec2d(tileWorldSize.x/2, tileWorldSize.y/2), this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, new Vec2d(10, 5),
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), false);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("weeds"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 1);
  }





  private void createTileFromChar(char c, int rowCount, int colCount) {
    switch (c) {
      case 'G':
        createGrassTile(rowCount, colCount);
        break;
      case 'D':
        createDirtTile(rowCount, colCount);
        break;
      case 'B':
        createBorderTile(rowCount, colCount);
        break;
      case 'W':
        createWaterTile(rowCount, colCount);
        break;
      case 'E':
        createEdgeTile(rowCount, colCount);
        break;
      case 'F':
      default:
        createDefaultTile(rowCount, colCount);
        break;
    }
  }

  private void createDirtTile(int rowCount, int colCount) {
    double randDouble = this.random.nextDouble();
    if (randDouble > 0.5) {
      Vec2d location = new Vec2d(0,0);
      GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

      SpriteComponent spriteComponent = new SpriteComponent(location, tilePixelSize,
          Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
      tile.addComponent(spriteComponent);
      s.addObject(tile);
      s.loadSprite(tile, s.getImage("dirt1"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 0);

    } else {
      Vec2d location = new Vec2d(0,0);
      GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

      SpriteComponent spriteComponent = new SpriteComponent(location, tilePixelSize,
          Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
      tile.addComponent(spriteComponent);
      s.addObject(tile);
      s.loadSprite(tile, s.getImage("dirt2"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 0);
    }


  }

  private void createWaterTile(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, tilePixelSize,
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("water"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 0);
  }

  private void createBorderTile(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, tilePixelSize,
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("border"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 0);
  }

  private void createEdgeTile(int rowCount, int colCount) {
    Vec2d location = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(location, tilePixelSize,
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    s.loadSprite(tile, s.getImage("edge"));
    GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
    tile.addComponent(graphics);
    g.addObjectToLayer(tile, 0);
  }

  private void createGrassTile(int rowCount, int colCount) {
    Vec2d grassLocation = new Vec2d(0,0);
    GameObject tile = new GameObject(new Vec2d(colCount * tileWorldSize.x, rowCount * tileWorldSize.y), tileWorldSize, this.gw);

    SpriteComponent spriteComponent = new SpriteComponent(grassLocation, tilePixelSize,
        Color.MAROON, this.gw.getScreenSize(), tile.getTransform(), true);
    tile.addComponent(spriteComponent);
    s.addObject(tile);
    double randDouble = this.random.nextDouble();
    if (randDouble > 0.75) {

      s.loadSprite(tile, s.getImage("grass1"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 0);
    } else if (randDouble > 0.5) {

      s.loadSprite(tile, s.getImage("grass2"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 0);
    } else if (randDouble > 0.25) {

      s.loadSprite(tile, s.getImage("grass3"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 0);

    } else {
      s.loadSprite(tile, s.getImage("grass4"));
      GraphicsComponent graphics = new GraphicsComponent(tile.getTransform(), this.gw.getScreenSize(), spriteComponent);
      tile.addComponent(graphics);
      g.addObjectToLayer(tile, 0);

    }




  }

  private void createDefaultTile(int rowCount, int colCount) {
    // Logic to handle unknown types of tiles
    //System.out.println("creating default (nothing)");
    // TODO
  }



}
