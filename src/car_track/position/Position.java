package car_track.position;
import java.awt.*;

public class Position {
    private static final Position ZERO;
    protected static float xOffset, yOffset, SCR_WIDTH, SCR_HEIGHT;
    static {
        xOffset = 0;
        yOffset = 0;
        ZERO = new Position(0, 0);
    }

    public static float getxOffset() {
        return xOffset;
    }

    public static float getyOffset() {
        return yOffset;
    }

    public static void setXOffset(float xOffset) {
        Position.xOffset = xOffset;
    }

    public static void setYOffset(float yOffset) {
        Position.yOffset = yOffset;
    }

    public static float distance(float x, float y, float x1, float y1) {
        float dx = x-x1, dy = y-y1;
        return (float) Math.sqrt(dx*dx+dy*dy);
    }

    public static Position fromPolar(float radians, float r){
        radians = (float) (radians%(Math.PI*2));
        float pi = (float) Math.PI,
                parentSine = radians<pi/2?radians:radians<3*pi/2?pi-radians:radians-2*pi,
                parentCos = radians>pi?pi*2-radians:radians;
        return new Position((float) Math.cos(parentCos)*r, (float) Math.sin(parentSine)*r);
    }

    private float x, y, rotation;

    public Position(float x, float y, float rotation){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public Position(float x, float y){
        this(x, y, 0);
    }

    public Position(){
        this(0, 0);
    }

    public Position(Point point){
        this(point.x, point.y);
    }

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
        this.rotation = position.rotation;
    }

    public float distance(Position position){
        float a = position.x-x,
                o=position.y-y;
        return (float) Math.sqrt(a*a+o*o);
    }

    public float angleTo(Position position){
        float a = position.x-x,
                o = position.y-y,
                h = (float) Math.sqrt(a*a+o*o),
                c = a/h,
                t = (float) Math.acos(c);
        return (float) (o<0?(2* Math.PI-t):o>0?t:a>0?0: Math.PI);
    }

    public Position apply(Vector vector){
        x += vector.getVelX();
        y += vector.getVelY();
        rotation += vector.getVelAng();
        return this;
    }

    public Position setX(float x) {
        this.x = x;
        return this;
    }

    public Position setY(float y) {
        this.y = y;
        return this;
    }

    public Position setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public Position absPosition(){
        return new Position(x+xOffset-SCR_WIDTH/2, y+yOffset-SCR_HEIGHT/2, rotation);
    }

    public Position scrPosition(){
        return new Position(getScrX(), getScrY(), rotation);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getScrX(){
        return x-xOffset+SCR_WIDTH/2;
    }

    public float getScrY(){
        return y-yOffset + SCR_HEIGHT/2;
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    public Position clone(){
        return new Position(x, y, rotation);
    }

    public Position move(Position position, boolean applyDirection){
        if(applyDirection && rotation!=0){
            float r = ZERO.angleTo(position) + rotation,
                    d = ZERO.distance(position);
            this.x += Math.cos(r)*d;
            this.y += Math.sin(r)*d;
            return this;
        }
        this.x += position.x;
        this.y += position.y;
        return this;
    }

    public Position move(float x, float y, float r){
        return move(new Position(x, y, r), true);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y;
    }

    public Position transform(float x, float y, float rotation) {
        this.x += x;
        this.y +=y;
        this.rotation += rotation;
        return this;
    }

    public Position translate(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public static void setScrHeight(float scrHeight) {
        SCR_HEIGHT = scrHeight;
    }

    public static void setScrWidth(float scrWidth) {
        SCR_WIDTH = scrWidth;
    }
}
