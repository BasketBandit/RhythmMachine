package uk.co.codefreak.rhythmmachine.colour;

import java.awt.*;

public class Colours {

    private static final Color[] colours = new Color[1];

    public Colours() {
        colours[0] = new Color(139,69,19);
    }

    public Color getColour(int colour) {
        return colours[colour];
    }
}
