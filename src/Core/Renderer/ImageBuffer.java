package Core.Renderer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageBuffer
{
    public static final int bufferWidth = 64, bufferHeight = 32, displaySize = bufferWidth*bufferHeight, scaleFactor = 15;

    private int[] imageBufferArray;

    private BufferedImage image;

    public ImageBuffer ()
    {
        this.imageBufferArray = new int[displaySize];
        this.image = new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_BYTE_GRAY);
    }

    public int[] getImageBufferArray ()
    {
        return this.imageBufferArray;
    }

    public void setImageBufferArrayAt (int x, int y, int value)
    {
        this.imageBufferArray[y*bufferWidth + x] = value;
    }

    public BufferedImage getImage ()
    {
        return this.image;
    }

    public void drawImage ()
    {
       /*
        for (int y = 0; y < bufferHeight; y++)
        {
            for (int x = 0; x < bufferWidth; x++)
            {
                for (int kernalY = y*scaleFactor; kernalY < (y+1)*scaleFactor; kernalY++)
                {
                    for (int kernalX = x*scaleFactor; kernalX < (x+1)*scaleFactor; kernalX++)
                    {
                        if ((imageBufferArray[y * bufferWidth + x] == 0))
                        {
                            image.setRGB(kernalX, kernalY, 0x000000);
                        } else
                        {
                            image.setRGB(kernalX, kernalY, 0xFFFFFF);
                        }
                    }
                }
            }
        }
       */

        byte [] pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < displaySize; i ++)
        {
            if (imageBufferArray[i] == 0)
            {
                pixels[i] = 0;
            }
            else
            {
                pixels[i] = (byte)255;
            }
        }
    }

    public void clearImage ()
    {
       for (int i = 0; i < displaySize; i++)
       {
           imageBufferArray[i] = 0;
       }
    }
}
