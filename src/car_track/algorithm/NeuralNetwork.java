package car_track.algorithm;

public class NeuralNetwork {

    private double[][][] network;
    private double fitness;

    public NeuralNetwork(int... layers){
        fitness = 0;
        if(layers.length<2){
            throw new IndexOutOfBoundsException("Must have both an input and output layer");
        }
        this.network = new double[layers.length-1][][];
        for (int i = 1; i <= this.network.length; i++) {
            double[][] layer = new double[layers[i]][];
            this.network[i-1] = layer;
            for (int j = 0; j < layer.length; j++) {
                double[] node = new double[1+ layers[i-1]];
                layer[j] = node;
                for (int k = 0; k < node.length; k++) {
                    node[i] = (Math.random()-0.5)*2;
                }
            }
        }
    }

    public NeuralNetwork(NeuralNetwork n1, NeuralNetwork n2){
        fitness = 0;
        network = new double[n1.network.length][][];
        for (int i = 0; i < network.length; i++) {
            double[][] l1 = n1.network[i], l2 = n2.network[i], l = new double[l1.length][];
            for (int j = 0; j < l.length; j++) {
                double[] p1 = l1[j], p2 = l2[j], p = new double[p2.length];
                for (int k = 0; k < p.length; k++) {
                    p[k] = Math.random()<0.5?p1[k]:p2[k];
                }
                l[j] = p;
            }
            network[i] = l;
        }
    }

    public double[] compute(double[] inputs){
        for (double[][] layer : network) {
            double[] outputs = new double[layer.length];
            for (int j = 0; j < outputs.length; j++) {
                double[] node = layer[j];
                outputs[j] += node[0];
                for (int k = 0; k < inputs.length; k++) {
                    outputs[j] += inputs[k]*node[k+1];
                }
                outputs[j] = 1/(1+Math.pow(Math.E, -outputs[j]));
            }
            inputs = outputs;
        }
        return inputs;
    }

    public void mutate(double mutationFactor){
        for (double[][] layer : network) {
            for (double[] node : layer) {
                for (int k = 0; k < node.length; k++) {
                    node[k] += (Math.random() - 0.5) * mutationFactor;
                }
            }
        }
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }
}