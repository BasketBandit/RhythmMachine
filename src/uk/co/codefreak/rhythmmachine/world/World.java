package uk.co.codefreak.rhythmmachine.world;

import java.util.Random;

public class World {

    private Tile[][] tiles;
    private int width;
    private int height;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y] = new Tile();
            }
        }

        update();
        initType();
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

    public void initType() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(tiles[x][y].getInside() == "B") {
                    tiles[x][y].setType(1);
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

        for(int x = 30; x < 40; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y].setInside("w");
                if(new Random().nextInt(11) == 1) {
                    tiles[x][y].setInside("W");
                }
            }
        }

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

    }

}
