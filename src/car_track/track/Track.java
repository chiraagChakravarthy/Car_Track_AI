package car_track.track;

import car_track.bounds.PolygonBound;
import car_track.bounds.render_base.PolygonBase;
import car_track.object.Bot;
import car_track.object.Plane;
import car_track.position.Position;
import car_track.position.UpdatingPosition;

import javax.swing.text.Segment;
import java.awt.*;
import java.util.ArrayList;

public class Track {
    private ArrayList<PolygonBase> segments;
    private PolygonBase track;
    private Plane plane;
    private UpdatingPosition position;

    public Track(int length, float width, float unitLength, float turnFactor, Plane plane) {
        Position[] trackPositions = new Position[length*2];
        this.plane = plane;
        Position basePosition = new Position(0, -100, (float) (Math.PI/2)),
                moveFactor = new Position(unitLength, 0),
                widthFactor = new Position(width /2, 0);
        segments = new ArrayList<>();
        position = new UpdatingPosition(0, 0);
        for (int i = 0; i < length; i++) {
            int m = i*2;
            basePosition.setRotation((float) (basePosition.getRotation()+(Math.random()-0.5)*turnFactor)).setRotation((float) Math.max(0, Math.min(basePosition.getRotation(), Math.PI)))
                    .move(moveFactor, true);
            trackPositions[m] = new Position(basePosition).setRotation((float) (basePosition.getRotation()+Math.PI/2)).move(widthFactor, true);
            trackPositions[m+1] = new Position(basePosition).setRotation((float) (basePosition.getRotation()-Math.PI/2)).move(widthFactor, true);
        }
        for (int i = 0; i < trackPositions.length/2-1; i++) {
            int m = i*2;
            segments.add(new PolygonBase(Color.WHITE, position, new Position[]{trackPositions[m], trackPositions[m+1], trackPositions[m+3], trackPositions[m+2]}, i==trackPositions.length/2-2));
        }
        Position[] relative = new Position[trackPositions.length];
        int trackLength = relative.length/2;
        for (int i = 0; i < trackLength; i++) {
            relative[i] = trackPositions[i*2];
            relative[relative.length-i-1] = trackPositions[i*2+1];
        }
        track = new PolygonBase(Color.WHITE, position, relative, false);
    }

    public boolean inside(Bot bot){
        return track.inside(bot.getBase());
    }

    public void render(Graphics2D g){
        track.render(g);
    }

    public void tick(){
        track.tick();
    }

    public double getFitness(Bot bot) {
        for (int i = segments.size()-1; i >=0; i--) {
            if(segments.get(i).intersects(bot.getBase()))
                return i;
        }
        return 0;
    }

    public PolygonBase getTrack() {
        return track;
    }

    public PolygonBound getLastSegment() {
        return segments.get(segments.size()-1);
    }
}
