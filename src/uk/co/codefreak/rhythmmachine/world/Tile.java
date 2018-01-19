package uk.co.codefreak.rhythmmachine.world;

import java.io.Serializable;

public class Tile implements Serializable {

    private String tileInside = "0";
    private int tileType = 0;

    public Tile() {
        this.tileInside = "▒";
        this.tileType = 0;
    }

    public Tile(String inside) {
        this.tileInside = inside;
        this.tileType = 0;
    }

    public Tile(String inside, int type) {
        this.tileInside = inside;
        this.tileType = type;
    }

    public void setTileInside(String inside) {
        this.tileInside = inside;
    }

    public String getTileInside() {
        return tileInside;
    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
    }

    public int getTileType() {
        return tileType;
    }

    public boolean containsNpc() {
        switch(tileInside) {
            case "S":
            case "D":
                return true;
            default: return false;
        }
    }

    public String toString() {
        return tileInside;
    }

}
