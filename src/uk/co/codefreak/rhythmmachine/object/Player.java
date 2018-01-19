package uk.co.codefreak.rhythmmachine.object;

public class Player extends Entity {

    private int gender;
    private Item[] inventory = new Item[24];

    public Player(String name, int gender) {
        this.setName(name);
        this.setEntityType(0);
        this.setPhysType(1);
        this.gender = gender;

        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = Item.KEY;
        }
    }

    public String getGender() {
        switch (gender) {
            case 0:
                return "Male";
            case 1:
                return "Female";
            default:
                return "Apache Attack Helicopter";
        }
    }


    // Inventory

    public Item[] getInventory() {
        return inventory;
    }

    public Item getInventoryItem(int slot) {
        return inventory[slot];
    }

    public void setInventory(int slot, Item item) {
        inventory[slot] = item;
    }

    public void switchInventory(int slot1, int slot2) {
        Item temp = inventory[slot2];
        inventory[slot2] = inventory[slot1];
        inventory[slot1] = temp;
    }

    public void removeInventory(int slot) {
        inventory[slot] = null;
    }

    public void removeAllInventory() {
        for (int i = 0; i < inventory.length - 1; i++) {
            inventory[i] = null;
        }
    }

}
