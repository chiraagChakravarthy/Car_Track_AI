package car_track.object;

import car_track.algorithm.GeneticAlgorithm;
import car_track.engine.RunnableWindow;
import car_track.position.Position;
import car_track.track.Track;

import java.awt.*;

public class Plane {
    private static final int MAX_ITERATIONS = 2000;

    private BotManager bots;
    private Track track;
    private int iterations;

    public Plane(){
        track = new Track(500, 200, 10, (float) (Math.PI/20), this);
        bots = new BotManager();
        iterations = 0;
    }

    public Track getTrack() {
        return track;
    }

    public void tick(){
        bots.tick();
        track.tick();
        iterations++;
    }

    public void render(Graphics2D g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, RunnableWindow.getInstance().getWindowWidth(), RunnableWindow.getInstance().getWindowHeight());
        track.render(g);
        bots.render(g);
    }

    public boolean isFinished(){
        if(iterations>MAX_ITERATIONS)
            bots.disableAll();
        return bots.allDisabled();
    }

    public BotManager getBots() {
        return bots;
    }

    public void setOffset(){
        Position offset = bots.getOffset();
        Position.setXOffset(offset.getX());
        Position.setYOffset(offset.getY());
    }

    public int getIterations() {
        return iterations;
    }
}