package Core.Renderer;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel
{
    private ImageBuffer imageBuffer;
    public RenderPanel (ImageBuffer imageBuffer)
    {
        this.imageBuffer = imageBuffer;
        this.setPreferredSize(new Dimension(ImageBuffer.bufferWidth*ImageBuffer.scaleFactor,ImageBuffer.bufferHeight*ImageBuffer.scaleFactor));
        this.setBackground(Color.ORANGE);
        this.setVisible(true);
    }

    @Override
    public void paintComponent (Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2D.drawImage(imageBuffer.getImage(), 0, 0, ImageBuffer.bufferWidth*ImageBuffer.scaleFactor, ImageBuffer.bufferHeight*ImageBuffer.scaleFactor, null);
    }

}
