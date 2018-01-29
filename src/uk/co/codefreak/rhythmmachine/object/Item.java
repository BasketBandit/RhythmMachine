package uk.co.codefreak.rhythmmachine.object;

public class Item extends Entity {

    public static final Item KEY = new Item("key", 'k');

    public Item() {
    }

    public Item(String name, char character) {
        setName(name);
        setCharacter(character);
    }

    public String toString() {
        return super.toString();
    }

}
