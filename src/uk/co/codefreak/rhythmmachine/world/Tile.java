package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.colour.Colour;

import java.awt.*;
import java.io.Serializable;

public class Tile implements Serializable {

    private String tileCharacter = "0";
    private Color tileColour = null;
    private int tileType = 0;

    public Tile() {
        this.tileCharacter = "▒";
        this.tileColour = Colour.GREY_40;
        this.tileType = 1;
    }

    public Tile(String character, int type, Color colour) {
        this.tileCharacter = character;
        this.tileType = type;
        this.tileColour = colour;
    }

    // Setters & Getters

    public void setTileCharacter(String character) {
        this.tileCharacter = character;
    }

    public String getTileCharacter() {
        return tileCharacter;
    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public int getTileType() {
        return tileType;
    }

    public void setTileColour(Color colour) {
        this.tileColour = colour;
    }

    public Color getTileColour(boolean night) {
        if(night) {
            double red = tileColour.getRed() * 0.4;
            double green = tileColour.getGreen() * 0.4;
            double blue = tileColour.getBlue() * 0.4;
            return new Color((int)red,(int)green,(int)blue);
        } else {
            return tileColour;
        }
    }

    public void setTileInternals(String character, int type, Color colour) {
        this.tileCharacter = character;
        this.tileType = type;
        this.tileColour = colour;
    }

    // Informative

    public boolean containsNpc() {
        switch(tileCharacter) {
            case "S":
            case "D":
            case "M":
                return true;
            default: return false;
        }
    }

    public boolean containsPlayer() {
        return tileCharacter.equals("λ");
    }

    public boolean isWater() {
        return tileCharacter.equals("w");
    }

    public boolean isDoor() {
        return tileCharacter.equals("X");
    }

    public boolean isSolid() {
        return tileType == 1;
    }

    public boolean causesShoreline() {
        switch(tileCharacter) {
            case "e":
            case "@":
                return false;
            default: return true;
        }
    }

    public String toString() {
        return tileCharacter;
    }

}
