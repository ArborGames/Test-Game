/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.model.enemies;

import test.world.Path;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Soldier extends Enemy {

    public Soldier() {
        this(0f,0f,0f);
    }

    public Soldier(float x, float y, float rotation) {
        super(x,y,rotation);
        setImage("towerDefense_tile245.png", 64, 64);
    }

    public Soldier(Path path) {
        super(path);
        setImage("towerDefense_tile245.png", 64, 64);
    }
}
