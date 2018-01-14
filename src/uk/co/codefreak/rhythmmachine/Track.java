package uk.co.codefreak.rhythmmachine;

import java.io.*;
import java.util.ArrayList;

public class Track implements java.io.Serializable {

    private String timeline = "";
    private String title;
    private String author;
    private int beatsPerMinute;
    private int trackLength;
    private ArrayList<NodeGroup> nodeGroups;

    public Track(String filename) {

        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String s;
            int x = 1;

            title = in.readLine();
            author = in.readLine();
            trackLength = Integer.parseInt(in.readLine());
            beatsPerMinute = Integer.parseInt(in.readLine());

            nodeGroups = new ArrayList<>(trackLength*beatsPerMinute);

            for (s = in.readLine(); s != null; s = in.readLine()) {
                String[] nodeIn = s.split(" ");

                int nodeParse1 = Integer.parseInt(nodeIn[0]);
                int nodeParse2 = Integer.parseInt(nodeIn[1]);
                int nodeParse3 = Integer.parseInt(nodeIn[2]);
                int nodeParse4 = Integer.parseInt(nodeIn[3]);
                int nodeParse5 = Integer.parseInt(nodeIn[4]);
                int nodeSum = (nodeParse1 + nodeParse2 + nodeParse3 + nodeParse4 + nodeParse5);

                if (nodeSum != 0) {
                    nodeGroups.add(new NodeGroup(new Node(x, nodeParse1), new Node(x+1, nodeParse2), new Node(x+2, nodeParse3), new Node(x+3, nodeParse4), new Node(x+4, nodeParse5)));
                } else {
                    Node nullNode = new Node(0, 0);
                    nodeGroups.add(new NodeGroup(nullNode, nullNode, nullNode, nullNode, nullNode));
                }

                timeline += s + "\n";

                x+=5;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            System.exit(1);
        }
        catch (IOException e) {
            System.exit(2);
        }
    }

    public String toString() {
        return timeline;
    }

}
