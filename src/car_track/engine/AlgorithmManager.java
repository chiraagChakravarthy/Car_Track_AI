package car_track.engine;

import car_track.algorithm.GeneticAlgorithm;
import car_track.object.Plane;
import car_track.object.RenderablePlane;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AlgorithmManager implements Runnable {
    private static final int GENERATION_INTERVAL = 1;
    private static final float DISPLAY_RATE = 0.1f;
    private RenderablePlane plane;
    private volatile State state;
    private ProgressBar bar;
    private boolean running;
    private Thread windowThread;
    private int generation;

    public AlgorithmManager(){
        state = State.PROCESSING;
        bar = new ProgressBar(RunnableWindow.getInstance().getWindowWidth()/2-100, 100, 200, 100, Color.LIGHT_GRAY, Color.CYAN);
        windowThread = Thread.currentThread();
        windowThread.setPriority(Thread.MIN_PRIORITY);
        generation = 0;
    }

    public void tick(){
        if(state.equals(State.DISPLAYING)){
            plane.tick();
            if(plane.isFinished()) {
                windowThread.setPriority(Thread.MIN_PRIORITY);
                state = State.PROCESSING;
            }
        }
    }

    public void render(Graphics2D g){
        if(running) {
            if (state.equals(State.DISPLAYING)) {
                plane.render(g);
            } else {
                bar.render(g);
            }
            g.setFont(new Font("Arial", Font.PLAIN, 50));
            g.setColor(Color.WHITE);
            g.drawString(generation + "", 100, 100);
        }
        else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 50));
            g.drawString("Press [Space] To Start", 500, 500);
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        GeneticAlgorithm algorithm = new GeneticAlgorithm();
        algorithm.init();
        while (running) {
            if (generation % GENERATION_INTERVAL == 0) {
                while (state.equals(State.DISPLAYING));
                RenderablePlane displayPlane = new RenderablePlane();
                displayPlane.getBots().addAll(algorithm.getBots(displayPlane));
                displayGeneration(displayPlane);
            }
            bar.setProgress(((float)generation%GENERATION_INTERVAL)/GENERATION_INTERVAL);
            generation++;
            for (int i = 0; i < 2; i++) {
                Plane plane = new Plane();
                plane.getBots().addAll(algorithm.getBots(plane));
                while (!plane.isFinished()) {
                    plane.tick();
                }
            }
            algorithm.sort();
            algorithm.propagateAndMutate();
        }
    }

    private void displayGeneration(RenderablePlane plane){
        this.plane = plane;
        state = State.DISPLAYING;
    }

    public void keyPressed(int k) {
        if(k== KeyEvent.VK_SPACE  && !running){
            RunnableWindow.getService().execute(this);
            running = true;
        }
    }

    public void keyReleased(int k) {

    }

    public enum State {
        DISPLAYING,
        PROCESSING
    }
}
