/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.world;

import arbor.util.ArborVector;
import java.awt.Graphics2D;
import java.util.ArrayList;
import test.model.turrets.Turret;
import test.util.MapReader;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Map {

    private Path path;
    private Tile[][] tileMap;
    private int[][] buildingMap;

    public Map(String fileName) {
        MapReader.getInstance().loadMap(fileName, this);
    }

    public void setPath(ArrayList<ArborVector> pathPoints) {
        path = new Path(pathPoints);
    }

    public Path getPath() {
        return path;
    }

    public void setTileMap(Tile[][] tileMap) {
        this.tileMap = tileMap;
    }

    public void setBuildingMap(int[][] buildingMap) {
        this.buildingMap = buildingMap;
    }

    public void render(Graphics2D canvas) {
        for (Tile[] row : tileMap) {
            for (Tile t : row) {
                t.render(canvas);
            }
        }
    }

    public ArborVector screenToMapCoordinates(ArborVector screenCoords) {
        return ArborVector.div(screenCoords, MapReader.MAP_SCALE);
    }

    //TODO: Tidy
    public boolean placeBuilding(Turret t, ArborVector screenPosition) {
        ArborVector gridPos = screenToMapCoordinates(screenPosition);
        int x = (int)gridPos.x;
        int y = (int)gridPos.y;
        if(x < 0 || y < 0 || x >= buildingMap.length || y >= buildingMap[0].length)
        {
            return false;
        }
        //0 = FREE, 1 = TAKEN, -1 = UNPLACEABLE
        if (buildingMap[x][y] == 0) {
            t.setPosition(new ArborVector(x * MapReader.MAP_SCALE, y * MapReader.MAP_SCALE));
            buildingMap[x][y] = 1;
            return true;
        }
        return false;
    }
}
