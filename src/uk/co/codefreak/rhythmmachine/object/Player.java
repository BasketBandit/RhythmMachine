package uk.co.codefreak.rhythmmachine.object;

import uk.co.codefreak.rhythmmachine.colour.Colour;

public class Player extends Entity {

    private int gender;
    private Item[] inventory = new Item[24];
    private Item[] equipment = new Item[10];

    public Player(String name, int gender) {
        this.setName(name);
        this.setEntityType(0);
        this.setPhysType(1);
        this.gender = gender;
        this.setEntityColour(Colour.WHITE);

        for(int i = 0; i < inventory.length; i++) {
            inventory[i] = Item.NOTHING;
        }

        for(int i = 0; i < equipment.length; i++) {
            equipment[i] = Item.NOTHING;
        }
    }

    public Player(Player player) {
        this.setName(player.getName());
        this.setEntityType(player.getEntityType());
        this.setPhysType(player.getPhysType());
        this.gender = player.getGenderInt();
        this.setEntityColour(player.getEntityColour());

        for (int i = 0; i < player.getInventory().length; i++) {
            this.inventory[i] = player.getInventoryItem(i);
        }

        this.setXPos(player.getXPos());
        this.setYPos(player.getYPos());
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

    public int getGenderInt() {
        return gender;
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

    public boolean hasItem(Item requestedItem) {
        for(Item item: inventory) {
            if(item == requestedItem) {
                return true;
            }
        }
        return false;
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
