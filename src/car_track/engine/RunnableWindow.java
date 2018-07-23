package car_track.engine;

import car_track.position.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableWindow extends Canvas implements Runnable, KeyListener, MouseListener, MouseWheelListener {
    private static ExecutorService service;
    private static RunnableWindow instance;
    static {
        service = Executors.newCachedThreadPool();
    }

    public static ExecutorService getService() {
        return service;
    }

    public static void main(String[] args) {
        instance = new RunnableWindow();
        instance.init();
        service.execute(instance);
    }

    private final Window window;
    private int runningTime = 0;
    private boolean running;
    private AlgorithmManager manager;

    private RunnableWindow() {
        window = new Window("Car Track AI", 3, this);
        addKeyListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        requestFocus();
        running = true;
    }

    public void init(){
        Position.setScrWidth(getWindowWidth());
        Position.setScrHeight(getWindowHeight());
    }

    public void run() {
        try {
            manager = new AlgorithmManager();
            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            int ticks = 60;
            double ns = 1000000000 / ticks;
            double delta = 0;
            int updates = 0, frames = 0;
            //Allows for the logging of the ticks and frames each second
            //RunnableWindow Loop\\
            while (running) {
                //Boolean which controls the running of the circle_fighter.game loop. Were it to equal false, the circle_fighter.game would simply freeze.
                /////////////////////////////
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while (delta > 1) {
                    tick();
                    updates++;
                    delta--;
                }
                frames++;
                render();
                /////////////////////////////
                //A tick is the circle_fighter.game's equivalent of an instant. The code above allows time to be constant in a loop that varies
                //in the length of each iteration
                if (System.currentTimeMillis() - timer >= 1000) {
                    System.out.println("FPS: " + frames + ", Ticks: " + updates);
                    updates = 0;
                    frames = 0;
                    timer += 1000;
                }
            }
            //RunnableWindow Loop\\
            stop();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }

    public void tick() {
        runningTime++;
        manager.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        //Instead of drawing directly to the canvas, drawing to a buffer strategy allows for a concept called triple buffering
        if (bs == null) {
            createBufferStrategy(2);
            //triple buffering in the long term greatly increases performance. Instead of replacing every pixel, triple buffering
            //only changes the pixels that weren't present before. It also searches for and remembers patterns in a single runtime
            //iteration.
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        manager.render(g);
        g.dispose();
        bs.show();
    }

    public void keyTyped(KeyEvent e) {
        //Irrelevant to program
    }

    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        manager.keyPressed(k);
    }

    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        manager.keyReleased(k);
        //k is an integer representing the key that was released

    }

    public void mouseClicked(MouseEvent e) {
        //Irrelevant to program
    }

    public void mousePressed(MouseEvent e) {
        //The mouse event itself is passed because it contains information of the mouse button pressed and the location of the mouse

    }

    public void mouseReleased(MouseEvent e) {
        //The mouse event itself is passed because it contains information of the mouse button released and the location of the mouse

    }

    public void mouseEntered(MouseEvent e) {
        //Irrelevant to program
    }

    public void mouseExited(MouseEvent e) {
        //Irrelevant to program
    }

    public int getRunningTime() {
        return runningTime;
    }

    public int getWindowWidth() {
        return window.getWidth();
    }

    public int getWindowHeight() {
        return window.getHeight();
    }

    public Point mouseLocation(){
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, window.getFrame());
        return new Point(mouseLocation.x, mouseLocation.y);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    public static float msToTicks(float ms){
        return ms*3/50f;
    }

    public static float ticksToMs(float ticks){
        return ticks*50/3f;
    }

    public static RunnableWindow getInstance() {
        return instance;
    }
}