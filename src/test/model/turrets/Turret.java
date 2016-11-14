/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.model.turrets;

import arbor.model.RenderableObject;
import arbor.util.ArborVector;
import arbor.view.RenderableImpl;
import java.awt.Graphics2D;
import test.model.enemies.Enemy;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Turret extends RenderableObject {

    private float range = 100f;
    private int damage = 10;
    private int reloadTime = 10;

    private float reloadTimer = 0f;
    private boolean shot = false;

    public Turret() {
        this(0f, 0f, 0f);
    }

    public Turret(float x, float y, float rotation) {
        super(x, y, rotation);
        setImage("towerDefense_tile249.png", 64, 64);
        muzzleFlash.setImage("towerDefense_tile298.png", 64, 64);
    }

    public Turret(ArborVector position, float rotation) {
        super(position, rotation);
        setImage("towerDefense_tile249.png", 64, 64);
        muzzleFlash.setImage("towerDefense_tile298.png", 64, 64);
    }

    @Override
    public void update() {
        shot = false;
        if (reloadTimer >= 0) {
            reloadTimer--;
        }
    }

    public void update(Enemy toShoot) {
        //TODO: Turret update
        if (toShoot != null) {
            rotation = ArborVector.angle(position, toShoot.getPosition()) + (float) ((2 * Math.PI) / 4);
            if (reloadTimer <= 0) {
                toShoot.damage(damage);
                reloadTimer = reloadTime;
                shot = true;
            }
        }
    }

    public float getRange() {
        return range;
    }

    //Muzzleflash
    private RenderableImpl muzzleFlash = new RenderableImpl();

    @Override
    public void render(Graphics2D canvas) {
        if (shot) {
            ArborVector pos = ArborVector.rotate(new ArborVector(0, -32), rotation);
            muzzleFlash.render(canvas, (int) position.x + (int) pos.x, (int) position.y + (int) pos.y, rotation);
        }
        super.render(canvas);
    }
}
