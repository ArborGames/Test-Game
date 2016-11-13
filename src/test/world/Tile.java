/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.world;

import arbor.model.RenderableObject;
import arbor.view.RenderableImpl;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public class Tile extends RenderableObject {

    RenderableImpl renderable = new RenderableImpl();

    public Tile() {
    }

    public Tile(float x, float y, float rotation) {
        super(x, y, rotation);
    }
    
    @Override
    public void update() {
    }

}
