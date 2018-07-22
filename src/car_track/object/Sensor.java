package car_track.object;

import car_track.bounds.PolygonBound;
import car_track.bounds.render_base.PolygonBase;
import car_track.position.Position;
import car_track.position.UpdatingPosition;

import java.util.ArrayList;

public class Sensor extends PolygonBound{
    private static final float RANGE = 500;

    private Bot bot;
    private float relativeRotation;

    public Sensor(Bot bot, float relativeRotation){
        super(new UpdatingPosition(bot.getPosition()), new Position[]{new Position(), new Position(RANGE, 0)});
        position.removeListener(this);
        bot.getPosition().addListener(this);
        this.bot = bot;
        this.relativeRotation = relativeRotation;
    }

    public float reading() {
        if(changed)
            update();
        PolygonBase track = bot.getPlane().getTrack().getTrack();
        float[] slopes = track.getSlopes(), lineRanges = track.getLineRanges();
        Position[] absolute = track.getAbsolute();
        float minDistance = RANGE;

        for (int i = 0; i < slopes.length; i++) {
            if (i != slopes.length - 1) {
                int m = i * 4;
                Position intersection = intersection(this.slopes[0], slopes[i], this.absolute[0].getX(), this.absolute[0].getY(), absolute[i].getX(), absolute[i].getY());
                float distance = position.distance(intersection);
                if (minDistance > distance) {
                    if (Float.isInfinite(slopes[i])) {
                        if (intersection.getY() > lineRanges[m + 2] && intersection.getY() > this.lineRanges[2] && intersection.getY() < lineRanges[m + 3] && intersection.getY() < this.lineRanges[3]) {
                            minDistance = distance;
                        }
                    } else if (intersection.getX() > lineRanges[m] && intersection.getX() > this.lineRanges[0] && intersection.getX() < lineRanges[m + 1] && intersection.getX() < this.lineRanges[1]) {
                        minDistance = distance;
                    }
                }
            }
        }
        return minDistance;
    }

    @Override
    protected void update() {
        position.setX(bot.getPosition().getX());
        position.setY(bot.getPosition().getY());
        position.setRotation(bot.getPosition().getRotation()+relativeRotation);
        super.update();
    }

    public float getRelativeRotation() {
        return relativeRotation;
    }
}