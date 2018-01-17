package uk.co.codefreak.rhythmmachine.object;

public class Player extends Entity {

    private int gender;
    private Item[] inventory = new Item[24];

    public Player(String name, int gender) {
        this.setName(name);
        this.setType(0);
        this.setPhysType(1);
        this.gender = gender;

        for(int i = 0; i < inventory.length; i++) {
            inventory[i] = null;
        }
    }

    public String getGender() {
        switch(gender) {
            case 0: return "Male";
            case 1: return "Female";
            default: return "Apache Attack Helicopter";
        }
    }

}
