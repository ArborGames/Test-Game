/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.scenes;

import arbor.control.ArborInput;
import arbor.model.ArborObject;
import arbor.model.RenderableObject;
import arbor.model.scene.Scene;
import arbor.util.ArborVector;
import arbor.view.Camera;
import test.model.enemies.Enemy;
import test.model.enemies.Soldier;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import test.model.enemies.PathCompleteComparator;
import test.model.turrets.Turret;
import test.world.Map;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class TestScene extends Scene {

    private ArrayList<ArborObject> objects = new ArrayList<>();
    private ArrayList<ArborObject> toRemove = new ArrayList<>();
    Map map = new Map("map_01.map");

    @Override
    public boolean onExit() {
        objects.clear();
        return true;
    }

    @Override
    public boolean onEnter() {
        objects.add(new Soldier(map.getPath()));
        return true;
    }

    @Override
    public void update() {
        //Scale camera based on player input.
        Camera.main.shiftScale(-ArborInput.getMouseWheelDelta() / 10f);

        //Place turrets
        if (ArborInput.getMouseButtonDown(1)) {
            Turret t = new Turret();
            if (map.placeBuilding(t, ArborInput.getScaledMousePoisition())) {
                objects.add(t);
                System.out.println("Turret added.");
            } else {
                System.out.println("Turret rejected.");
            }
        }

        if (new Random().nextInt(100) > 98) {
            objects.add(new Soldier(map.getPath()));
        }

        for (ArborObject eu : objects) {
            eu.update();
            if (eu instanceof Enemy && ((Enemy) eu).isDone()) {
                toRemove.add(eu);
            } else if (eu instanceof Turret) {
                PathCompleteComparator comp = new PathCompleteComparator();
                Enemy enemy = null;
                for (ArborObject obj : objects) {
                    //TODO: Really, really improve
                    if (ArborVector.distance(eu.getPosition(), obj.getPosition()) < ((Turret)eu).getRange() && obj instanceof Enemy) {
                        if (enemy == null) {
                            enemy = (Enemy) obj;
                        } else {
                            if (comp.compare(enemy, (Enemy) obj) < 0) {
                                enemy = (Enemy) obj;
                            }
                        }
                    }
                }
                ((Turret) eu).update(enemy);
            }
        }

        for (ArborObject eu : toRemove) {
            objects.remove(eu);
        }
        toRemove.clear();
    }

    @Override
    public void render(Graphics2D canvas) {
        map.render(canvas);
        //TODO: This can cause one-frame issues if someone is deleted while we are rendering
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof RenderableObject) {
                RenderableObject eu = (RenderableObject) objects.get(i);
                if (eu == null || (eu instanceof Enemy && ((Enemy) eu).isDone())) {
                    continue;
                }
                eu.render(canvas);
            }
        }
    }

}
