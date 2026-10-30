package Core;

import Core.Renderer.RenderPanel;
import Core.Renderer.Window;

import java.io.File;

public class Application implements Runnable
{
    private final RenderPanel renderPanel;
    private Thread mainThread;
    private CHIP_8 chip8;
    private Interpreter interpreter;
    private Window window;
    private Keyboard keyboard;
    int frames = 0;
    double accumulator = 0.0;


    public Application (File file)
    {
        this.keyboard = new Keyboard();
        this.chip8 = new CHIP_8(keyboard);
        new ProgramLoader(file, chip8).load();
        this.interpreter = new Interpreter(chip8);
        this.renderPanel = new RenderPanel(chip8.getImageBuffer());
        this.window = new Window(renderPanel);
        window.addKeyListener(keyboard);

        startThread();
    }

    private void startThread ()
    {
        mainThread = new Thread(this);
        mainThread.start();
    }


    @Override
    public void run()
    {
        double deltaTime, fpsCAP = 1_000_000_000.0/400.0;
        long currentTime, previousTime = System.nanoTime();
        while (true)
        {
            currentTime = System.nanoTime();

            deltaTime = currentTime - previousTime;

            previousTime = currentTime;

            interpreter.runFrames(1);

            interpreter.updateTimers();

            renderPanel.repaint();

            double sleepMillis = (fpsCAP - (System.nanoTime() - currentTime))/1_000_000.0;

            clockSpeedLimit(sleepMillis);

            //printFPS(deltaTime);
        }
    }

    private void printFPS (double deltaTime)
    {
        frames ++;

        accumulator += deltaTime/1_000_000_000.0;

        if (accumulator >= 1.0)
        {
            int FPS = (int)(frames/accumulator);

            System.out.println("FPS: " + FPS);

            accumulator = 0.0;
            frames = 0;
        }
    }

    private void clockSpeedLimit (double sleepMillis)
    {
        if (sleepMillis > 0.0)
        {
            try
            {
                Thread.sleep((long) sleepMillis);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
