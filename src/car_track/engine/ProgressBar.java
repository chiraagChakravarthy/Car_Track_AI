package car_track.engine;


import java.awt.*;

public class ProgressBar {
    private static final float BORDER_PROPORTION = 0.1f;
    private float progress, x, y, width, height, barX, barWidth, barY, barHeight;
    private Color borderColor, barColor;
    public ProgressBar(float x, float y, float width, float height, Color borderColor, Color barColor){
        this.width = width;
        this.height = height;
        this.progress = 1;
        this.borderColor = borderColor;
        this.barColor = barColor;
        barWidth = width*(1- BORDER_PROPORTION);
        barHeight = height-width*BORDER_PROPORTION;
        setX(x);
        setY(y);
    }

    public void render(Graphics2D g) {
        g.setColor(borderColor);
        g.fillRoundRect((int)x, (int)y, (int)width, (int)height, (int)height/5, (int)height/5);
        g.setColor(barColor);
        g.setStroke(new BasicStroke(height/30.0f));
        g.drawRoundRect((int)barX, (int)barY, ((int)barWidth), (int)barHeight, (int)barHeight/5, (int)barHeight/5);
        g.fillRoundRect((int)barX, (int)barY, (int)(barWidth*progress), (int)barHeight, (int)barHeight/5, (int)barHeight/5);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        barX = (width-barWidth)/2+x;
    }

    public void setY(float y) {
        this.y = y;
        barY = (height-barHeight)/2+y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
