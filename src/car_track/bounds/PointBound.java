package car_track.bounds;

import car_track.position.UpdatingPosition;

import java.awt.*;

public class PointBound extends Bound {

    public PointBound(UpdatingPosition position){
        super(position);
    }

    @Override
    public boolean intersects(CircularBound bound) {
        return position.distance(bound.getPosition()) <= bound.getRadius();
    }

    @Override
    public boolean intersects(PointBound bound) {
        return false;
    }

    @Override
    public boolean intersects(PolygonBound bound) {
        return bound.intersects(this);
    }

    @Override
    public boolean inside(CircularBound bound) {
        return false;
    }

    @Override
    public boolean inside(PointBound bound) {
        return false;
    }

    @Override
    public boolean inside(PolygonBound bound) {
        return false;
    }

    @Override
    public Rectangle outerBounds() {
        return new Rectangle((int)position.getX(), (int)position.getY(), 1, 1);
    }
}
