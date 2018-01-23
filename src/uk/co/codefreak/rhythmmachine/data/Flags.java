package uk.co.codefreak.rhythmmachine.data;

import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.world.World;

import java.io.Serializable;

public class Flags implements Serializable {

    // Player data
    public Player PLAYER = null;

    // World data
    public World BASE_WORLD = null;
    public World WORLD = null;

    // Quest data
    public boolean testQuest = false;

    public Flags() {
    }

    public void setFlags(Player player, World baseWorld, World world) {
        PLAYER = player;
        WORLD = world;
        BASE_WORLD = baseWorld;
    }

    public void setFlags(Flags flags) {
        WORLD = flags.WORLD;

    }

    public boolean getFlag(String flag) {
        switch(flag) {
            case "testQuest":
                return testQuest;
            default: return false;
        }
    }

    public void setFlag(String flag, boolean value) {
        switch(flag) {
            case "testQuest":
                testQuest = value;
            default:
        }
    }

}
