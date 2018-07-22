package car_track.position.movement;

import car_track.position.UpdatingPosition;
import car_track.position.Vector;

public class OmniAccAngMovement extends OmniDirectionalMovement {
    private boolean right, left;
    private float angAcc, angVel;

    public OmniAccAngMovement(UpdatingPosition position, Vector vector, float acc, float maxVel, float angAcc, float angVel) {
        super(position, vector, acc, maxVel);
        this.angAcc = angAcc;
        this.angVel = angVel;
    }

    @Override
    public void tick() {
        vector.setVelAng(right?Math.min(vector.getVelAng() + angAcc, angVel): left?Math.max(vector.getVelAng()-angAcc, -angVel):vector.getVelAng()>0?Math.max(vector.getVelAng()-angAcc, 0):Math.min(vector.getVelAng()+angAcc, 0));
        super.tick();
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}
