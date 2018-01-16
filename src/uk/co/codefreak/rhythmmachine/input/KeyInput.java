package uk.co.codefreak.rhythmmachine.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private static final int numKeys = 256;
    private static final boolean[] keys = new boolean[numKeys];
    private static final boolean[] lastKeys = new boolean[numKeys];

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public static void update() {
        for (int i = 0; i < numKeys; i++) {
            lastKeys[i] = keys[i];
        }
    }

    public static boolean isDown(int keyCode) {
        return keys[keyCode];
    }

    public static boolean wasPressed(int keyCode) {
        return isDown(keyCode) && !lastKeys[keyCode];
    }

    public static boolean wasReleased(int keyCode) {
        return !isDown(keyCode) && lastKeys[keyCode];
    }

}
