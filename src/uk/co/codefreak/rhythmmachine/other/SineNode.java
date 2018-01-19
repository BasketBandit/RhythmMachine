package uk.co.codefreak.rhythmmachine.other;

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

    // IN FRAME INIT
    // for(int i = 0; i < sineNodes.length; i++) {
    //    sineNodes[i] = new SineNode();
    //    for(int x = 0; x < i; x++) {
    //        sineNodes[i].setAngle();
    //        sineNodes[i].setAngle();
    //        sineNodes[i].setAngle();
    //    }
    // }

    // IN THE RENDER METHOD
    // for(int j = 0; j < sineNodes.length; j++) {
    //     grr.fill3DRect(j*5+30, 150, 5, (int) Math.round(33 * sineNodes[j].getAngle()), true);
    // }

    // IN THE TICK METHOD
    // for (int i = 0; i < sineNodes.length; i++) {
    //     sineNodes[i].setAngle();
    // }

}
