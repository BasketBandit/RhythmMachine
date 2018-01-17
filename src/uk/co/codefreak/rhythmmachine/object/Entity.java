package uk.co.codefreak.rhythmmachine.object;

public class Entity {

    private int id;
    private String name;

    // Type -> 0 = Player, 1 = NPC, 2 = Item
    private int type;

    // Physics Type -> 0 = Not Solid (haha), 1 = Solid
    private int physType;

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

    public int getPhysType() {
        return physType;
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

    public void setPhysType(int physType) {
        this.physType = physType;
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
