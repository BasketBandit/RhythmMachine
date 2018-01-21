package uk.co.codefreak.rhythmmachine.world;

import uk.co.codefreak.rhythmmachine.colour.Colour;
import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;

import java.util.Random;

public class World {

    private MapList maps;

    private Map map;
    private int time = 18000;
    private int timeMax = 36000; // 10 minutes @ 60 ticks per second. (36000)
    private NonPlayableCharacter[] npcs;
    private String notification = "";
    private boolean initialised = false;

    public World(int no) {
        this.maps = new MapList();
        this.map = maps.getMap(no);
        this.npcs = map.getNpcs();

        update(0,0, 0);
        initNpcs();
    }

    public void changeMap(int no) {
        this.map = maps.getMap(no);
        this.npcs = map.getNpcs();

        initialised = false;

        update(0,0,0);
        initNpcs();
    }

    private void initNpcs() {
        for(int i = 0; i < npcs.length; i++) {
            // Randomly select the location and which NPCs are spawned.
            int rand = new Random().nextInt(3);
            int x = new Random().nextInt(60);
            int y = new Random().nextInt(60);

            if(rand == 0) {
                while(getTile(x,y).getTileType() == 1 || getTile(x,y).getTileType() == 2 || getTile(x,y).containsNpc()) {
                    x = new Random().nextInt(60);
                    y = new Random().nextInt(60);
                }
                npcs[i] = new NonPlayableCharacter(x,y,0);
            } else if(rand == 1) {
                while(getTile(x,y).getTileType() == 1 || getTile(x,y).containsNpc()) {
                    x = new Random().nextInt(60);
                    y = new Random().nextInt(60);
                }
                npcs[i] = new NonPlayableCharacter(x, y, 1);
            } else {
                while(getTile(x,y).getTileType() == 1 || getTile(x,y).getTileType() == 2 || getTile(x,y).containsNpc()) {
                    x = new Random().nextInt(60);
                    y = new Random().nextInt(60);
                }
                npcs[i] = new NonPlayableCharacter(x,y,2);
            }
        }
        this.initialised = true;
    }

