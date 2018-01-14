package uk.co.codefreak.rhythmmachine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Application extends Canvas implements Runnable {

    public static final String title = "Rhythm Machine";
    public static final int width = 1920;
    public static final int height = (width/16)*9;

    private JFrame frame = new JFrame(title);
    private String framesPerSecondText = "0 FPS";
    private String ticksPerSecondText = "0 TPS";
    private int applicationRunTime = 0;

    private SineNode[] sineNodes = new SineNode[(int)Math.round(width/5.2)];
    private Graphics2D grr;

    private boolean isRunning;

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

        for(int j = 0; j < sineNodes.length; j++) {
            grr.fill3DRect(j*5+30, 250, 5, (int) Math.round(33 * sineNodes[j].getAngle()), true);
        }

        //////////////////////////////////

        g.dispose();
        bs.show();
    }

    private void tick() {
        for (int i = 0; i < sineNodes.length; i++) {
        sineNodes[i].setAngle();
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
