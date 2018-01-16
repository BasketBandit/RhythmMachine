package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.object.*;

import java.util.Random;

public class World {

    private Tile[][] tiles;
    private Npc[] npcs;
    private int width;
    private int height;

    private boolean initialised = false;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
        this.npcs = new Npc[2];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y] = new Tile();
            }
        }

        update();
        initNpcs();
        initTypes();

        this.initialised = true;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Npc[] getNpcs() {
        return npcs;
    }

    public void initNpcs() {
        npcs[0] = new Npc(new Random().nextInt(40)+50,new Random().nextInt(20)+5,0);
        npcs[1] = new Npc(new Random().nextInt(40)+50,new Random().nextInt(20)+5,0);
        tiles[npcs[0].getX()][npcs[0].getY()].setInside("S");
        tiles[npcs[1].getX()][npcs[1].getY()].setInside("S");
    }

    public void initTypes() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(tiles[x][y].getInside() == "B") {
                    tiles[x][y].setType(1);
                }

                if(tiles[x][y].getInside() == "S") {

                }
            }
        }
    }

    public void update() {
        tiles[10][2].setInside("B");
        tiles[11][2].setInside("B");
        tiles[12][2].setInside("B");
        tiles[13][2].setInside("B");
        tiles[14][2].setInside("B");

        // Give the water a ripple effect using Random(). (It looks really nice!)
        for(int x = 30; x < 40; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y].setInside("w");
                if(new Random().nextInt(11) == 1) {
                    tiles[x][y].setInside("W");
                }
            }
        }

        // Dynamically set the waters edge to E.
        for(int x = 30; x < 40; x++) {
            for(int y = 0; y < height; y++) {
                if(x + 1 < width && tiles[x+1][y].getInside() != "w" && tiles[x+1][y].getInside() != "W" && tiles[x+1][y].getInside() != "E") {
                    tiles[x][y].setInside("E");
                } else if(x - 1 > -1 && tiles[x-1][y].getInside() != "w" && tiles[x-1][y].getInside() != "W" && tiles[x-1][y].getInside() != "E") {
                    tiles[x][y].setInside("E");
                } else if(y - 1 > -1 && tiles[x][y-1].getInside() != "w" && tiles[x][y-1].getInside() != "W" && tiles[x][y-1].getInside() != "E") {
                    tiles[x][y].setInside("E");
                } else if(y + 1 < height && tiles[x][y+1].getInside() != "w" && tiles[x][y+1].getInside() != "W" && tiles[x][y+1].getInside() != "E") {
                    tiles[x][y].setInside("E");
                }
            }
        }

        // Update all the NPC locations.
        if(initialised) {
            for (int a = 0; a <= npcs.length - 1; a++) {

                if(new Random().nextInt(500) == 1 && npcs[a].getX() < width-1) {
                    npcs[a].incX();
                } else if(new Random().nextInt(500) == 2 && npcs[a].getX() > 0) {
                    npcs[a].decX();
                } else if(new Random().nextInt(500) == 3 && npcs[a].getY() < height-1) {
                    npcs[a].incY();
                } else if(new Random().nextInt(500) == 4 && npcs[a].getY() > 0) {
                    npcs[a].decY();
                }

                tiles[npcs[a].getX()][npcs[a].getY()].setInside(npcs[a].toString());
            }
        }
    }
}
