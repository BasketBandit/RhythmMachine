package uk.co.codefreak.rhythmmachine;

import uk.co.codefreak.rhythmmachine.colour.Colours;
import uk.co.codefreak.rhythmmachine.input.KeyInput;
import uk.co.codefreak.rhythmmachine.object.Npc;
import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.world.MapSerialize;
import uk.co.codefreak.rhythmmachine.world.Tile;
import uk.co.codefreak.rhythmmachine.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Application extends Canvas implements Runnable {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private static final String title = "Rhythm Machine";
    private int width = (int) Math.round(screenSize.getWidth()*0.75);
    private int height = (width/16)*9;

    // Base world is used to update the map correctly.
    private World baseWorld;
    private World world;
    private int currentWorld = 0;

    private Tile[][] tiles;

    private Colours colours = new Colours();

    private Player player;

    private JFrame frame = new JFrame(title);
    private String framesPerSecondText = "0 FPS";
    private String ticksPerSecondText = "0 TPS";
    private int applicationRunTime = 0;

    private SineNode[] sineNodes = new SineNode[(int)Math.round(width/5.4)];
    private Graphics2D grr;

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

        world = new World(currentWorld);
        baseWorld = new World(currentWorld);
        tiles = world.getTiles();

        player = new Player("Josh",0);

        frameInit(this);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        int refreshRate = 0;

        for(int i = 0; i < gs.length; i++) {
            DisplayMode dm = gs[i].getDisplayMode();

            refreshRate = dm.getRefreshRate();
            if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
                System.out.println("Unknown refresh rate, capping at 60.");
                refreshRate = 60;
            }
        }

        double target = refreshRate + 0.0;
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
                canRender = true;
            } else {
                canRender = false;
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
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, width, height);

        //////////////////////////////////

        grr = (Graphics2D) g;

        grr.setColor(Color.WHITE);

        grr.drawString(applicationRunTime + " Second(s)", 10, 20);
        grr.drawString(framesPerSecondText, 10, 40);
        grr.drawString(ticksPerSecondText, 10, 60);
        grr.drawString(sineNodes[0].getAngle()+"",10,80);

        grr.drawString(width + " x " + height, 200, 20);

        for(int j = 0; j < sineNodes.length; j++) {
            grr.fill3DRect(j*5+30, 150, 5, (int) Math.round(33 * sineNodes[j].getAngle()), true);
        }

        // Draw character information.
        grr.drawString(player.getName(), 30, 205);

        // Draw the world and everything within it.
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {

                if(tiles[x][y].getInside().equals("n")) {
                    grr.setColor(Color.GREEN);
                } else if(tiles[x][y].getInside().equals("H")){
                    grr.setColor(Color.WHITE);
                } else if(tiles[x][y].getInside().equals("w") || tiles[x][y].getInside().equals("W")) {
                    grr.setColor(Color.BLUE);
                } else if(tiles[x][y].getInside().equals("E")) {
                    grr.setColor(colours.getColour(0));
                } else if(tiles[x][y].getInside().equals("B")) {
                    grr.setColor(Color.RED);
                } else if(tiles[x][y].getInside().equals("S")) {
                    grr.setColor(Color.WHITE);
                }
                grr.drawString(tiles[x][y].getInside() + "", 30 + (10 * x), 220 + (10 * y));

            }
        }

        //////////////////////////////////

        g.dispose();
        bs.show();
    }

    private void tick() {
        for (int i = 0; i < sineNodes.length; i++) {
        sineNodes[i].setAngle();
        }

        keyCheck();

        // Redraw the whole map.
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                tiles[x][y].setInside(baseWorld.getTile(x,y).getInside()) ;
            }
        }

        // Update dynamic objects and draw.
        world.update(player.getX(), player.getY());

        // Draw player position.
        tiles[player.getX()][player.getY()].setInside("H");

    }

    private void keyCheck() {
        if(KeyInput.isDown(0x25) && !keyPressed) {
            System.out.println("left");
            if(player.getX() > 0 && typeCheck(0)) {
                player.decX();
            } else if(player.getX() == 0) {
                changeMap(0);
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x25) && keyPressed) {

        } else if(KeyInput.isDown(0x26) && !keyPressed) {
            System.out.println("up");
            if(player.getY() > 0 && typeCheck(1)) {
                player.decY();
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x26) && keyPressed) {

        } else if(KeyInput.isDown(0x27) && !keyPressed) {
            System.out.println("right");
            if(player.getX() < world.getWidth()-1 && typeCheck(2)) {
                player.incX();
            } else if(player.getX() == world.getWidth()-1) {
                changeMap(1);
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x27) && keyPressed) {

        } else if(KeyInput.isDown(0x28) && !keyPressed) {
            System.out.println("down");
            if(player.getY() < world.getHeight()-1 && typeCheck(3)) {
                player.incY();
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

        Npc[] npcs = world.getNpcs();

        if(direction == 0) {

            if(tiles[player.getX()-1][player.getY()].getType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getX() == player.getX() - 1 && npcs[n].getY() == player.getY()) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }

        } else if(direction == 1) {

            if(tiles[player.getX()][player.getY()-1].getType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getX() == player.getX() && npcs[n].getY() == player.getY()-1) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }

        } else if(direction == 2) {

            if(tiles[player.getX()+1][player.getY()].getType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getX() == player.getX()+1 && npcs[n].getY() == player.getY()) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }

        } else if(direction == 3) {

            if(tiles[player.getX()][player.getY()+1].getType() != 1) {
                for(int n = 0; n < npcs.length; n++) {
                    if (npcs[n].getX() == player.getX() && npcs[n].getY() == player.getY()+1) {
                        System.out.println(npcs[n].getDetails());
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void changeMap(int direction) {

        if(direction == 1 && currentWorld+1 < world.mapsTotal()) {
            int playerY = player.getY();

            currentWorld++;
            baseWorld = new World(currentWorld);
            world = new World(currentWorld);
            tiles = world.getTiles();

            player.setX(0);
            player.setY(playerY);
        }

        if(direction == 0 && currentWorld > 0) {
            int playerY = player.getY();

            currentWorld--;
            baseWorld = new World(currentWorld);
            world = new World(currentWorld);
            tiles = world.getTiles();

            player.setX(world.getWidth()-1);
            player.setY(playerY);
        }

    }

    private void frameInit(Application ex) {

        for(int i = 0; i < sineNodes.length; i++) {
            sineNodes[i] = new SineNode();
            for(int x = 0; x < i; x++) {
                sineNodes[i].setAngle();
                sineNodes[i].setAngle();
                sineNodes[i].setAngle();
            }
        }

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
        ms.serializeAll("src/maps/","world_");

        Application ex = new Application();
        ex.start();
    }
}
