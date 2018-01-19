package uk.co.codefreak.rhythmmachine.world;

import java.io.Serializable;

public class Tile implements Serializable {

    private String inside = "0";
    private int type = 0;

    public Tile() {
        this.inside = "▒";
        this.type = 0;
    }

    public Tile(String inside) {
        this.inside = inside;
        this.type = 0;
    }

    public Tile(String inside, int type) {
        this.inside = inside;
        this.type = type;
    }

    public void setInside(String inside) {
        this.inside = inside;
    }

    public String getInside() {
        return inside;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        return inside;
    }

}
