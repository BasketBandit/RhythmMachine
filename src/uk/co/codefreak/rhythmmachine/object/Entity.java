package uk.co.codefreak.rhythmmachine.object;

public class Entity {

    private static int id;
    private static String name;
    private static int type;

    private int xPos = 0;
    private int yPos = 0;

    public Entity() {
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setX(int pos) {
        xPos = pos;
    }

    public void setY(int pos) {
        yPos = pos;
    }

    public void incX() {
        xPos++;
    }

    public void incY() {
        yPos++;
    }

    public void decX() {
        xPos--;
    }

    public void decY() {
        yPos--;
    }

}
