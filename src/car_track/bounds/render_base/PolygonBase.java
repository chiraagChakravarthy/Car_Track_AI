package car_track.bounds.render_base;

import car_track.bounds.PolygonBound;
import car_track.position.Position;
import car_track.position.UpdatingPosition;

import java.awt.*;

public class PolygonBase extends PolygonBound {
    private Polygon polygon;
    private Color color;
    private boolean fill;
    public PolygonBase(Color color, UpdatingPosition position, Position[] relative, boolean fill) {
        super(position, relative);
        this.color = color;
        this.fill = fill;
    }


    public void render(Graphics2D g) {
        if(polygon != null){
            g.setColor(color);
            if(fill)
                g.fill(polygon);
            else
                g.draw(polygon);
        }
    }

    public void tick(){
        reconfigurePolygon();
    }

    @Override
    public void onPositionChanged() {
        super.onPositionChanged();
        reconfigurePolygon();
    }

    private void reconfigurePolygon(){
        Position[] absolute = getAbsolute();
        if(absolute != null && absolute[0] != null) {
            int[] x = new int[absolute.length],
                    y = new int[absolute.length];
            for (int i = 0; i < absolute.length; i++) {
                x[i] = (int) absolute[i].getScrX();
                y[i] = (int) absolute[i].getScrY();
            }
            polygon = new Polygon(x, y, absolute.length);
        }
    }
}
