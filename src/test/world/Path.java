/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.world;

import arbor.util.ArborVector;
import java.util.ArrayList;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Path {

    private ArrayList<ArborVector> points = new ArrayList<>();

    public Path(ArrayList<ArborVector> points) {
        this.points = points;
    }

    public ArborVector getPoint(int index) {
        if (index >= points.size()) {
            return null;
        }
        return points.get(index);
    }

    public int getSize() {
        return points.size();
    }
}
