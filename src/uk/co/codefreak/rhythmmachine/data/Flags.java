package uk.co.codefreak.rhythmmachine.data;

import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.world.World;

import java.io.Serializable;

public class Flags implements Serializable {

    // Player data
    public Player player = null;

    // World data
    public World world = null;

    // Quest data
    public boolean testQuest = false;

    public Flags() {
    }


    public void setFlags(Player player, World world) {
        this.player = player;
        this.world = world;
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
