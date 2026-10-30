package Core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener
{
    private final boolean [] key;

    public Keyboard ()
    {
        this.key = new boolean[16];
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        //Nothing
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();

        keyMapper(keyCode, true);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int keyCode = e.getKeyCode();

        keyMapper(keyCode, false);
    }

    public boolean [] getKeys ()
    {
        return this.key;
    }

    public boolean isKeyPressed (int keycode)
    {
        return key[keycode];
    }

    private void keyMapper (int keycode, boolean value)
    {
        switch (keycode)
        {
            case KeyEvent.VK_1 -> key[0x1] = value;
            case KeyEvent.VK_2 -> key[0x2] = value;
            case KeyEvent.VK_3 -> key[0x3] = value;
            case KeyEvent.VK_4 -> key[0xC] = value;
            case KeyEvent.VK_Q -> key[0x4] = value;
            case KeyEvent.VK_W -> key[0x5] = value;
            case KeyEvent.VK_E -> key[0x6] = value;
            case KeyEvent.VK_R -> key[0xD] = value;
            case KeyEvent.VK_A -> key[0x7] = value;
            case KeyEvent.VK_S -> key[0x8] = value;
            case KeyEvent.VK_D -> key[0x9] = value;
            case KeyEvent.VK_F -> key[0xE] = value;
            case KeyEvent.VK_Z -> key[0xA] = value;
            case KeyEvent.VK_X -> key[0x0] = value;
            case KeyEvent.VK_C -> key[0xB] = value;
            case KeyEvent.VK_V -> key[0xF] = value;
        }
    }
}
