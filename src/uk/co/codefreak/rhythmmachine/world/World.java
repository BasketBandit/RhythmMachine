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

                while(getTile(x,y).getType() == 1 || getTile(x,y).getType() == 2) {
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
                String s = map.getTile(x,y).getInside();
                if(s.equals("B") || s.equals("▒")) {
                    map.getTile(x,y).setType(1);
                }

            }
        }
    }

    public void update(int playerX, int playerY) {

        // Give the water a ripple effect using Random(). (It looks really nice!)
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {
                if(new Random().nextInt(15) == 1 && map.getTile(x,y).getInside().equals("w")) {
                    map.getTile(x,y).setInside("W");
                }
            }
        }

        // Dynamically set the waters edge to E.
        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {
                if(map.getTile(x,y).getInside().equals("w") || map.getTile(x,y).getInside().equals("W")) {
                    if (x + 1 < map.getWidth() && !map.getTile(x+1,y).getInside().equals("w") && !map.getTile(x+1,y).getInside().equals("W") && !map.getTile(x+1,y).getInside().equals("E")) {
                        map.getTile(x,y).setInside("E");
                    } else if (x - 1 > -1 && !map.getTile(x-1,y).getInside().equals("w") && !map.getTile(x-1,y).getInside().equals("W") && !map.getTile(x-1,y).getInside().equals("E")) {
                        map.getTile(x,y).setInside("E");
                    } else if (y - 1 > -1 && !map.getTile(x,y-1).getInside().equals("w") && !map.getTile(x,y-1).getInside().equals("W") && !map.getTile(x,y-1).getInside().equals("E")) {
                        map.getTile(x,y).setInside("E");
                    } else if (y + 1 < map.getHeight() && !map.getTile(x,y+1).getInside().equals("w") && !map.getTile(x,y+1).getInside().equals("W") && !map.getTile(x,y+1).getInside().equals("E")) {
                        map.getTile(x,y).setInside("E");
                    }

                    if(map.getTile(x,y).getType() != 0 && map.getTile(x,y).getInside().equals("E")) {
                        map.getTile(x, y).setType(0);
                    }
                }
            }
        }

        // Update all the NPC locations.
        if(initialised) {
            for (int a = 0; a <= npcs.length - 1; a++) {

                int rand = new Random().nextInt(500);
                if(rand == 1 && npcs[a].getX() < map.getWidth()-1 && posCheck(2, a, playerX, playerY)) {
                    npcs[a].incX();
                } else if(rand == 2 && npcs[a].getX() > 0 && posCheck(0, a, playerX, playerY)) {
                    npcs[a].decX();
                } else if(rand == 3 && npcs[a].getY() < map.getHeight()-1 && posCheck(3, a, playerX, playerY)) {
                    npcs[a].incY();
                } else if(rand == 4 && npcs[a].getY() > 0 && posCheck(1, a, playerX, playerY)) {
                    npcs[a].decY();
                }

                map.getTile(npcs[a].getX(),npcs[a].getY()).setInside(npcs[a].toString());
            }
        }

    }

    private boolean posCheck(int direction, int a, int playerX, int playerY) {

        int npcType = npcs[a].getNpcType();

        if(direction == 0 && npcs[a].getX()-1 == playerX && npcs[a].getY() == playerY) {
            return false;
        } else if (direction == 1 && npcs[a].getX() == playerX && npcs[a].getY()-1 == playerY) {
            return false;
        } else if (direction == 2 && npcs[a].getX()+1 == playerX && npcs[a].getY() == playerY) {
            return false;
        } else if(direction == 3 && npcs[a].getX() == playerX && npcs[a].getY()+1 == playerY) {
            return false;
        }

        if(direction == 0 && getTile(npcs[a].getX()-1,npcs[a].getY()).getType() == 1) {
            return false;
        } else if(direction == 1 && getTile(npcs[a].getX(),npcs[a].getY()-1).getType() == 1) {
            return false;
        } else if(direction == 2 && getTile(npcs[a].getX()+1,npcs[a].getY()).getType() == 1) {
            return false;
        } else if(direction == 3 && getTile(npcs[a].getX(),npcs[a].getY()+1).getType() == 1)  {
            return false;
        }

        if(direction == 0 && getTile(npcs[a].getX()-1,npcs[a].getY()).getType() == 2 && npcType == 0) {
            return false;
        } else if(direction == 1 && getTile(npcs[a].getX(),npcs[a].getY()-1).getType() == 2 && npcType == 0) {
            return false;
        } else if(direction == 2 && getTile(npcs[a].getX()+1,npcs[a].getY()).getType() == 2 && npcType == 0) {
            return false;
        } else if(direction == 3 && getTile(npcs[a].getX(),npcs[a].getY()+1).getType() == 2 && npcType == 0) {
            return false;
        }

        return true;
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

    public int getStartPosX() {
        return map.getStartPosX();
    }

    public int getStartPosY() {
        return map.getStartPosY();
    }
}
