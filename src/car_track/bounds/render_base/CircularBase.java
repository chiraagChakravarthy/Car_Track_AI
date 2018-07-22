package car_track.bounds.render_base;

import car_track.bounds.CircularBound;
import car_track.position.UpdatingPosition;
import java.awt.*;

public class CircularBase extends CircularBound {
    private Color color;
    public CircularBase(UpdatingPosition position, float radius, Color color){
        super(position, radius);
        this.color = color;
    }

    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int)(position.getScrX()-radius), (int)(position.getScrY()-radius), (int)(2*radius), (int)(2*radius));
    }
}