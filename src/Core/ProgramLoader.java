package Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgramLoader
{
    private File file;
    private CHIP_8 chip_8;

    public ProgramLoader(File file, CHIP_8 chip_8)
    {
        this.file = file;
        this.chip_8 = chip_8;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public void load ()
    {
        try
        {
            byte [] buffer = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

            for (int i = 0; i < buffer.length; i++)
            {
                chip_8.setMemoryAt(0x200+i, Byte.toUnsignedInt(buffer[i]));
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
