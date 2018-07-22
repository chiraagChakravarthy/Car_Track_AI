package car_track.position.movement;

import car_track.position.UpdatingPosition;
import car_track.position.Vector;

public abstract class MovementVector {
    protected UpdatingPosition position;
    protected Vector vector;
    public MovementVector(UpdatingPosition position, Vector vector){
        this.position = position;
        this.vector = vector;
    }
    public abstract void tick();

    public Vector getVector() {
        return vector;
    }

    public UpdatingPosition getPosition() {
        return position;
    }
}