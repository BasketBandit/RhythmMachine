package uk.co.codefreak.rhythmmachine;

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

}
