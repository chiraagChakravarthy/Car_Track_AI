package car_track.algorithm;

import car_track.object.Bot;
import car_track.object.Plane;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneticAlgorithm {
    private static final double MUTATION_FACTOR = .1, SURVIVAL_RATE = 0.1;
    private static final int POPULATION_SIZE = 100;
    private NeuralNetwork[] networks;
    public GeneticAlgorithm(){
        networks = new NeuralNetwork[POPULATION_SIZE];
    }

    public void init(){
        for (int i = 0; i < POPULATION_SIZE; i++) {
            networks[i] = new NeuralNetwork(Bot.SENSORS, 16, 3);
        }
    }

    public void propagateAndMutate(){
        Arrays.sort(networks, (o1, o2) -> (int) (Math.signum(o1.getFitness()-o2.getFitness())*2));
        NeuralNetwork[] babies = new NeuralNetwork[networks.length];
        int amountLiving = (int) (SURVIVAL_RATE*networks.length);
        for (int i = 0; i < networks.length; i++) {
            babies[i] = new NeuralNetwork(networks[(int)(networks.length-1-Math.random()*amountLiving)], networks[(int)(networks.length-1-Math.random()*amountLiving)]);
            babies[i].mutate(MUTATION_FACTOR);
        }
        networks = babies;
    }

    public ArrayList<Bot> getBots(Plane plane){
        ArrayList<Bot> bots = new ArrayList<>();
        for(NeuralNetwork network : networks){
            bots.add(new Bot(plane, network));
        }
        return bots;
    }
}
