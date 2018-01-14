package uk.co.codefreak.rhythmmachine;

public class NodeGroup implements java.io.Serializable {

    private Node channel1;
    private Node channel2;
    private Node channel3;
    private Node channel4;
    private Node channel5;

    public NodeGroup(Node channel1, Node channel2, Node channel3, Node channel4, Node channel5) {
        this.channel1 = channel1;
        this.channel2 = channel2;
        this.channel3 = channel3;
        this.channel4 = channel4;
        this.channel5 = channel5;
    }

    public String toString() {
        return channel1.toString() + " " + channel2.toString() + " " + channel3.toString() + " " + channel4.toString() + " " + channel5.toString();
    }
}
