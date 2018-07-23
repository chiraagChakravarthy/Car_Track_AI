package car_track.object;

import car_track.algorithm.NeuralNetwork;
import car_track.bounds.render_base.CircularBase;
import car_track.position.UpdatingPosition;
import car_track.position.Vector;
import car_track.position.movement.OmniAccAngMovement;

import java.awt.*;

public class Bot {
    public static final int SENSORS = 10;
    public static final float RADIUS = 10;
    private UpdatingPosition position;
    private Vector vector;
    private OmniAccAngMovement movement;
    private CircularBase base;
    private Sensor[] sensors;
    private Plane plane;
    private boolean enabled;
    private NeuralNetwork network;

    public Bot(Plane plane, NeuralNetwork network){
        this.network = network;
        position = new UpdatingPosition(0, 0, (float) (Math.PI/2));
        vector = new Vector(0, 0, 0);
        movement = new OmniAccAngMovement(position, vector, 0.4f, 12, (float) Math.toRadians(12), (float) Math.toRadians(12));
        base = new CircularBase(position, RADIUS, new Color((int)(Math.random()*255), (int)(Math.random()*255),(int)(Math.random()*255)));
        sensors = new Sensor[SENSORS];
        enabled = true;
        movement.setFront(true);
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = new Sensor(this, (float) ((float)i/SENSORS*Math.PI*2));
        }
        this.plane = plane;
    }

    public void tick(){
        if(enabled){
            double[] sensorInputs = new double[sensors.length];
            for (int i = 0; i < sensors.length; i++) {
                sensorInputs[i] = sensors[i].reading();;
            }
            double[] output = network.compute(sensorInputs);
            //Left, Stop, Right
            int max = 0;
            double maxVal = output[0];
            for (int i = 0; i < output.length; i++) {
                if(output[i]>maxVal){
                    maxVal = output[i];
                    max = i;
                }
            }
            System.out.println(max);
            movement.setLeft(max==0);
            movement.setRight(max==2);
            movement.tick();
            position.apply(vector);
            if(!plane.getTrack().inside(this)||plane.getTrack().getLastSegment().inside(base)){
                disable();
            }
        }
    }

    public void render(Graphics2D g){
        base.render(g);
    }

    public CircularBase getBase() {
        return base;
    }

    public UpdatingPosition getPosition() {
        return position;
    }

    public void disable(){
        enabled = false;
        network.setFitness(network.getFitness()+plane.getTrack().getFitness(this));
    }

    public Plane getPlane() {
        return plane;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public double getFitness() {
        return plane.getTrack().getFitness(this);
    }

    public Sensor[] getSensors() {
        return sensors;
    }
}