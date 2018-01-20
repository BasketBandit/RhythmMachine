package uk.co.codefreak.rhythmmachine.object;

import uk.co.codefreak.rhythmmachine.colour.Colour;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class NonPlayableCharacter extends Entity {

    // See documentation for type information.
    private int npcType;

    public NonPlayableCharacter(int xPos, int yPos, int npcType) {
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.setName(randomName());
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
        } else if(npcType == 2) {
            this.setEntityColour(Colour.BLACK);
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
            case 2: string += "Cow ";
            break;
            default: string += "ERR";
        }

        string += "named " + getName() + ".";

        return string;
    }

    public boolean canSwim() {
        return npcType == 1;
    }

    public String toString() {
        switch(npcType) {
            case 0: return "S";
            case 1: return "D";
            case 2: return "M";
            default: return "ERR";
        }
    }

    private String randomName() {
        try(BufferedReader ln = new BufferedReader(new FileReader("src/resources/textfiles/names.txt"))) {
            int number = new Random().nextInt(4946);
            String name = "";
            for(int i = 0; i < number; i++) {
                name = ln.readLine();
            }
            return name;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "John";
    }

}
