/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.model.enemies;

import java.util.Comparator;

/**
 *
 * @author a.c.ramsay-baggs
 */
public class PathCompleteComparator implements Comparator<Enemy> {

    @Override
    public int compare(Enemy t, Enemy t1) {
        //Use path distance
        if (t.getPathIndex() == t1.getPathIndex()) {
            return Float.compare(t1.getDistanceToNextPoint(), t.getDistanceToNextPoint());
        }
        return Integer.compare(t.getPathIndex(), t1.getPathIndex());
    }

}
