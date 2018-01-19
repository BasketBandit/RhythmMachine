package uk.co.codefreak.rhythmmachine.object;

public class Item extends Entity {

    public static final Item NOTHING = new Item("", "");
    public static final Item KEY = new Item("key", "k");

    private String itemName;
    private String charName;

    public Item(String itemName, String charName) {
        this.itemName = itemName;
        this.charName = charName;
    }

    public String toString() {
        return charName;
    }

}
