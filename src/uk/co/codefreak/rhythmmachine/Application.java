package uk.co.codefreak.rhythmmachine;

import uk.co.codefreak.rhythmmachine.colour.Colour;
import uk.co.codefreak.rhythmmachine.data.Flags;
import uk.co.codefreak.rhythmmachine.data.SaveHandler;
import uk.co.codefreak.rhythmmachine.input.KeyInput;
import uk.co.codefreak.rhythmmachine.object.Equippable;
import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;
import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.time.Time;
import uk.co.codefreak.rhythmmachine.world.MapSerialize;
import uk.co.codefreak.rhythmmachine.world.Tile;
import uk.co.codefreak.rhythmmachine.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Application extends Canvas {

    private static final String version = "0.11.1";
    private static final String title = "Rhythm Machine (" + version + ")";
    private static final int width = 800;
    private static final int height = 620;

    // Base world is used to update the map correctly.
    private World baseWorld;
    private World world;
    private Tile[][] tiles;
    private Player player;
    private Flags flags = new Flags();

    private long moveTimer = System.currentTimeMillis();
    private int notificationTimer = 0;
    private boolean renderInventory = false; // default false.
    private boolean renderEquipment = true; // default false.
    private boolean[] keyPressed = new boolean[8];

    private JFrame frame = new JFrame(title);
    private String framesPerSecondText = "0 FPS";
    private String ticksPerSecondText = "0 TPS";
    private Time time = new Time();
    private int ticksPerSecondNumber = 0;
    private int playTime = 0;

    private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private Font[] fonts = ge.getAllFonts();

    private boolean isRunning;
    private boolean[] renderScene = new boolean[2];

    private void start() {
        if(isRunning) return;
        isRunning = true;
        new Thread(run, "application-thread").start();
        // new Thread(this, "application-thread2").start(); // -> If you uncomment this, shit hits the fan.
    }

    private void stop() {
        if(!isRunning) return;
        isRunning = false;
    }

    private Runnable run = () -> {
        System.out.println("Running...");

        int refreshRate = 60; // Capped framerate.
        double target = (refreshRate + 0.0);
        double nanoPerTick = 1000000000.0 / target;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double unprocessed = 0.0;
        int framesPerSecond = 0;
        int ticksPerSecond = 0;
        boolean canRender;

        renderScene[0] = true;

        worldInit();
        frameInit(this);

        addKeyListener(new KeyInput());

        while(isRunning) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nanoPerTick;
            lastTime = now;

            if(unprocessed >= 1.0) {
                tick();
                unprocessed--;
                ticksPerSecond++;
                ticksPerSecondNumber++;
                canRender = true;
            } else {
                canRender = false; // Set this to true for an uncapped framerate. (And high CPU usage)
            }

            try {
                Thread.sleep(1); // If problems, change to 1ms.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(canRender) {
                render();
                framesPerSecond++;
            }

            if(System.currentTimeMillis() - 1000 > timer) {
                timer += 1000;
                framesPerSecondText = framesPerSecond + " FPS";
                ticksPerSecondText = ticksPerSecond + " TPS";
                framesPerSecond = 0;
                ticksPerSecond = 0;
                ticksPerSecondNumber = 0;
                if(!renderScene[0]) {
                    playTime++;
                }
            }
        }
    };

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Colour.GREY_40);
        g.fillRect(0, 0, width, height);

        Graphics2D grr = (Graphics2D) g;

        grr.setColor(Colour.GREY_30);
        grr.setFont(fonts[368].deriveFont(Font.PLAIN,12));

        // Draw application background
        for(int x = 0; x < 90; x++) {
            for(int y = 0; y < 65; y++) {
                grr.drawString("w", 1 + (9 * x), 1 + (9 * y));
            }
        }

        // Draw framerate, tickrate, version and window dimensions.
        grr.setColor(Colour.WHITE);
        grr.drawString(framesPerSecondText + " | " + ticksPerSecondText + " | " + version + " | " + width + " x " + height + " | " + "total playtime " + time.toHours(playTime) + " | " + world.mapsTotal() + " total maps" + " | " + world.getTotalConnectedMaps() + " connected maps", 10, 19);

        // START SCENE ZERO (Menu)
        if(renderScene[0]) {

            // Draw the world and everything within it.
            for (int x = 0; x < world.getWidth(); x++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    if(tiles[x][y].containsPlayer()) {
                        grr.setColor(Color.WHITE);
                    } else {
                        grr.setColor(Colour.TRANSPARENT);
                    }
                    grr.drawString(">", 10 + (9 * x), 60 + (18 * y));
                }
            }

            // Draw menu option text.
            grr.setColor(Colour.WHITE);
            grr.drawString("New Game", 25, 60);
            grr.drawString("Load Game", 25, 78);

            // Draw copyright
            grr.drawString("Copyright © 2018 Joshua Hunt", 10, 570);

        } // END SCENE ZERO

        // START SCENE ONE (Main)
        if(renderScene[1]) {

            // Draw notifications
            grr.setColor(Colour.WHITE);
            grr.drawString(world.getNotification(), 560, 60);

            // Draw clock
            grr.drawString(world.getTimeString(), 560, 40);

            // Draw character information.
            grr.drawString("Push 'g' to toggle inventory.", 560, 80);

            // Draw world.
            for (int x = 0; x < world.getWidth(); x++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    if (world.isNight() && !world.isAlwaysDay() && player.isDistanceFromTile(x, y, 5)) {
                        grr.setColor(colourCheck(x, y, true, true).darker());
                    } else {
                        grr.setColor(colourCheck(x, y, true, false).darker());
                    }
                    // Interesting concept on how to draw background tiles.
                    grr.fill3DRect(9 + (9 * x), 29 + (9 * y),10,10,false);

                    // Colour Player and NPCs
                    if (world.isNight() && !world.isAlwaysDay() && player.isDistanceFromTile(x, y, 5)) {
                        grr.setColor(colourCheck(x, y, false, true));
                    } else {
                        grr.setColor(colourCheck(x, y, false, false));
                    }
                    grr.drawString(tiles[x][y].getTileCharacter(), 10 + (9 * x), 37 + (9 * y));
                }
            }

            // Draw inventory.
            if(renderInventory) {
                grr.setColor(Colour.WHITE);
                int invX = 0;
                int invY = 0;
                for (int inv = 0; inv < player.getInventory().size(); inv++) {
                    grr.drawString(player.getInventoryItem(inv).toString(), 567 + (24 * invX), 114 + (24 * invY));
                    grr.draw3DRect(560 + (24 * invX), 100 + (24 * invY), 20, 20, false);

                    if (invX != 3) {
                        invX++;
                    } else {
                        invX = 0;
                        invY++;
                    }
                }
            }

            if(renderEquipment) {
                grr.setColor(Colour.WHITE);
                Equippable e;

                // Head, neck, body, legs, feet.
                for(int i = 0; i < 5; i++) {
                    e = (Equippable) player.getEquipment(i);
                    grr.drawString(e.toString(), 603, 274 + (24 * i));
                    grr.draw3DRect(596, 260 + (24 * i), 20, 20, false);
                }

                // Sword, hands.
                for(int i = 0; i < 2; i++) {
                    e = (Equippable) player.getEquipment(i + 5);
                    grr.drawString(e.toString(), 579, 322 + (48 * i));
                    grr.draw3DRect(572, 308 + (48 * i), 20, 20, false);
                }

                // Offhand, finger.
                for(int i = 0; i < 2; i++) {
                    e = (Equippable) player.getEquipment(i + 7);
                    grr.drawString(e.toString(), 627, 324 + (48 * i));
                    grr.draw3DRect(620, 308 + (48 * i), 20, 20, false);
                }
            }

        } // END SCENE ONE

        g.dispose();
        bs.show();
    }

    private void tick() {
        keyCheck(player.getXPos(), player.getYPos());

        // Reset the whole map.
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                tiles[x][y].setTileCharacter(baseWorld.getTile(x,y).getTileCharacter()) ;
            }
        }

        if(renderScene[1]) {
            world.update(player.getXPos(), player.getYPos(), ticksPerSecondNumber); // Update dynamic objects and draw.
        }

        tiles[player.getXPos()][player.getYPos()].setTileCharacter("λ"); // Set player position.

        // Sets notification text to "" after 3 seconds.
        notificationTimer++;
        if(notificationTimer == 180) {
            world.setNotification("");
            notificationTimer = 0;
        }
    }

    private void keyCheck(int x, int y) { // NOTE: Try to rework this feature next, it sucks.

        // REMEMBER @ NUMBER ARE IN HEX!

        // Timeout
        if(System.currentTimeMillis() - 125 > moveTimer) {
            moveTimer += 125;
            for (int i = 0; i < 4; i++) {
                keyPressed[i] = false;
            }
        }

        // Checks made: If door in front of direction is door, if the tile is a walkable type,

        // Left Arrow
        if(KeyInput.isDown(0x25) && !keyPressed[0]) {
            if(x != 0 && tiles[x-1][y].isDoor()) {
                changeMap(-1, tiles[x-1][y].getTileDoorPointer());
            } else if(x > 0 && typeCheck(0, x, y)) {
                player.decXPos();
            } else if(x == 0) {
                changeMap(0,null);
            }
            moveTimer += 125;
            keyPressed[0] = true;
        }

        // Up Arrow
        else if(KeyInput.isDown(0x26) && !keyPressed[1]) {
            if(y != 0 && tiles[x][y-1].isDoor()) {
                changeMap(-1, tiles[x][y-1].getTileDoorPointer());
            } else if(y > 0 && typeCheck(1, x, y)) {
                player.decYPos();
            } else if(y == 0) {
                changeMap(1, null);
            }
            moveTimer += 125;
            keyPressed[1] = true;
        }

        // Right Arrow
        else if(KeyInput.isDown(0x27) && !keyPressed[2]) {
            if(x != tiles.length-1 && tiles[x+1][y].isDoor()) { // Looks over the array. FIX out of bounds
                changeMap(-1, tiles[x+1][y].getTileDoorPointer());
            } else if(x < world.getWidth() - 1 && typeCheck(2, x, y)) {
                player.incXPos();
            } else if(x == world.getWidth() - 1) {
                changeMap(2, null);
            }
            moveTimer += 125;
            keyPressed[2] = true;
        }

        // Down Arrow
        else if(KeyInput.isDown(0x28) && !keyPressed[3]) {
            if(y != tiles[x].length-1 && tiles[x][y+1].isDoor()) {
                changeMap(-1, tiles[x][y+1].getTileDoorPointer());
            } else if(y < world.getHeight() - 1 && typeCheck(3, x, y)) {
                player.incYPos();
            } else if(y == world.getHeight() - 1) {
                changeMap(3,null);
            }
            moveTimer += 125;
            keyPressed[3] = true;
        }

        if(renderScene[0]) {

            // Enter
            if(KeyInput.isDown(0x0A) && !keyPressed[7]) {
                if(player.getYPos() == 0) {
                    baseWorld = new World("test");
                    world = new World("test");
                    tiles = world.getTiles();
                    playTime = 0;
                    player = new Player("Menu-chan",0);
                    player.setXPos(world.getStartXPos());
                    player.setYPos(world.getStartYPos());

                    renderScene[0] = false;
                    renderScene[1] = true;
                } else if(player.getYPos() == 1) {
                    flags = new SaveHandler().loadGame("Menu-chan");
                    if(flags != null) {
                        player = new Player(flags.PLAYER);
                        baseWorld = new World(flags.BASE_WORLD);
                        world = new World(flags.WORLD);
                        tiles = world.getTiles();
                        playTime = flags.PLAY_TIME;
                        world.setNotification("Game loaded!");
                        notificationTimer = 0;

                        renderScene[0] = false;
                        renderScene[1] = true;
                    }
                }
            } else if(KeyInput.isDown(0x0A) && keyPressed[7]) {
            } else {
                keyPressed[7] = false;
            }

        }

        if(renderScene[1]) {

            // G
            if(KeyInput.isDown(0x47) && !keyPressed[4]) {
                renderInventory = !renderInventory;
                keyPressed[4] = true;
            } else if(KeyInput.isDown(0x47) && keyPressed[4]) {
            } else {
                keyPressed[4] = false;
            }

            // E
            if(KeyInput.isDown(0x45) && !keyPressed[5]) {
                if(world != null && baseWorld != null) {
                    flags.setFlags(player, baseWorld, world, playTime);
                    new SaveHandler(player.getName(), flags);
                    world.setNotification("Game saved!");
                    notificationTimer = 0;
                }
            } else if(KeyInput.isDown(0x45) && keyPressed[5]) {
            } else {
                keyPressed[5] = false;
            }

            // F
            if(KeyInput.isDown(0x46) && !keyPressed[6]) {
                // Load the saved flags from the file, inject them into the application.
                flags = new SaveHandler().loadGame(player.getName());
                player = new Player(flags.PLAYER);
                baseWorld = new World(flags.BASE_WORLD);
                world = new World(flags.WORLD);
                tiles = world.getTiles();
                playTime = flags.PLAY_TIME;
                world.setNotification("Game loaded!");
                notificationTimer = 0;
            } else if(KeyInput.isDown(0x46) && keyPressed[6]) {
            } else {
                keyPressed[6] = false;
            }

        }

    }

    private boolean typeCheck(int direction, int x, int y) {
        // Directions: 0 -> Left, 1 -> Up, 2 -> Right, 3 -> Down.
        // NOTE: ONCE IDS ARE IMPLEMENTED CHECK BY ID AND NOT ARRAY POS

        NonPlayableCharacter[] npcs = world.getNpcs();

        if(direction == 0) {
            if(!tiles[x-1][y].isSolid() && !tiles[x-1][y].isWater()) {
                for(NonPlayableCharacter npc : npcs) {
                    if (npc.getXPos() == x - 1 && npc.getYPos() == y && npc.isSolid()) {
                        world.setNotification(npc.getDetails());
                        notificationTimer = 0;
                        return false;
                    }
                }
                return true;
            }
        } else if(direction == 1) {
            if(!tiles[x][y-1].isSolid() && !tiles[x][y-1].isWater()) {
                for(NonPlayableCharacter npc : npcs) {
                    if (npc.getXPos() == x && npc.getYPos() == y-1 && npc.isSolid()) {
                        world.setNotification(npc.getDetails());
                        notificationTimer = 0;
                        return false;
                    }
                }
                return true;
            }
        } else if(direction == 2) {
            if(!tiles[x+1][y].isSolid() && !tiles[x+1][y].isWater()) {
                for(NonPlayableCharacter npc : npcs) {
                    if (npc.getXPos() == x+1 && npc.getYPos() == y && npc.isSolid()) {
                        world.setNotification(npc.getDetails());
                        notificationTimer = 0;
                        return false;
                    }
                }
                return true;
            }
        } else if(direction == 3) {
            if(!tiles[x][y+1].isSolid() && !tiles[x][y+1].isWater()) {
                for(NonPlayableCharacter npc : npcs) {
                    if (npc.getXPos() == x && npc.getYPos() == y+1 && npc.isSolid()) {
                        world.setNotification(npc.getDetails());
                        notificationTimer = 0;
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Checks and returns the appropriate tile colour based on location and time.
    private Color colourCheck(int x, int y, boolean background, boolean night) {
        if(background && night) {
            return baseWorld.getTiles()[x][y].getTileColour(true);
        } else if(background && !night) {
            return baseWorld.getTiles()[x][y].getTileColour(false);
        } else {
            if (night) {
                if (tiles[x][y].containsNpc()) {
                    return world.getNpcByPos(x, y).getEntityColour();
                } else if (tiles[x][y].containsPlayer()) {
                    return player.getEntityColour();
                } else {
                    return tiles[x][y].getTileColour(true);
                }
            } else {
                if (tiles[x][y].containsNpc()) {
                    return world.getNpcByPos(x, y).getEntityColour();
                } else if (tiles[x][y].containsPlayer()) {
                    return player.getEntityColour();
                } else {
                    return tiles[x][y].getTileColour(false);
                }
            }
        }
    }

    private void changeMap(int direction, String name) {
        int playerXPos = player.getXPos();
        int playerYPos = player.getYPos();

        if(direction == -1) {
            String map = world.getConnectedMap(name);

            if(!map.equals("null")) {
                world.getMap().setStartPos(playerXPos, playerYPos);
                baseWorld.changeMap(map);
                world.changeMap(map);
                tiles = world.getTiles();

                player.setXPos(world.getStartXPos());
                player.setYPos(world.getStartYPos());
            }

        } else if(direction == 0) {
            String map = world.getConnectedMap(0);

            if(!map.equals("null")) {
                baseWorld.changeMap(map);
                world.changeMap(map);
                tiles = world.getTiles();

                player.setXPos(world.getWidth() - 1);
                player.setYPos(playerYPos);
            }

        } else if(direction == 1) {
            String map = world.getConnectedMap(1);

            if(!map.equals("null")) {
                baseWorld.changeMap(map);
                world.changeMap(map);
                tiles = world.getTiles();

                player.setXPos(playerXPos);
                player.setYPos(world.getHeight() - 1);
            }

        } else if(direction == 2) {
            String map = world.getConnectedMap(2);

            if(!map.equals("null")) {
                baseWorld.changeMap(map);
                world.changeMap(map);
                tiles = world.getTiles();

                player.setXPos(0);
                player.setYPos(playerYPos);
            }

        } else if(direction == 3) {
            String map = world.getConnectedMap(3);

            if(!map.equals("null")) {
                baseWorld.changeMap(map);
                world.changeMap(map);
                tiles = world.getTiles();

                player.setXPos(playerXPos);
                player.setYPos(0);
            }
        }

    }

    private void worldInit() {
        baseWorld = new World("menu0");
        world = new World("menu0");
        tiles = world.getTiles();

        player = new Player("Menu-chan",0);
        player.setXPos(world.getStartXPos());
        player.setYPos(world.getStartYPos());
    }

    private void frameInit(Application ex) {
        frame.getContentPane().add(ex);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setUndecorated(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
    }

    public static void main(String[] args) {
        new MapSerialize().serialize("src/resources/maps/");
        new Application().start();
    }

}
