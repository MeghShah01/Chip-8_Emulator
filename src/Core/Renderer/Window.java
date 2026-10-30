package Core.Renderer;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    public Window (JPanel renderPanel)
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(renderPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setBackground(Color.GREEN);
        this.setTitle("Chip-8 Emulator");
        this.setVisible(true);
    }

}
