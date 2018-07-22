package car_track.position;

public class Vector{
    private float velX, velY, velAng;

    public Vector(float velX, float velY, float velAng){
        this.velX = velX;
        this.velY = velY;
        this.velAng = velAng;
    }


    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public float getVelAng() {
        return velAng;
    }

    public Vector setVelX(float velX) {
        this.velX = velX;
        return this;
    }

    public Vector setVelY(float velY) {
        this.velY = velY;
        return this;
    }

    public Vector setVelAng(float velAng) {
        this.velAng = velAng;
        return this;
    }

    public float velocity(){
        return (float) Math.sqrt(velX*velX+velY*velY);
    }
}