    public void update(int playerXPos, int playerYPos, int ticksPerSecond) {

        // Deal with the time
        time = (time == timeMax) ? 0 : time + 1;

        // Water ripple
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if(map.getTile(x,y).isWater() && ticksPerSecond % 15 == 0) {
                    map.getTile(x,y).setTileColour(Colour.randomBlue());
                }
            }
        }

        // Dynamically set the waters edge to e.
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getTile(x,y).isWater()) {
                    if (x + 1 < map.getWidth() && !map.getTile(x + 1, y).isWater() && map.getTile(x + 1, y).causesShoreline()) {
                        map.getTile(x,y).setTileInternals("e", 0, Colour.SADDLE_BROWN);
                    } else if (x - 1 > -1 && !map.getTile(x - 1, y).isWater() && map.getTile(x - 1, y).causesShoreline()) {
                        map.getTile(x,y).setTileInternals("e", 0, Colour.SADDLE_BROWN);
                    } else if (y - 1 > -1 && !map.getTile(x, y - 1).isWater() && map.getTile(x, y - 1).causesShoreline()) {
                        map.getTile(x,y).setTileInternals("e", 0, Colour.SADDLE_BROWN);
                    } else if (y + 1 < map.getHeight() && !map.getTile(x, y + 1).isWater() && map.getTile(x, y + 1).causesShoreline()) {
                        map.getTile(x,y).setTileInternals("e", 0, Colour.SADDLE_BROWN);
                    }
                }
            }
        }

        // Update all the NPC locations.
        if(initialised) {
            for(NonPlayableCharacter npc : npcs) {
                int rand = new Random().nextInt(501);

                // Randomly decide if the NPC is going to move or not. (4/500)x60 -> (240/500) -> (12/25) -> (0.48)/s
                if(rand == 1 && npc.getXPos() < map.getWidth()-1 && posCheck(2, npc, playerXPos, playerYPos)) {
                    npc.incXPos();
                } else if(rand == 2 && npc.getXPos() > 0 && posCheck(0, npc, playerXPos, playerYPos)) {
                    npc.decXPos();
                } else if(rand == 3 && npc.getYPos() < map.getHeight()-1 && posCheck(3, npc, playerXPos, playerYPos)) {
                    npc.incYPos();
                } else if(rand == 4 && npc.getYPos() > 0 && posCheck(1, npc, playerXPos, playerYPos)) {
                    npc.decYPos();
                }

                // If its dark and the NPC is within viewing distance, display, else don't.
                if(isNight() && !npc.isDistanceFromTile(playerXPos,playerYPos,5)) {
                    map.getTile(npc.getXPos(), npc.getYPos()).setTileCharacter(npc.toString());
                } else if(!isNight()) {
                    map.getTile(npc.getXPos(), npc.getYPos()).setTileCharacter(npc.toString());
                }
            }
        }

    }

    private boolean posCheck(int direction, NonPlayableCharacter npc, int playerXPos, int playerYPos) {

        if(direction == 0) {
            if(npc.getXPos()-1 == playerXPos && npc.getYPos() == playerYPos) {
            } else if(getTile(npc.getXPos()-1,npc.getYPos()).containsNpc()) {
            } else if(getTile(npc.getXPos()-1,npc.getYPos()).isSolid()) {
            } else if(getTile(npc.getXPos()-1,npc.getYPos()).isWater() && !npc.canSwim()) {
            } else {
                return true;
            }
        } else if(direction == 1) {
            if(npc.getXPos() == playerXPos && npc.getYPos()-1 == playerYPos) {
            } else if(getTile(npc.getXPos(),npc.getYPos()-1).containsNpc()) {
            } else if(getTile(npc.getXPos(),npc.getYPos()-1).isSolid()) {
            } else if(getTile(npc.getXPos(),npc.getYPos()-1).isWater() && !npc.canSwim()) {
            } else {
                return true;
            }
        } else if(direction == 2) {
            if(npc.getXPos()+1 == playerXPos && npc.getYPos() == playerYPos) {
            } else if(getTile(npc.getXPos()+1,npc.getYPos()).containsNpc()) {
            } else if(getTile(npc.getXPos()+1,npc.getYPos()).isSolid()) {
            } else if(getTile(npc.getXPos()+1,npc.getYPos()).isWater() && !npc.canSwim()) {
            } else {
                return true;
            }
        } else if(direction == 3) {
            if(npc.getXPos() == playerXPos && npc.getYPos()+1 == playerYPos) {
            } else if(getTile(npc.getXPos(),npc.getYPos()+1).containsNpc()) {
            } else if(getTile(npc.getXPos(),npc.getYPos()+1).isSolid()) {
            } else if(getTile(npc.getXPos(),npc.getYPos()+1).isWater() && !npc.canSwim()) {
            } else {
                return true;
            }
        }
            return false;
    }

    // Time methods

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeMax() {
        return timeMax;
    }

    public boolean isNight() {
        return time > -1 && time < (timeMax/2);
    }

    public boolean isDay() {
        return time < timeMax && time > (timeMax/2);
    }

    public int timeUntilDay() {
        return ((timeMax/2) - time) / 60;
    }

    public int timeUntilNight() {
        return (timeMax - time) / 60;
    }

    public String getTimeString() {
        return (isNight()) ? timeUntilDay() + " seconds until day." : timeUntilNight() + " seconds until night.";
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

    public NonPlayableCharacter getNpcByPos(int x, int y) {
        for(NonPlayableCharacter npc: npcs) {
            if(npc.getXPos() == x && npc.getYPos() == y) {
                return npc;
            }
        }
        return null;
    }

    public int getStartXPos() {
        return map.getStartXPos();
    }

    public int getStartYPos() {
        return map.getStartYPos();
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String text) {
        this.notification = text;
    }

}
