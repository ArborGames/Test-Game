/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.model.enemies;

import arbor.model.RenderableObject;
import arbor.util.ArborVector;
import test.world.Path;

/**
 *
 * @author Alexander Ramsay-Baggs
 */
public abstract class Enemy extends RenderableObject {

    private int currentPathIndex = 0;
    private float speed = 1f;
    private Path path;

    private boolean isDone = false;

    public Enemy() {
        super();
    }

    public Enemy(float x, float y, float rotation) {
        super(x, y, rotation);
    }

    public Enemy(Path path) {
        super();
        setPath(path);
    }

    public void setPath(Path path) {
        this.path = path;
        position = (ArborVector) path.getPoint(0).clone();

        ArborVector currPoint = position;
        ArborVector nextPoint = (ArborVector) path.getPoint(currentPathIndex + 1).clone();

        rotation = ArborVector.angle(currPoint, nextPoint);
    }

    public void followPath() {
        if (isDone) {
            return;
        }
        //Get direction to next path index
        ArborVector currPoint = position;
        if (currentPathIndex + 1 >= path.getSize()) {
            isDone = true;
            return;
        }
        ArborVector nextPoint = (ArborVector) path.getPoint(currentPathIndex + 1).clone();

        ArborVector dir = ArborVector.sub(nextPoint, currPoint);

        float sqrMag = dir.sqrMagnitude();
        
        //If we are closer to it than we would normally be, move that distance
        if (sqrMag < speed * speed) {
            position.set(nextPoint);
            currentPathIndex++;

            //Move toward the next point
            if (currentPathIndex + 1 >= path.getSize()) {
                isDone = true;
                return;
            }
            nextPoint = (ArborVector) path.getPoint(currentPathIndex + 1).clone();

            currPoint.set(position);
            //Move extra distance
            dir = ArborVector.sub(nextPoint, currPoint);

            float speedLeft = sqrMag - (speed * speed);

            dir.normalize();
            dir.mult(speedLeft);
            position.add(dir);

            //Get rotation, and set rotation
            rotation = ArborVector.angle(currPoint, nextPoint);

            return;
        }
        //Else, lets move toward it at speed
        dir.normalize();
        dir.mult(speed);
        position.add(dir);
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public void update() {
        followPath();
    }
}
