package uk.co.codefreak.rhythmmachine;

public class Node implements java.io.Serializable {

    private int id;
    private int type;

    public Node(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public String toString() {
        return ""+type;
    }

}
