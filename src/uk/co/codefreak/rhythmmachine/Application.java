package uk.co.codefreak.rhythmmachine;

import uk.co.codefreak.rhythmmachine.colour.Colour;
import uk.co.codefreak.rhythmmachine.data.Flags;
import uk.co.codefreak.rhythmmachine.data.SaveHandler;
import uk.co.codefreak.rhythmmachine.input.KeyInput;
import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;
import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.world.MapSerialize;
import uk.co.codefreak.rhythmmachine.world.Tile;
import uk.co.codefreak.rhythmmachine.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Application extends Canvas {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final String version = "0.8.6";
    private static final String title = "Rhythm Machine";
    private int width = 800;
    private int height = 625;

    // Base world is used to update the map correctly.
    private World baseWorld;
    private World world;
    private Tile[][] tiles;
    private Player player;
    private Flags flags = new Flags();

    private long moveTimer = System.currentTimeMillis();
    private boolean renderInventory = false;
    private boolean m;

    private JFrame frame = new JFrame(title);
    private String framesPerSecondText = "0 FPS";
    private String ticksPerSecondText = "0 TPS";
    private int ticksPerSecondNumber = 0;
    private int applicationRunTime = 0;

    private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private Font[] fonts = ge.getAllFonts();

    private boolean isRunning;

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
                // new Render(player, world);
                framesPerSecond++;
            }

            if(System.currentTimeMillis() - 1000 > timer) {
                timer += 1000;
                //System.out.printf("FPS: %d | TPS: %d\n", framesPerSecond, ticksPerSecond);
                framesPerSecondText = framesPerSecond + " FPS";
                ticksPerSecondText = ticksPerSecond + " TPS";
                framesPerSecond = 0;
                ticksPerSecond = 0;
                ticksPerSecondNumber = 0;
                applicationRunTime++;
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
        for(int x = 0; x < 82; x++) {
            for(int y = 0; y < 64; y++) {
                grr.drawString("w", 1 + (9 * x), 13 + (9 * y));
            }
        }

        // Draw framerate, tickrate, version and window dimensions.
        grr.setColor(Colour.WHITE);
        grr.drawString(framesPerSecondText + " | " + ticksPerSecondText + " | " + version + " | " + width + " x " + height + " | " + world.mapsTotal() + " total maps" + " | " + world.getTotalConnectedMaps() + " connected maps", 10, 20);

        // Draw notifications
        grr.setColor(Colour.WHITE);
        grr.drawString(world.getNotification(), 560, 60);

        // Draw clock
        grr.drawString(world.getTimeString(), 560, 40);

        // Draw character information.
        grr.drawString("Push 'g' to toggle inventory.", 560, 80);

        // Draw the world and everything within it.
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                if(world.isNight() && player.isDistanceFromTile(x,y,5)) {
                    grr.setColor(colourCheck(x, y, true));
                } else {
                    grr.setColor(colourCheck(x, y, false));
                }
                grr.drawString(tiles[x][y].getTileCharacter(), 10 + (9 * x), 40 + (9 * y));
            }
        }


        if(renderInventory) {
            grr.setColor(Colour.WHITE);
            int invX = 0;
            int invY = 0;
            for (int inv = 0; inv < player.getInventory().length; inv++) {
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

        world.update(player.getXPos(), player.getYPos(), ticksPerSecondNumber); // Update dynamic objects and draw.
        tiles[player.getXPos()][player.getYPos()].setTileCharacter("λ"); // Set player position.
    }

    private void keyCheck(int x, int y) { // NOTE: Try to rework this feature next, it sucks.

        // Arrow keys -> 0x25 = LEFT, 0x26 = UP, 0x27 = RIGHT, 0x28 = DOWN

        // G
        if(KeyInput.isDown(0x47)) {
            System.out.println("Toggled inventory.");
            renderInventory = !renderInventory;
        }

        // E
        if(KeyInput.isDown(0x45)) {
            flags.setFlags(player,baseWorld,world);
            new SaveHandler(player.getName(),flags);
            world.setNotification("Game saved!");
        }

        // F
        if(KeyInput.isDown(0x46)) {
            // Load the saved flags from the file, inject them into the application.
            flags = new SaveHandler().loadGame(player.getName());
            player = new Player(flags.PLAYER);
            baseWorld = new World(flags.BASE_WORLD);
            world = new World(flags.WORLD);
            tiles = world.getTiles();
            world.setNotification("Game loaded!");
        }

        if(System.currentTimeMillis() - 150 > moveTimer) {
            moveTimer += 150;

            double keyTimer = System.currentTimeMillis();

            // Left Arrow
            if(KeyInput.isDown(0x25)) {
                if (x > 0 && typeCheck(0, x, y)) {
                    player.decXPos();
                } else if (x == 0) {
                    changeMap(0);
                }
            }

            // Up Arrow
            if (KeyInput.isDown(0x26)) {
                if (y > 0 && typeCheck(1, x, y)) {
                    player.decYPos();
                } else if (y == 0) {
                    changeMap(1);
                }
                m = true;
            }

            // Right Arrow
            if (KeyInput.isDown(0x27)) {
                if (x < world.getWidth() - 1 && typeCheck(2, x, y)) {
                    player.incXPos();
                } else if (x == world.getWidth() - 1) {
                    changeMap(2);
                }
                m = true;
            }

            // Down Arrow
            if (KeyInput.isDown(0x28)) {
                if (y < world.getHeight() - 1 && typeCheck(3, x, y)) {
                    player.incYPos();
                } else if (y == world.getHeight() - 1) {
                    changeMap(3);
                }
                m = true;
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
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Checks and returns the appropriate tile colour based on location and time.
    private Color colourCheck(int x, int y, boolean night) {
        if(night) {
            if (tiles[x][y].containsNpc()) {
                return world.getNpcByPos(x,y).getEntityColour();
            } else if (tiles[x][y].containsPlayer()) {
                return player.getEntityColour();
            } else {
                return tiles[x][y].getTileColour(true);
            }
        } else {
            if (tiles[x][y].containsNpc()) {
                return world.getNpcByPos(x,y).getEntityColour();
            } else if (tiles[x][y].containsPlayer()) {
                return player.getEntityColour();
            } else {
                return tiles[x][y].getTileColour(false);
            }
        }
    }

    private void changeMap(int direction) {
        int playerXPos = player.getXPos();
        int playerYPos = player.getYPos();

        if(direction == 0) {
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
        baseWorld = new World("test");
        world = new World("test");
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
