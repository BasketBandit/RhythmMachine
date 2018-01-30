package uk.co.codefreak.rhythmmachine.object;

import uk.co.codefreak.rhythmmachine.colour.Colour;

import java.awt.*;

public class Equippable extends Item {

    // Nothing
    public static final Equippable NOTHING = new Equippable("", Colour.TRANSPARENT, ' ', -1,-1,0.0,0.0,-1,0.0);

    // Imaginary
    public static final Equippable IMAGINARY_SWORD = new Equippable("Imaginary sword", Colour.TRANSPARENT,' ', 0,1,0.0,0.0,-1,0.0);

    // Wooden
    public static final Equippable WOODEN_SWORD = new Equippable("Wooden sword", Colour.SADDLE_BROWN, 'I',5,1,1.0,0.0,-1,2.0);
    public static final Equippable WOODEN_SHIELD = new Equippable("Wooden shield", Colour.SADDLE_BROWN, 'O',7,1,0.0,3.0,-1,5.0);
    public static final Equippable WODDEN_HELMET = new Equippable("Wooden helmet", Colour.SADDLE_BROWN, 'M', 0, 1, 0.0, 1.0, -1, 2.0);
    public static final Equippable WOODEN_CUIRASS = new Equippable("Wooden cuirass", Colour.SADDLE_BROWN, 'T', 2,1,0.0,1.0, -1, 5.0);
    public static final Equippable WOODEN_GREAVES = new Equippable("Wooden greaves", Colour.SADDLE_BROWN, 'H', 3, 1, 0.0, 1.0, -1, 4.0);
    public static final Equippable WOODEN_GAUNTLETS = new Equippable("Wooden gauntlets", Colour.SADDLE_BROWN, 'P', 6, 1, 0.0, 1.0,-1,1.0);
    public static final Equippable WOODEN_BOOTS = new Equippable("Wooden boots", Colour.SADDLE_BROWN, 'F', 4, 1, 0.0, 1.0, -1, 2.0);

    private int equiptmentSlot;
    private int level;
    private double attackValue;
    private double defenseValue;
    private double weight;
    private double durability;

    public Equippable(String name, Color colour, char character, int slot, int level, double attack, double defense, double durability, double weight) {
        setName(name);
        setEntityType(2);
        setEntityColour(colour);
        setCharacter(character);
        setPhysType(0);
        this.equiptmentSlot = slot;
        this.level = level;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.weight = weight;
        this.durability = durability;
    }

    public String toString() {
        return getCharacter()+"";
    }

    public int getEquiptmentSlot() {
        return equiptmentSlot;
    }

    public double getAttackValue() {
        return attackValue;
    }

    public double getDefenseValue() {
        return  defenseValue;
    }

    public double getWeight() {
        return weight;
    }

    public double getDurability() {
        return durability;
    }

    public int getLevel() {
        return level;
    }
}
