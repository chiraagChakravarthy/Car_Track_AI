package car_track.bounds;

import car_track.position.OnPositionChanged;
import car_track.position.Position;
import car_track.position.UpdatingPosition;

import java.awt.*;

public class PolygonBound extends Bound implements OnPositionChanged {
    protected Position[] absolute;
    protected Position[] relative;
    protected float[] slopes;
    protected boolean changed;
    protected float[] lineRanges;
    protected Rectangle outerBound;

    public PolygonBound(UpdatingPosition position, Position[] relative){
        super(position);
        this.relative = relative;
        absolute = new Position[relative.length];
        lineRanges = new float[absolute.length*4];
        slopes = new float[absolute.length];
        position.addListener(this);
    }

    //TODO finish this method
    @Override
    public boolean intersects(CircularBound bound) {
        if(changed)
            update();
        if(inside(bound.getPosition())) {
            return true;
        }
        for (int i = 0; i < absolute.length; i++) {
            if(absolute[i].distance(bound.getPosition())<bound.getRadius()) {
                return true;
            }
            float slope = Float.isFinite(slopes[i])?-1.0f/slopes[i]:0;
            Position closest = intersection(slopes[i], slope, absolute[i].getX(), absolute[i].getY(), bound.getPosition().getX(), bound.getPosition().getY());
            if(closest.distance(bound.getPosition())<bound.getRadius()){
                int m = i*4;
                if(Float.isFinite(slopes[i])){
                    if(closest.getX()>lineRanges[m]&&closest.getX()<lineRanges[m+1]){
                        return true;
                    }
                }
                else {
                    float halfChord = (float) Math.sqrt(bound.getRadius()*bound.getRadius()- Math.pow(bound.getPosition().getX()-lineRanges[m], 2));
                    if(bound.getPosition().getY()+halfChord>lineRanges[m+2]&&bound.getPosition().getY()-halfChord<lineRanges[m+3]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean intersects(PointBound bound) {
        if(changed)
            update();
        return inside(bound.getPosition());
    }

    /*
    1. Check for any edge intersection
        a. If so, then they intersect
    2. Check if one vertex of this is inside the other
        a. if so, then they intersect
    3. Check if one vertex of the other is inside this
        a. If so, then they intersect
    4. They do not intersect
     */
    @Override
    public boolean intersects(PolygonBound bound) {
        if(changed)
            update();
        Position[] boundAbsolute = bound.getAbsolute();
        float[] boundSlopes = bound.getSlopes(),
                boundRanges = bound.getLineRanges();
        for (int i = 0; i < slopes.length; i++) {
            int m = i*4;
            for (int j = 0; j < boundSlopes.length; j++) {
                int n = j*4;
                Position intersection = intersection(slopes[i], boundSlopes[j], absolute[i].getX(), absolute[i].getY(), boundAbsolute[j].getX(), boundAbsolute[j].getY());
                if(Float.isInfinite(boundSlopes[j])) {
                    if(intersection.getY() > lineRanges[m+2]&&intersection.getY()>boundRanges[n+2]&&intersection.getY()<lineRanges[m+3]&&intersection.getY()<boundRanges[n+3]) {
                        return true;
                    }
                }
                else if(intersection.getX() > lineRanges[m]&&intersection.getX()>boundRanges[n]&&intersection.getX()<lineRanges[m+1]&&intersection.getX()<boundRanges[n+1]) {
                    return true;
                }
            }
        }
        for(Position position : absolute){
            if(bound.inside(position)) {
                return true;
            }
        }
        for (Position position : boundAbsolute) {
            if (inside(position)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean inside(CircularBound bound) {
        if(changed)
            update();
        if(inside(bound.getPosition())){
            for (int i = 0; i < absolute.length; i++) {
                if(absolute[i].distance(bound.getPosition())<bound.getRadius()) {
                    return false;
                }
                float slope = Float.isFinite(slopes[i])?-1.0f/slopes[i]:0;
                Position closest = intersection(slopes[i], slope, absolute[i].getX(), absolute[i].getY(), bound.getPosition().getX(), bound.getPosition().getY());
                if(closest.distance(bound.getPosition())<bound.getRadius()){
                    int m = i*4;
                    if(Float.isFinite(slopes[i])&&closest.getX()>lineRanges[m]&&closest.getX()<lineRanges[m+1])
                        return false;
                    else {
                        float halfChord = (float) Math.sqrt(bound.getRadius()*bound.getRadius()- Math.pow(bound.getPosition().getX()-lineRanges[m], 2));
                        if(bound.getPosition().getY()+halfChord>lineRanges[m+2]&&bound.getPosition().getY()-halfChord<lineRanges[m+3]) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean inside(PointBound bound) {
        return inside(bound.getPosition());
    }

    @Override
    public boolean inside(PolygonBound bound) {
        for (int i = 0; i < bound.absolute.length; i++) {
            if(!inside(bound.getAbsolute()[i]))
                return false;
        }
        return true;
    }

    @Override
    public Rectangle outerBounds() {
        return outerBound;
    }

    //TODO optimize finding odd/even intersects between horizontal line from point and edge of polygon
    protected void update(){
        float maxX=0, maxY=0, minX=0, minY=0;
        for (int i = 0; i < relative.length; i++) {
            Position absolute = new Position(position).move(relative[i], true);
            this.absolute[i] = absolute;
            if(i==0){
                maxX = absolute.getX();
                minX = absolute.getX();
                maxY = absolute.getY();
                minY = absolute.getY();
            }
            maxX = Math.max(maxX, absolute.getX());
            maxY = Math.max(maxY, absolute.getY());
            minX = Math.min(minX, absolute.getX());
            minY = Math.min(minY, absolute.getY());
        }
        outerBound = new Rectangle((int)minX, (int)minY, (int)(maxX-minX), (int)(maxY-minY));
        for (int i = 0; i < relative.length; i++) {
            int n = (i+1)%relative.length, m=4*i;
            slopes[i] = (int)absolute[i].getX()==(int)absolute[n].getX()? Float.POSITIVE_INFINITY:(absolute[i].getY()-absolute[n].getY())/(absolute[i].getX()-absolute[n].getX());
            lineRanges[m] = Math.min(absolute[i].getX(), absolute[n].getX());
            lineRanges[m+1] = Math.max(absolute[i].getX(), absolute[n].getX());
            lineRanges[m+2] = Math.min(absolute[i].getY(), absolute[n].getY());
            lineRanges[m+3] = Math.max(absolute[i].getY(), absolute[n].getY());
        }
        changed = false;
    }

    private boolean inside(Position position){
        if(changed)
            update();
        int amount = 0;
        for (int i = 0; i < absolute.length; i++) {
            if(intersects(position.getX(), position.getY(), i)) {
                amount++;
            }
        }
        return amount%2==1;
    }

    @Override
    public void onPositionChanged() {
        changed = true;
    }

    private boolean intersects(float x, float y, int i){
        int m = i*4;
        Position intersection = intersection(0, slopes[i], x, y, absolute[i].getX(), absolute[i].getY());
        return (Float.isInfinite(slopes[i])?intersection.getY()>lineRanges[m+2]&&intersection.getY()<lineRanges[m+3]:
                intersection.getX()>lineRanges[m]&&intersection.getX()<=lineRanges[m+1])&&intersection.getX()>x;
    }

    public Position[] getAbsolute() {
        if(changed)
            update();
        return absolute;
    }

    public float[] getSlopes(){
        if(changed)
            update();
        return slopes;
    }

    public float[] getLineRanges(){
        if(changed)
            update();
        return lineRanges;
    }
}