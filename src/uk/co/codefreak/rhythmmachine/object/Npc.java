package uk.co.codefreak.rhythmmachine.object;

import java.util.Random;

public class Npc extends Entity{

    private static int npcType;

    public Npc(int xPos, int yPos, int npcType) {
        this.setX(xPos);
        this.setY(yPos);
        this.setName(new Random().nextInt(1001)+"");
        this.setType(1);
        this.npcType = npcType;
    }

    public int getNpcType() {
        return this.npcType;
    }

    public void setNpcType(int npcType) {
        this.npcType = npcType;
    }

    public String toString() {
        switch(npcType) {
            case 0: return "S";
            default: return "ERR";
        }
    }

}
