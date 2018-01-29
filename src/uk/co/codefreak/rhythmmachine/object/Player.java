package uk.co.codefreak.rhythmmachine.object;

import uk.co.codefreak.rhythmmachine.colour.Colour;

import java.util.ArrayList;

public class Player extends Item {

    private int gender;
    private ArrayList<Object> inventory;
    private ArrayList<Object> equipment;

    public Player(String name, int gender) {
        this.setName(name);
        this.setEntityType(0);
        this.setPhysType(1);
        this.gender = gender;
        this.setEntityColour(Colour.WHITE);

        inventory = new ArrayList<>();
        equipment = new ArrayList<>();

        inventory.add(Equippable.WOODEN_SWORD);
        for(int i = 0; i < 23; i++) {
            inventory.add(Equippable.NOTHING);
        }

        for(int i = 0; i < 10; i++) {
            equipment.add(Equippable.NOTHING);
        }
        switchEquipment(0);
    }

    public Player(Player player) {
        this.setName(player.getName());
        this.setEntityType(player.getEntityType());
        this.setPhysType(player.getPhysType());
        this.gender = player.getGenderInt();
        this.setEntityColour(player.getEntityColour());

        for (int i = 0; i < player.getInventory().size(); i++) {
            this.inventory.set(i, player.getInventoryItem(i));
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

    public ArrayList getInventory() {
        return inventory;
    }

    public Object getInventoryItem(int slot) {
        return inventory.get(slot);
    }

    public void setInventory(int slot, Entity item) {
        inventory.set(slot, item);
    }

    public void switchInventory(int slot1, int slot2) {
        Object temp = inventory.get(slot2);
        inventory.set(slot2,inventory.get(slot1));
        inventory.set(slot1, temp);
    }

    public boolean hasItem(Entity requestedItem) {
        for(Object item: inventory) {
            if(item == requestedItem) {
                return true;
            }
        }
        return false;
    }

    public void removeInventory(int slot) {
        inventory.remove(slot);
    }

    public void removeAllInventory() {
        inventory.clear();
    }

    // Equipment

    public ArrayList getEquipment() {
        return equipment;
    }

    public Object getEquipment(int slot) {
        return equipment.get(slot);
    }

    public void setEquipment(int slot, Equippable item) {
        equipment.set(slot, item);
    }


    public boolean switchEquipment(int slot1) {
        if(inventory.get(slot1) instanceof Equippable) {
            Equippable e = (Equippable) inventory.get(slot1);
            int slot2 = e.getEquiptmentSlot();
            Object temp = equipment.get(slot2);

            equipment.set(slot2, inventory.get(slot1));
            inventory.set(slot1, temp);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasEquipment(Equippable requestedItem) {
        for(Object item: equipment) {
            if(item == requestedItem) {
                return true;
            }
        }
        return false;
    }

    public void removeEquipment(int slot) {
        equipment.remove(slot);
    }

    public boolean removeToInventory(int slot) {
        if(inventory.size() < 24) {
            inventory.add(equipment.get(slot));
            return true;
        } else {
            return false;
        }
    }

    public void removeAllEquipment() {
        equipment.clear();
    }
}
