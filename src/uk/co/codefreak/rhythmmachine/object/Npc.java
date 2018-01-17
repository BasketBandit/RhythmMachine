package uk.co.codefreak.rhythmmachine.object;

import java.util.Random;

public class Npc extends Entity {

    // NPC Type -> 0 = Sheep, 1 = Cow, etc.
    private int npcType;

    public Npc(int xPos, int yPos, int npcType) {
        this.setX(xPos);
        this.setY(yPos);
        this.setName(new Random().nextInt(1001)+"");
        this.setType(1);
        this.setPhysType(1);
        this.npcType = npcType;
    }

    public int getNpcType() {
        return this.npcType;
    }

    public void setNpcType(int npcType) {
        this.npcType = npcType;
    }

    public String getDetails() {
        String string = "";

        switch(npcType) {
            case 0: string += "Sheep ";
            break;

            default: string += "ERR";
        }

        string += "named " + getName() + ".";

        return string;
    }

    public String toString() {
        switch(npcType) {
            case 0: return "S";
            default: return "ERR";
        }
    }

}
