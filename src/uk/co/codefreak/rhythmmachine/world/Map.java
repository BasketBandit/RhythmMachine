package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;

import java.io.*;

public class Map implements Serializable {

    private String name;
    private int width;
    private int height;
    private Tile[][] tiles;
    private NonPlayableCharacter[] npcs;

    private int startPosX;
    private int startPosY;

    public Map(String name) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(name));

            this.setName(in.readLine());
            this.setWidth(Integer.parseInt(in.readLine()));
            this.setHeight(Integer.parseInt(in.readLine()));
            this.setStartPosX(Integer.parseInt(in.readLine()));
            this.setStartPosY(Integer.parseInt(in.readLine()));
            this.npcs = new NonPlayableCharacter[Integer.parseInt(in.readLine())];

            this.tiles = new Tile[getWidth()][getHeight()];

            for(int y = 0; y < getHeight(); y++) {
                for(int x = 0; x < getWidth(); x++) {

                    int s = in.read();

                    while(s == 13 || s == 10) {
                        s = in.read();
                    }

                    if(s != 13 && s != 10) {
                        char c = (char) s;
                        String inside = Character.toString(c);
                        if(inside.equals("w")) {
                            this.tiles[x][y] = new Tile(inside, 2);
                        } else {
                            this.tiles[x][y] = new Tile(inside);
                        }
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
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public Tile getTile(int x, int y) {
        return this.tiles[x][y];
    }

    public void setTiles(int x, int y, String inside) {
        this.tiles[x][y].setInside(inside);
    }

    public NonPlayableCharacter[] getNpcs() {
        return this.npcs;
    }

    public void setNpcs(NonPlayableCharacter[] npcs) {
        this.npcs = npcs;
    }

    public int getStartPosX() {
        return startPosX;
    }

    public void setStartPosX(int startPosX) {
        this.startPosX = startPosX;
    }

    public int getStartPosY() {
        return startPosY;
    }

    public void setStartPosY(int startPosY) {
        this.startPosY = startPosY;
    }
}
