/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.util;

import arbor.util.ArborVector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import test.world.Map;
import test.world.Tile;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class MapReader {

    public static final int MAP_SCALE = 64;

    private static MapReader instance = new MapReader();

    public static MapReader getInstance() {
        return instance;
    }

    private MapReader() {
    }

    int[] flags = new int[]{0B0001, // NORTH
        0B0010, // EAST
        0B0100, // SOUTH
        0B1000 // WEST
};

    int entryDir = 0, exitDir = 0, pathSize = 0;

    public void loadMap(String mapName, Map map) {
        int entryDir = 0, exitDir = 0, pathSize = 0;
        int[][] parsedMap = parseFile(mapName);
        TileType[][] typedMap = getTypedMap(parsedMap);
        map.setPath(loadPath(parsedMap));
        map.setTileMap(createTiles(typedMap));
        map.setBuildingMap(createBuildingMap(parsedMap));
    }

    private int[][] parseFile(String mapName) {
        ArrayList<String> fileLines = null;
        try {
            fileLines = readMapFile("maps/" + mapName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get dimensions of tile map
        String widthHeight = fileLines.get(0);
        String[] split = widthHeight.split(",");
        int width = Integer.parseInt(split[0]);
        int height = Integer.parseInt(split[1]);
        // Get the tile info
        int[][] intMap = new int[width][height];
        for (int y = 0; y < height; y++) {
            String line = fileLines.get(y + 1);
            String[] tiles = line.split(",");
            for (int x = 0; x < width; x++) {
                // Go through, check to see if the tile needs to be a part of
                // the path
                intMap[x][y] = Integer.parseInt(tiles[x]);
                if (intMap[x][y] > pathSize) {
                    pathSize = intMap[x][y];
                }
            }
        }

        split = fileLines.get(height + 1).split(",");
        entryDir = Integer.parseInt(split[0]);
        exitDir = Integer.parseInt(split[1]);

        return intMap;
    }

    private ArrayList<String> readMapFile(String filePath) throws IOException {
        java.nio.file.Path path = Paths.get(filePath);
        return (ArrayList<String>) Files.readAllLines(path);
    }

    private TileType[][] getTypedMap(int[][] parsedMap) {
        int width = parsedMap.length;
        int height = parsedMap[0].length;
        TileType[][] typedMap = new TileType[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int flag = 0;

                if (parsedMap[x][y] == 0) {
                    typedMap[x][y] = TileType.NONE;
                    continue;
                }

                //NORTH
                if (y < height - 1) {
                    if (parsedMap[x][y + 1] != 0) {
                        flag |= flags[2];
                    }
                }
                //SOUTH
                if (y != 0) {
                    if (parsedMap[x][y - 1] != 0) {
                        flag |= flags[0];
                    }
                }
                //EAST
                if (x < width - 1) {
                    if (parsedMap[x + 1][y] != 0) {
                        flag |= flags[1];
                    }
                }
                //WEST
                if (x != 0) {
                    if (parsedMap[x - 1][y] != 0) {
                        flag |= flags[3];
                    }
                }

                if (parsedMap[x][y] == 1) {
                    flag |= flags[entryDir];
                } else if (parsedMap[x][y] == pathSize) {
                    flag |= flags[exitDir];
                }

                typedMap[x][y] = getTileType(flag);
            }
        }
        return typedMap;
    }

    private TileType getTileType(int flag) {
        switch (flag) {
            case 0b0011:
                return TileType.NORTH_EAST;
            case 0b0101:
                return TileType.NORTH_SOUTH;
            case 0b1001:
                return TileType.WEST_NORTH;
            case 0b0110:
                return TileType.EAST_SOUTH;
            case 0b1010:
                return TileType.WEST_EAST;
            case 0b1100:
                return TileType.SOUTH_WEST;
            default:
                System.out.println("ERROR: No appropriate tile type found for tile.");
                System.out.println("This will be shown as a 'NONE' type. Fix this.");
                return TileType.NONE;
        }
    }

    private ArrayList<ArborVector> loadPath(int[][] parsedMap) {
        ArrayList<ArborVector> orderedPoints = getOrderedPathPoints(parsedMap);
        ArrayList<ArborVector> pathPoints = new ArrayList<>();

        ArborVector entry = new ArborVector();
        switch (entryDir) {
            case 0:
                entry = new ArborVector(0f, -1f);
                break;
            case 1:
                entry = new ArborVector(1f, 0f);
                break;
            case 2:
                entry = new ArborVector(0f, 1f);
                break;
            case 3:
                entry = new ArborVector(-1f, 0f);
                break;
        }
        entry.x *= MAP_SCALE;
        entry.y *= MAP_SCALE;
        entry = new ArborVector(orderedPoints.get(0).x + entry.x, orderedPoints.get(0).y + entry.y);
        pathPoints.add(entry);

        for (ArborVector point : orderedPoints) {
            point.x *= MAP_SCALE;
            point.y *= MAP_SCALE;
        }
        pathPoints.addAll(orderedPoints);

        ArborVector exit = new ArborVector();
        switch (exitDir) {
            case 0:
                exit = new ArborVector(0f, -1f);
                break;
            case 1:
                exit = new ArborVector(-1f, 0f);
                break;
            case 2:
                exit = new ArborVector(0f, 1f);
                break;
            case 3:
                exit = new ArborVector(1f, 0f);
                break;
        }
        exit.x *= MAP_SCALE;
        exit.y *= MAP_SCALE;
        exit = new ArborVector(exit.x + orderedPoints.get(orderedPoints.size() - 1).x, exit.y + orderedPoints.get(orderedPoints.size() - 1).y);
        pathPoints.add(exit);

        return pathPoints;
    }

    private ArrayList<ArborVector> getOrderedPathPoints(int[][] parsedMap) {
        ArrayList<ArborVector> orderedPoints = new ArrayList<>();
        orderedPoints.ensureCapacity(pathSize);
        for (int i = 0; i < pathSize; i++) {
            orderedPoints.add(new ArborVector());
        }
        for (int x = 0; x < parsedMap.length; x++) {
            for (int y = 0; y < parsedMap[x].length; y++) {
                if (parsedMap[x][y] != 0) {
                    orderedPoints.set(parsedMap[x][y] - 1, new ArborVector(x, y));
                }
            }
        }
        return orderedPoints;
    }

    private Tile[][] createTiles(TileType[][] typedMap) {
        Tile[][] tileMap = new Tile[typedMap.length][typedMap[0].length];
        for (int x = 0; x < typedMap.length; x++) {
            for (int y = 0; y < typedMap[x].length; y++) {
                tileMap[x][y] = new Tile(x * MAP_SCALE, y * MAP_SCALE, 0);
                tileMap[x][y].setImage(associatedFileName(typedMap[x][y]), MAP_SCALE, MAP_SCALE);
            }
        }

        return tileMap;
    }

    // Returns the screen position of the tile
    private ArborVector getTileLocation(int x, int y) {
        return new ArborVector(x, y);
    }

    private int[][] createBuildingMap(int[][] parsedMap) {
        int[][] buildingMap = parsedMap;
        for (int[] row : buildingMap) {
            for (int i : row) {
                if (i != 0) {
                    i = -1;
                }
            }
        }
        return buildingMap;
    }

    enum TileType {
        NONE, WEST_EAST, NORTH_SOUTH, WEST_NORTH, NORTH_EAST, EAST_SOUTH, SOUTH_WEST
    }

    private static String associatedFileName(TileType t) {
        switch (t) {
            case NONE:
                return "Tiles/none.png";
            case WEST_EAST:
                return "Tiles/west_east.png";
            case NORTH_SOUTH:
                return "Tiles/north_south.png";
            case WEST_NORTH:
                return "Tiles/west_north.png";
            case NORTH_EAST:
                return "Tiles/north_east.png";
            case EAST_SOUTH:
                return "Tiles/east_south.png";
            case SOUTH_WEST:
                return "Tiles/south_west.png";
        }
        return null;
    }
}
