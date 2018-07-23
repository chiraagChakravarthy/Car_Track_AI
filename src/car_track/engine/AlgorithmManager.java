package car_track.engine;

import car_track.algorithm.GeneticAlgorithm;
import car_track.object.Bot;
import car_track.object.Plane;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmManager implements Runnable {
    private static final int GENERATION_INTERVAL = 10;
    private static final float DISPLAY_RATE = 0.1f;
    private Plane plane;
    private volatile State state;
    private ProgressBar bar;
    private boolean running;
    private Thread windowThread;
    private int generation;

    public AlgorithmManager(){
        state = State.PROCESSING;
        bar = new ProgressBar(RunnableWindow.getInstance().getWindowWidth()/2-100, 100, 200, 100, Color.LIGHT_GRAY, Color.CYAN);
        running = true;
        windowThread = Thread.currentThread();
        windowThread.setPriority(Thread.MIN_PRIORITY);
        generation = 0;
    }

    public void tick(){
        if(state.equals(State.DISPLAYING)){
            plane.tick();
            if(plane.getIterations()%30==0)
                plane.getBots().recalibrateMaxBot();
            if(plane.isFinished()) {
                windowThread.setPriority(Thread.MIN_PRIORITY);
                state = State.PROCESSING;
            }
            plane.setOffset();
        }
    }

    public void render(Graphics2D g){
        if(state.equals(State.DISPLAYING)){
            plane.render(g);
        }
        else {
            bar.render(g);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.setColor(Color.WHITE);
        g.drawString(generation + "", 100, 100);
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        GeneticAlgorithm algorithm = new GeneticAlgorithm();
        algorithm.init();
        while (running) {
            algorithm.sort();
            if (generation % GENERATION_INTERVAL == 0) {
                while (state.equals(State.DISPLAYING));
                displayGeneration(new Plane(), algorithm);
            }
            algorithm.propagateAndMutate();
            bar.setProgress(((float)generation%GENERATION_INTERVAL)/GENERATION_INTERVAL);
            generation++;
            Plane plane = new Plane();
            plane.getBots().addAll(algorithm.getBots(plane));
            while (!plane.isFinished()) {
                plane.tick();
            }
        }
    }

    private void displayGeneration(Plane plane, GeneticAlgorithm algorithm){
        this.plane = plane;
        ArrayList<Bot> allBots = algorithm.getBots(plane);
        Bot[] bots = new Bot[(int) (allBots.size()*DISPLAY_RATE)];
        System.arraycopy(allBots.toArray(new Bot[allBots.size()]), 0, bots, 0, bots.length);
        plane.getBots().addAll(new ArrayList<>(Arrays.asList(bots)));
        state = State.DISPLAYING;
    }

    public void keyPressed(int k) {
        if(k== KeyEvent.VK_SPACE)
            RunnableWindow.getService().execute(this);
    }

    public void keyReleased(int k) {

    }

    public enum State {
        DISPLAYING,
        PROCESSING
    }
}
