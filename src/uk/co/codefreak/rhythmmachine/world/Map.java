package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.colour.Colour;
import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;

import java.io.*;

public class Map implements Serializable {

    private String name;
    private int width;
    private int height;
    private Tile[][] tiles;
    private NonPlayableCharacter[] npcs;

    private String[] connectedMaps;

    private int startXPos;
    private int startYPos;

    public Map(String map) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(map));

            // Name
            in.readLine();
            this.setName(in.readLine());

            // Dimensions
            in.readLine();
            this.setWidth(Integer.parseInt(in.readLine()));
            this.setHeight(Integer.parseInt(in.readLine()));
            this.tiles = new Tile[getWidth()][getHeight()];

            // Starting coordinates
            in.readLine();
            this.setStartXPos(Integer.parseInt(in.readLine()));
            this.setStartYPos(Integer.parseInt(in.readLine()));

            // Number of NPCs
            in.readLine();
            this.npcs = new NonPlayableCharacter[Integer.parseInt(in.readLine())];

            // Connected Maps
            in.readLine();
            int loopTotal = Integer.parseInt(in.readLine());
            connectedMaps = new String[4+loopTotal];

            // Loops 4 times to find the 4 mandatory directional maps (null if no map);
            for(int i = 0; i < 4; i++) {
                connectedMaps[i] = in.readLine();
            }
            for(int i = 4; i < 4+loopTotal; i++) {
                connectedMaps[i] = in.readLine();
            }

            in.readLine();
            initTiles(in, loopTotal);

        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }
        catch (IOException e) {
            System.exit(2);
        }
    }

    private void initTiles(BufferedReader in, int loopTotal) {
        try {
            boolean flip = true;
            int[][] doorCoords = new int[0][0];
            String coord;
            String[] coords;

            // Loop through the door coordinates, putting them into an int array.
            if(loopTotal != 0) {
                doorCoords = new int[loopTotal][2];
                for (int i = 0; i < loopTotal; i++) {
                    coord = in.readLine();
                    coords = coord.split(",");
                    doorCoords[i][0] = Integer.parseInt(coords[0]);
                    doorCoords[i][1] = Integer.parseInt(coords[1]);
                }
            }

            // Tile Data
            in.readLine();

            for(int y = 0; y < getHeight(); y++) {
                flip = !flip;

                for (int x = 0; x < getWidth(); x++) {
                    flip = !flip;

                    int s = in.read();

                    while(s == 13 || s == 10) {
                        s = in.read();
                    }

                    if (s != 13 && s != 10) {
                        char c = (char) s;
                        String character = Character.toString(c);
                        if(character.equals("n")) {
                            this.tiles[x][y] = (flip) ? new Tile(character, 0, Colour.GREEN_BB) : new Tile(character, 0, Colour.GREEN_99);
                        } else if(character.equals("w")) {
                            this.tiles[x][y] = new Tile(character, 2, Colour.BLUE_BB);
                        } else if(character.equals("B")) {
                            this.tiles[x][y] = new Tile(character, 1, Colour.RED_BB);
                        } else if(character.equals("c")) {
                            this.tiles[x][y] = new Tile(character, 0, Colour.GREY_70);
                        } else if(character.equals("@")) {
                            this.tiles[x][y] = new Tile(character, 0, Colour.GREY_B0);
                        } else if(character.equals("K")) {
                            this.tiles[x][y] = new Tile(character, 1, Colour.GREY_30);
                        } else if(character.equals("O")) {
                            this.tiles[x][y] = new Tile(character, 1, Colour.GREY_70);
                        } else if(character.equals("X")) {
                            // Check which map the current x:y is connected to, creating a tile with that data.
                            String connectedMap = "";
                            for(int i = 0; i < doorCoords.length; i++) {
                                if(x == doorCoords[i][0] && y == doorCoords[i][1]) {
                                    connectedMap = connectedMaps[4+i];
                                }
                            }
                            this.tiles[x][y] = new Tile(character, 3, Colour.WHITE, connectedMap);
                        } else {
                            this.tiles[x][y] = new Tile();
                        }
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
     }

    // Setters & Getters

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return this.width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public Tile getTile(int x, int y) {
        return this.tiles[x][y];
    }

    public int getTotalConnectedMaps() {
        int count = 0;
        for(String map : connectedMaps) {
            if(!map.equals("null")) {
                count++;
            }
        }
        return count;
    }

    public String[] getConnectedMaps() {
        return connectedMaps;
    }

    public String getConnectedMap(int arrayPos) {
        return connectedMaps[arrayPos];
    }

    public String getConnectedMap(String name) {
        for(String map: connectedMaps) {
            if(map.equals(name)) {
                return map;
            }
        }
        return null;
    }

    public void setTiles(int x, int y, String tileCharacter) {
        this.tiles[x][y].setTileCharacter(tileCharacter);
    }

    public NonPlayableCharacter[] getNpcs() {
        return this.npcs;
    }

    public void setNpcs(NonPlayableCharacter[] npcs) {
        this.npcs = npcs;
    }

    public int getStartXPos() {
        return startXPos;
    }

    private void setStartXPos(int startXPos) {
        this.startXPos = startXPos;
    }

    public int getStartYPos() {
        return startYPos;
    }

    private void setStartYPos(int startYPos) {
        this.startYPos = startYPos;
    }
}
