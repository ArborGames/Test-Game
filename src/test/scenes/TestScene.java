/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.scenes;

import arbor.control.ArborInput;
import arbor.model.scene.Scene;
import test.model.enemies.Enemy;
import test.model.enemies.Soldier;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.input.KeyCode;
import test.world.Map;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class TestScene extends Scene {

    private ArrayList<Enemy> enemyUnits = new ArrayList<>();
    private ArrayList<Enemy> toRemove = new ArrayList<>();
    Map map = new Map("map_01.map");

    @Override
    public boolean onExit() {
        enemyUnits.clear();
        return true;
    }

    @Override
    public boolean onEnter() {
        enemyUnits.add(new Soldier(map.getPath()));
        return true;
    }

    @Override
    public void update() {
        //Test input class
        if(ArborInput.getKey(KeyCode.E))
        {
            System.out.println("E pressed");
        }
        
        if (new Random().nextInt(100) > 98) {
            enemyUnits.add(new Soldier(map.getPath()));
        }

        for (Enemy eu : enemyUnits) {
            eu.update();
            if (eu.isDone()) {
                toRemove.add(eu);
            }
        }

        for (Enemy eu : toRemove) {
            enemyUnits.remove(eu);
        }
        toRemove.clear();
    }

    @Override
    public void render(Graphics2D canvas) {
        map.render(canvas);
        //TODO: This can cause one-frame issues if someone is deleted while we are rendering
        for (int i = 0; i < enemyUnits.size(); i++) {
            Enemy eu = enemyUnits.get(i);
            if(eu == null || eu.isDone())
                continue;
            eu.render(canvas);
        }
    }

}
