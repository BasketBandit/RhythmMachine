package uk.co.codefreak.rhythmmachine;

import uk.co.codefreak.rhythmmachine.colour.Colour;
import uk.co.codefreak.rhythmmachine.input.KeyInput;
import uk.co.codefreak.rhythmmachine.object.NonPlayableCharacter;
import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.world.MapSerialize;
import uk.co.codefreak.rhythmmachine.world.Tile;
import uk.co.codefreak.rhythmmachine.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Application extends Canvas implements Runnable {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final String version = "0.5.3";
    private static final String title = "Rhythm Machine";
    private int width = (int) Math.round(screenSize.getWidth()*0.75);
    private int height = 580;

    // Base world is used to update the map correctly.
    private World baseWorld;
    private World world;
    private int currentWorld = 0;

    private Tile[][] tiles;

    private Player player;

    private JFrame frame = new JFrame(title);
    private String framesPerSecondText = "0 FPS";
    private String ticksPerSecondText = "0 TPS";
    private int ticksPerSecondNumber = 0;
    private int applicationRunTime = 0;

    private boolean isRunning;
    private boolean keyPressed = false;

    private void start() {
        if(isRunning) return;
        isRunning = true;
        new Thread(this, "application-thread").start();
        // new Thread(this, "application-thread2").start(); // -> If you uncomment this, shit hits the fan.
    }

    private void stop() {
        if(!isRunning) return;
        isRunning = false;
    }

    @Override
    public void run() {
        System.out.println("Running...");

        worldInit();

        frameInit(this);

        int refreshRate = 60; // Capped framerate.
        double target = (refreshRate + 0.0);
        double nanoPerTick = 1000000000.0 / target;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double unprocessed = 0.0;
        int framesPerSecond = 0;
        int ticksPerSecond = 0;
        boolean canRender = false;

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
                //System.out.printf("FPS: %d | TPS: %d\n", framesPerSecond, ticksPerSecond);
                framesPerSecondText = framesPerSecond + " FPS";
                ticksPerSecondText = ticksPerSecond + " TPS";
                framesPerSecond = 0;
                ticksPerSecond = 0;
                ticksPerSecondNumber = 0;
                applicationRunTime++;
            }
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Colour.GREY_40);
        g.fillRect(0, 0, width, height);

        //////////////////////////////////

        Graphics2D grr = (Graphics2D) g;

        // Draw framerate, tickrate, version and window dimensions.
        grr.setColor(Colour.WHITE);
        grr.drawString(framesPerSecondText + " | " + ticksPerSecondText + " | " + version + " | " + width + " x " + height, 10, 20);

        // Draw character information.
        grr.drawString("Inventory", 520, 40);

        // Draw the world and everything within it.
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {

                grr.setColor(colourCheck(x,y));
                grr.drawString(tiles[x][y].getTileCharacter(), 10 + (10 * x), 40 + (10 * y));
            }
        }

        grr.setColor(Colour.WHITE);

        int invX = 0; int invY = 0;
        for(int inv = 0; inv < player.getInventory().length; inv++) {
            grr.drawString(player.getInventoryItem(inv).toString(),520 + (20 * invX),60 + (20 * invY));

            invX++;

            if(invX == 4) {
                invX = 0;
                invY++;
            }
        }

        //////////////////////////////////

        g.dispose();
        bs.show();
    }

    private void tick() {
        keyCheck();

        // Reset the whole map.
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                tiles[x][y].setTileCharacter(baseWorld.getTile(x,y).getTileCharacter()) ;
            }
        }

        world.update(player.getXPos(), player.getYPos(), ticksPerSecondNumber); // Update dynamic objects and draw.
        tiles[player.getXPos()][player.getYPos()].setTileCharacter("H"); // Set player position.
    }

    private void keyCheck() {

        // Arrow keys -> 0x25 = LEFT, 0x26 = UP, 0x27 = RIGHT, 0x28 = DOWN

        if(KeyInput.isDown(0x25) && !keyPressed) {
            if(player.getXPos() > 0 && typeCheck(0)) {
                player.decXPos();
            } else if(player.getXPos() == 0) {
                changeMap(0);
            }
            keyPressed = true;
        } else if(KeyInput.isDown(0x25) && keyPressed) {
        } else if(KeyInput.isDown(0x26) && !keyPressed) {
            if(player.getYPos() > 0 && typeCheck(1)) {
                player.decYPos();
            }
            keyPressed = true;
        } else if(KeyInput.isDown(0x26) && keyPressed) {
        } else if(KeyInput.isDown(0x27) && !keyPressed) {
            if(player.getXPos() < world.getWidth()-1 && typeCheck(2)) {
                player.incXPos();
            } else if(player.getXPos() == world.getWidth()-1) {
                changeMap(1);
            }
            keyPressed = true;
        } else if(KeyInput.isDown(0x27) && keyPressed) {
        } else if(KeyInput.isDown(0x28) && !keyPressed) {
            if(player.getYPos() < world.getHeight()-1 && typeCheck(3)) {
                player.incYPos();
            }
            keyPressed = true;
        } else if(KeyInput.isDown(0x28) && keyPressed) {
        } else {
            keyPressed = false;
        }
    }

    private boolean typeCheck(int direction) {
        // Directions: 0 -> Left, 1 -> Up, 2 -> Right, 3 -> Down.
        // NOTE: ONCE IDS ARE IMPLEMENTED CHECK BY ID AND NOT ARRAY POS

        NonPlayableCharacter[] npcs = world.getNpcs();

        if(direction == 0) {
            if(tiles[player.getXPos()-1][player.getYPos()].getTileType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getXPos() == player.getXPos() - 1 && npcs[n].getYPos() == player.getYPos() && npcs[n].getPhysType() == 1) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            }
        } else if(direction == 1) {
            if(tiles[player.getXPos()][player.getYPos()-1].getTileType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getXPos() == player.getXPos() && npcs[n].getYPos() == player.getYPos()-1 && npcs[n].getPhysType() == 1) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            }
        } else if(direction == 2) {
            if(tiles[player.getXPos()+1][player.getYPos()].getTileType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getXPos() == player.getXPos()+1 && npcs[n].getYPos() == player.getYPos() && npcs[n].getPhysType() == 1) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            }
        } else if(direction == 3) {
            if(tiles[player.getXPos()][player.getYPos()+1].getTileType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getXPos() == player.getXPos() && npcs[n].getYPos() == player.getYPos()+1 && npcs[n].getPhysType() == 1) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Color colourCheck(int x, int y) {
        if(tiles[x][y].containsNpc()) {
            return world.getNpcByPos(x,y).getEntityColour();
        } else if(tiles[x][y].containsPlayer()) {
            return player.getEntityColour();
        } else {
            return tiles[x][y].getTileColour();
        }
    }

    private void changeMap(int direction) {

        if(direction == 1 && currentWorld+1 < world.mapsTotal()) {
            int playerYPos = player.getYPos();

            currentWorld++;
            baseWorld = new World(currentWorld);
            world = new World(currentWorld);
            tiles = world.getTiles();

            player.setXPos(0);
            player.setYPos(playerYPos);
        }

        if(direction == 0 && currentWorld > 0) {
            int playerYPos = player.getYPos();

            currentWorld--;
            baseWorld = new World(currentWorld);
            world = new World(currentWorld);
            tiles = world.getTiles();

            player.setXPos(world.getWidth()-1);
            player.setYPos(playerYPos);
        }

    }

    private void worldInit() {
        baseWorld = new World(currentWorld);
        world = new World(currentWorld);
        tiles = world.getTiles();

        player = new Player("Josh",0);
        player.setXPos(world.getStartXPos());
        player.setYPos(world.getStartYPos());
    }

    private void frameInit(Application ex) {
        frame.add(ex);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();

    }

    public static void main(String[] args) {
        MapSerialize ms = new MapSerialize();
        ms.serializeAll("src/resources/maps/","world_");

        Application ex = new Application();
        ex.start();
    }

}
