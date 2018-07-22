package car_track.bounds;

import car_track.position.Position;
import car_track.position.UpdatingPosition;

import java.awt.*;

public class CircularBound extends Bound {
    protected float radius;
    public CircularBound(UpdatingPosition position, float radius){
        super(position);
        this.radius = radius;
    }

    //Local
    @Override
    public boolean intersects(CircularBound bound) {
        return bound.position.distance(position)<=bound.radius+radius;
    }

    @Override
    public boolean intersects(PointBound bound) {
        return bound.intersects(this);
    }

    @Override
    public boolean intersects(PolygonBound bound) {
        return bound.intersects(this);
    }

    @Override
    public boolean inside(CircularBound bound) {
        return bound.getPosition().distance(position)+bound.getRadius()>radius;
    }

    @Override
    public boolean inside(PointBound bound) {
        return bound.getPosition().distance(position)>radius;
    }

    @Override
    public boolean inside(PolygonBound bound) {
        for(int i = 0; i < bound.getAbsolute().length; i++){
            if(bound.getAbsolute()[i].distance(position)>radius)
                return false;
        }
        return true;
    }

    @Override
    public Rectangle outerBounds() {
        return new Rectangle((int)(position.getX()-radius), (int)(position.getY()-radius), (int)(radius*2), (int)(radius*2));
    }

    public float getRadius() {
        return radius;
    }
}
