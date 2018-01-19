package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;

import java.util.Random;

public class World {

    private MapList maps;

    private Map map;
    private NonPlayableCharacter[] npcs;

    private boolean initialised = false;

    public World(int world) {

        this.maps = new MapList();
        this.map = maps.getMap(world);
        this.npcs = map.getNpcs();

        update(0,0);
        initNpcs();
        initTypes();
    }

    private void initNpcs() {
        for(int i = 0; i < npcs.length; i++) {
            // Randomly select the location and which NPCs are spawned.
            int rand = new Random().nextInt(2);
            if(rand == 0) {
                int x = new Random().nextInt(50);
                int y = new Random().nextInt(50);

                while(getTile(x,y).getTileType() == 1 || getTile(x,y).getTileType() == 2) {
                    x = new Random().nextInt(50);
                    y = new Random().nextInt(50);
                }

                npcs[i] = new NonPlayableCharacter(x,y,0);
            } else {
                npcs[i] = new NonPlayableCharacter(new Random().nextInt(50), new Random().nextInt(50), 1);
            }
        }
        this.initialised = true;
    }

    private void initTypes() {
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {

                // If wall, make solid.
                String s = map.getTile(x,y).getTileInside();
                if(s.equals("B") || s.equals("▒")) {
                    map.getTile(x,y).setTileType(1);
                }

            }
        }
    }

    public void update(int playerXPos, int playerYPos) {

        // Give the water a ripple effect using Random(). (It looks really nice!)
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {
                if(new Random().nextInt(15) == 1 && map.getTile(x,y).getTileInside().equals("w")) {
                    map.getTile(x,y).setTileInside("W");
                }
            }
        }

        // Dynamically set the waters edge to E.
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {
                if(map.getTile(x,y).getTileInside().equals("w") || map.getTile(x,y).getTileInside().equals("W")) {
                    if (x + 1 < map.getWidth() && !map.getTile(x+1,y).getTileInside().equals("w") && !map.getTile(x+1,y).getTileInside().equals("W") && !map.getTile(x+1,y).getTileInside().equals("E")) {
                        map.getTile(x,y).setTileInside("E");
                    } else if (x - 1 > -1 && !map.getTile(x-1,y).getTileInside().equals("w") && !map.getTile(x-1,y).getTileInside().equals("W") && !map.getTile(x-1,y).getTileInside().equals("E")) {
                        map.getTile(x,y).setTileInside("E");
                    } else if (y - 1 > -1 && !map.getTile(x,y-1).getTileInside().equals("w") && !map.getTile(x,y-1).getTileInside().equals("W") && !map.getTile(x,y-1).getTileInside().equals("E")) {
                        map.getTile(x,y).setTileInside("E");
                    } else if (y + 1 < map.getHeight() && !map.getTile(x,y+1).getTileInside().equals("w") && !map.getTile(x,y+1).getTileInside().equals("W") && !map.getTile(x,y+1).getTileInside().equals("E")) {
                        map.getTile(x,y).setTileInside("E");
                    }

                    if(map.getTile(x,y).getTileType() != 0 && map.getTile(x,y).getTileInside().equals("E")) {
                        map.getTile(x, y).setTileType(0);
                    }
                }
            }
        }

        // Update all the NPC locations.
        if(initialised) {
            for (int a = 0; a <= npcs.length - 1; a++) {

                int rand = new Random().nextInt(500);
                if(rand == 1 && npcs[a].getXPos() < map.getWidth()-1 && posCheck(2, a, playerXPos, playerYPos)) {
                    npcs[a].incXPos();
                } else if(rand == 2 && npcs[a].getXPos() > 0 && posCheck(0, a, playerXPos, playerYPos)) {
                    npcs[a].decXPos();
                } else if(rand == 3 && npcs[a].getYPos() < map.getHeight()-1 && posCheck(3, a, playerXPos, playerYPos)) {
                    npcs[a].incYPos();
                } else if(rand == 4 && npcs[a].getYPos() > 0 && posCheck(1, a, playerXPos, playerYPos)) {
                    npcs[a].decYPos();
                }

                map.getTile(npcs[a].getXPos(),npcs[a].getYPos()).setTileInside(npcs[a].toString());
            }
        }

    }

    private boolean posCheck(int direction, int a, int playerXPos, int playerYPos) {

        int npcType = npcs[a].getNpcType();

        if(direction == 0) {
            if(npcs[a].getXPos()-1 == playerXPos && npcs[a].getYPos() == playerYPos) {
            } else if(getTile(npcs[a].getXPos()-1,npcs[a].getYPos()).containsNpc()) {
            } else if(getTile(npcs[a].getXPos()-1,npcs[a].getYPos()).getTileType() == 1) {
            } else if(getTile(npcs[a].getXPos()-1,npcs[a].getYPos()).getTileType() == 2 && npcType == 0) {
            } else {
                return true;
            }
        } else if(direction == 1) {
            if(npcs[a].getXPos() == playerXPos && npcs[a].getYPos()-1 == playerYPos) {
            } else if(getTile(npcs[a].getXPos(),npcs[a].getYPos()-1).containsNpc()) {
            } else if(getTile(npcs[a].getXPos(),npcs[a].getYPos()-1).getTileType() == 1) {
            } else if(getTile(npcs[a].getXPos(),npcs[a].getYPos()-1).getTileType() == 2 && npcType == 0) {
            } else {
                return true;
            }
        } else if(direction == 2) {
            if(npcs[a].getXPos()+1 == playerXPos && npcs[a].getYPos() == playerYPos) {
            } else if(getTile(npcs[a].getXPos()+1,npcs[a].getYPos()).containsNpc()) {
            } else if(getTile(npcs[a].getXPos()+1,npcs[a].getYPos()).getTileType() == 1) {
            } else if(getTile(npcs[a].getXPos()+1,npcs[a].getYPos()).getTileType() == 2 && npcType == 0) {
            } else {
                return true;
            }
        } else if(direction == 3) {
            if(npcs[a].getXPos() == playerXPos && npcs[a].getYPos()+1 == playerYPos) {
            } else if(getTile(npcs[a].getXPos(),npcs[a].getYPos()+1).containsNpc()) {
            } else if(getTile(npcs[a].getXPos(),npcs[a].getYPos()+1).getTileType() == 1) {
            } else if(getTile(npcs[a].getXPos(),npcs[a].getYPos()+1).getTileType() == 2 && npcType == 0) {
            } else {
                return true;
            }
        }
            return false;
    }

    // Getters

    public int mapsTotal() {
        return maps.mapsTotal();
    }

    public Tile[][] getTiles() {
        return map.getTiles();
    }

    public Tile getTile(int x, int y) {
        return map.getTile(x,y);
    }

    public int getWidth() {
        return map.getWidth();
    }

    public int getHeight() {
        return map.getHeight();
    }

    public NonPlayableCharacter[] getNpcs() {
        return npcs;
    }

    public int getStartXPos() {
        return map.getStartXPos();
    }

    public int getStartYPos() {
        return map.getStartYPos();
    }
}
