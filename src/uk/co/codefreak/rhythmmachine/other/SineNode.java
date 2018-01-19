package uk.co.codefreak.rhythmmachine.other;

import java.util.*;

public class SineNode {

    private double angle = 0.0;

    public SineNode() {
    }

    public void setAngle() {
        angle += 0.1;
    }

    public double getAngle() {
        return Math.sin(angle);
    }

    public int getAngleInt() {
        return (int) Math.round(Math.sin(angle));
    }

    // IN THE RENDER METHOD
    // for(int j = 0; j < sineNodes.length; j++) {
    //     grr.fill3DRect(j*5+30, 150, 5, (int) Math.round(33 * sineNodes[j].getAngle()), true);
    // }

    // IN THE TICK METHOD
    // for (int i = 0; i < sineNodes.length; i++) {
    //     sineNodes[i].setAngle();
    // }

}
