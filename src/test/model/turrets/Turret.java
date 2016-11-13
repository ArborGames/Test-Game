/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.model.turrets;

import arbor.model.RenderableObject;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Turret extends RenderableObject{

    private float range = 3f;
    private int damage = 1;
    private float rateOfFire = 10f;
    
    public Turret() {
    }

    public Turret(float x, float y, float rotation) {
        super(x, y, rotation);
    }

    @Override
    public void update() {
        //TODO: Turret update
    }
}
