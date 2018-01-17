package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.object.Npc;

import java.io.*;

public class Map implements Serializable {

    private String name;
    private int width;
    private int height;
    private Tile[][] tiles;
    private Npc[] npcs;

    private int startX;
    private int startY;

    public Map(String name) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(name));

            this.setName(in.readLine());
            this.setWidth(Integer.parseInt(in.readLine()));
            this.setHeight(Integer.parseInt(in.readLine()));
            this.setStartX(Integer.parseInt(in.readLine()));
            this.setStartY(Integer.parseInt(in.readLine()));
            this.npcs = new Npc[Integer.parseInt(in.readLine())];

            this.tiles = new Tile[getWidth()][getHeight()];

            for(int y = 0; y < getHeight(); y++) {
                for(int x = 0; x < getWidth(); x++) {

                    int s = in.read();

                    while(s == 13 || s == 10) {
                        s = in.read();
                    }

                    if(s != 13 && s != 10) {
                        String inside = (char) s;
                        this.tiles[x][y] = new Tile(inside);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }
        catch (IOException e) {
            System.exit(2);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Npc[] getNpcs() {
        return npcs;
    }

    public void setNpcs(Npc[] npcs) {
        this.npcs = npcs;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }
}
