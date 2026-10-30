package Application;

import Core.Application;

import java.io.File;

class Main
{
    public static void main (String [] args)
    {
        File file = new File("C:\\Users\\Manish\\Documents\\JavaProjects\\CHIP-8_Emulator\\Roms\\Pong (1 player).ch8");

        new Application(file);
    }
}
