package uk.co.codefreak.rhythmmachine.object;

import java.util.Random;

public class NonPlayableCharacter extends Entity {

    // NPC Type -> 0 = Sheep, 1 = Duck, etc.
    private int npcType;

    public NonPlayableCharacter(int xPos, int yPos, int npcType) {
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
            case 1: string += "Duck ";
            break;
            default: string += "ERR";
        }

        string += "named " + getName() + ".";

        return string;
    }

    public String toString() {
        switch(npcType) {
            case 0: return "S";
            case 1: return "D";
            default: return "ERR";
        }
    }

}
