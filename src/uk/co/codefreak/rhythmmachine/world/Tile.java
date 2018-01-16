package uk.co.codefreak.rhythmmachine.world;

public class Tile {

    private String inside = "0";
    private int type = 0;

    public Tile() {
        this.inside = "0";
        this.type = 0;
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

}
