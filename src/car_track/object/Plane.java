package car_track.object;

import car_track.track.Track;

public class Plane {
    private static final int MAX_ITERATIONS = 2000;
    protected BotManager bots;
    protected Track track;
    protected int iterations;

    public Plane(){
        track = generateTrack(500, 200, 10, (float) (Math.PI/20));
        this.bots = new BotManager();
        iterations = 0;
    }

    public Track getTrack() {
        return track;
    }

    public void tick(){
        bots.tick();
        iterations++;
    }

    public boolean isFinished(){
        if(iterations>MAX_ITERATIONS)
            bots.disableAll();
        return bots.allDisabled();
    }

    public BotManager getBots() {
        return bots;
    }

    public int getIterations() {
        return iterations;
    }

    protected Track generateTrack(int length, int width, int unitLength, float turnFactor){
        return new Track(length, width, unitLength, turnFactor, this);
    }
}