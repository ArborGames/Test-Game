/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.model.turrets;

import arbor.model.RenderableObject;
import arbor.util.ArborVector;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Turret extends RenderableObject{

    private float range = 3f;
    private int damage = 1;
    private float rateOfFire = 10f;
    
    public Turret() {
        this(0f,0f,0f);
    }

    public Turret(float x, float y, float rotation) {
        super(x, y, rotation);
        setImage("towerDefense_tile249.png", 64, 64);
    }

    public Turret(ArborVector position, float rotation) {
        super(position, rotation);
        setImage("towerDefense_tile249.png", 64, 64);
    }
    
    

    @Override
    public void update() {
        //TODO: Turret update
    }
}
