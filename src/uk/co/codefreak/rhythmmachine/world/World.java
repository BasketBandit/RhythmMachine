package uk.co.codefreak.rhythmmachine.world;

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
                if(tiles[x][y].getInside() == "W") {
                    tiles[x][y].setType(1);
                }
            }
        }
    }

    public void update() {
        tiles[10][2].setInside("W");
        tiles[11][2].setInside("W");
        tiles[12][2].setInside("W");
        tiles[13][2].setInside("W");
        tiles[14][2].setInside("W");

        for(int x = 30; x < 40; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y].setInside("m");
            }
        }

        for(int x = 30; x < 40; x++) {
            for(int y = 0; y < height; y++) {
                if(x + 1 < width && tiles[x+1][y].getInside() != "m" && tiles[x+1][y].getInside() != "M" && tiles[x+1][y].getInside() != "E") {
                    tiles[x][y].setInside("E");
                } else if(x - 1 > -1 && tiles[x-1][y].getInside() != "m" && tiles[x-1][y].getInside() != "M" && tiles[x-1][y].getInside() != "E") {
                    tiles[x][y].setInside("E");
                } else if(y - 1 > -1 && tiles[x][y-1].getInside() != "m" && tiles[x][y-1].getInside() != "M" && tiles[x][y-1].getInside() != "E") {
                    tiles[x][y].setInside("E");
                } else if(y + 1 < height && tiles[x][y+1].getInside() != "m" && tiles[x][y+1].getInside() != "M" && tiles[x][y+1].getInside() != "E") {
                    tiles[x][y].setInside("E");
                }
            }
        }

    }

}
