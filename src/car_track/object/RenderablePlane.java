package car_track.object;

import car_track.position.Position;
import car_track.track.RenderableTrack;
import car_track.track.Track;

import java.awt.*;

public class RenderablePlane extends Plane {
    public RenderablePlane(){
        super();
    }

    @Override
    public void tick() {
        super.tick();
        Position offset = bots.getOffset();
        Position.setXOffset(offset.getX());
        Position.setYOffset(offset.getY());
        getTrack().tick();
        if(iterations%30==0)
            bots.recalibrateMaxBot();
    }

    public void render(Graphics2D g){
        getTrack().render(g);
        bots.render(g);
    }

    @Override
    protected Track generateTrack(int length, int width, int unitLength, float turnFactor) {
        return new RenderableTrack(length, width, unitLength, turnFactor, this);
    }

    @Override
    public RenderableTrack getTrack() {
        return (RenderableTrack)super.getTrack();
    }
}
