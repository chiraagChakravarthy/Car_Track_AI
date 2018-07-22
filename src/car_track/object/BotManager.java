package car_track.object;

import car_track.algorithm.GeneticAlgorithm;
import car_track.position.Position;

import java.awt.*;
import java.util.ArrayList;

public class BotManager {
    private ArrayList<Bot> bots;
    private Bot maxBot;
    public BotManager(Plane plane, GeneticAlgorithm algorithm){
        bots = new ArrayList<>();
        bots.addAll(algorithm.getBots(plane));
        maxBot = bots.get(0);
    }

    public void tick(){
        for(Bot bot : bots){
            bot.tick();
        }
    }

    public void render(Graphics2D g){
        for(Bot bot : bots){
            bot.render(g);
        }
    }

    public void disableAll() {
        for(Bot bot : bots){
            bot.disable();
        }
    }

    public boolean allDisabled() {
        for(Bot bot : bots){
            if(bot.isEnabled())
                return false;
        }
        return true;
    }

    public Position getOffset() {
        return maxBot==null?new Position():maxBot.getPosition();
    }

    public void recalibrateMaxBot(){
        int maxIndex = 0;
        double maxVal = 0;
        for (int i = 0; i < bots.size(); i++) {
            if(bots.get(i).isEnabled()) {
                double fitness = bots.get(i).getFitness();
                if (fitness > maxVal) {
                    maxVal = fitness;
                    maxIndex = i;
                }
            }
        }
        this.maxBot = bots.get(maxIndex);
    }

    public Bot getBot(int i) {
        return bots.get(i);
    }
}
