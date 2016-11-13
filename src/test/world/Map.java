/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.world;

import arbor.util.ArborVector;
import java.awt.Graphics2D;
import java.util.ArrayList;
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
}
