package uk.co.codefreak.rhythmmachine;

import uk.co.codefreak.rhythmmachine.colour.Colours;
import uk.co.codefreak.rhythmmachine.input.KeyInput;
import uk.co.codefreak.rhythmmachine.object.Player;
import uk.co.codefreak.rhythmmachine.world.Tile;
import uk.co.codefreak.rhythmmachine.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Application extends Canvas implements Runnable {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static final String title = "Rhythm Machine";
    public int width = (int) Math.round(screenSize.getWidth()*0.75);
    public int height = (width/16)*9;

    private World world;
    private Tile[][] tiles;

    private Colours colours = new Colours();

    private Player player = new Player("Josh",0);

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
        new Thread(this, "Main-Thread").start();
    }

    private void stop() {
        if(!isRunning) return;
        isRunning = false;
    }

    @Override
    public void run() {
        System.out.println("Running...");

        this.world = new World(95,30);
        this.tiles = world.getTiles();

        frameInit(this);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        int refreshRate = 0;

        for (int i = 0; i < gs.length; i++) {
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

        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                if(tiles[x][y].getInside() == "n") {
                    grr.setColor(Color.GREEN);
                } else if(tiles[x][y].getInside() == "H"){
                    grr.setColor(Color.WHITE);
                } else if(tiles[x][y].getInside() == "w" || tiles[x][y].getInside() == "W") {
                    grr.setColor(Color.BLUE);
                } else if(tiles[x][y].getInside() == "E") {
                    grr.setColor(colours.getColour(0));
                } else if(tiles[x][y].getInside() == "B") {
                    grr.setColor(Color.RED);
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

        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                tiles[x][y].setInside("n");
            }
        }

        world.update();
        tiles[player.getX()][player.getY()].setInside("H");

    }

    private void keyCheck() {
        if(KeyInput.isDown(0x25) && keyPressed == false) {
            System.out.println("left");
            if(player.getX() > 0 && tiles[player.getX()-1][player.getY()].getType() != 1) {
                player.decX();
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x25) && keyPressed == true) {

        } else if(KeyInput.isDown(0x26) && keyPressed == false) {
            System.out.println("up");
            if(player.getY() > 0 && tiles[player.getX()][player.getY()-1].getType() != 1) {
                player.decY();
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x26) && keyPressed == true) {

        } else if(KeyInput.isDown(0x27) && keyPressed == false) {
            System.out.println("right");
            if(player.getX() < world.getWidth()-1 && tiles[player.getX()+1][player.getY()].getType() != 1) {
                player.incX();
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x27) && keyPressed == true) {

        } else if(KeyInput.isDown(0x28) && keyPressed == false) {
            System.out.println("down");
            if(player.getY() < world.getHeight()-1 && tiles[player.getX()][player.getY()+1].getType() != 1) {
                player.incY();
            }
            keyPressed = true;

        } else if(KeyInput.isDown(0x28) && keyPressed == true) {

        } else {
            keyPressed = false;
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
        Application ex = new Application();
        ex.start();
    }
}
