package uk.co.codefreak.rhythmmachine.object;

import uk.co.codefreak.rhythmmachine.colour.Colour;

import java.util.Random;

public class NonPlayableCharacter extends Entity {

    // See documentation for type information.
    private int npcType;

    public NonPlayableCharacter(int xPos, int yPos, int npcType) {
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.setName(new Random().nextInt(1001)+"");
        this.setEntityType(1);
        this.setPhysType(1);
        this.npcType = npcType;

        autoColour(npcType);
    }

    private void autoColour(int npcType) {
        if(npcType == 0) {
            this.setEntityColour(Colour.WHITE);
        } else if(npcType == 1) {
            this.setEntityColour(Colour.YELLOW);
        }
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
