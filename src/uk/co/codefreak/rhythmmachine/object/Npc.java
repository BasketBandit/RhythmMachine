package uk.co.codefreak.rhythmmachine.object;

import java.util.Random;

public class Npc extends Entity{

    private static int npcType;

    public Npc(int npcType) {
        this.setName(new Random().nextInt(1001)+"");
        this.setType(2);
        this.npcType = npcType;
    }

    public int getNpcType() {
        return this.npcType;
    }

    public void setNpcType(int npcType) {
        this.npcType = npcType;
    }

}
