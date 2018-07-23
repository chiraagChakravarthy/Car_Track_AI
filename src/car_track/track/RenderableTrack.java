package car_track.track;

import car_track.bounds.PolygonBound;
import car_track.bounds.render_base.PolygonBase;
import car_track.object.Plane;
import car_track.position.Position;

import java.awt.*;

public class RenderableTrack extends Track {

    public RenderableTrack(int length, float width, float unitLength, float turnFactor, Plane plane) {
        super(length, width, unitLength, turnFactor, plane);
    }

    @Override
    protected PolygonBound generateBound(Position[] relative) {
        return new PolygonBase(Color.WHITE, position, relative, false);
    }

    public void render(Graphics2D g) {
        getTrack().render(g);
    }

    public void tick() {
        getTrack().tick();
    }

    @Override
    public PolygonBase getTrack() {
        return (PolygonBase)super.getTrack();
    }
}
