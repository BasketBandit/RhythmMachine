package uk.co.codefreak.rhythmmachine.object;

public class Item extends Entity {

    public static final Item NOTHING = new Item("", "");
    public static final Item KEY = new Item("key", "k");

    private String name;
    private String charName;

    public Item(String name, String charName) {
        this.name = name;
        this.charName = charName;
    }

    public String toString() {
        return charName;
    }

}
