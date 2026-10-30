package Core;

import Core.Renderer.ImageBuffer;

public class CHIP_8
{
    public static final int RAMSize = 4096, fontSetSize = 80, fontStartAddress = 0x50;
    private final int [] memory;
    private final CHIP8_Stack stack;
    private final ImageBuffer imageBuffer;
    private final int [] vRegisters;
    private int pc, delayTimer, soundTimer, opCode, iRegister;
    private final int [] fonts =
            {
                    0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
                    0x20, 0x60, 0x20, 0x20, 0x70, // 1
                    0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
                    0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
                    0x90, 0x90, 0xF0, 0x10, 0x10, // 4
                    0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
                    0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
                    0xF0, 0x10, 0x20, 0x40, 0x40, // 7
                    0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
                    0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
                    0xF0, 0x90, 0xF0, 0x90, 0x90, // A
                    0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
                    0xF0, 0x80, 0x80, 0x80, 0xF0, // C
                    0xE0, 0x90, 0x90, 0x90, 0xE0, // D
                    0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
                    0xF0, 0x80, 0xF0, 0x80, 0x80  // F
            };
    Keyboard keyboard;

    public CHIP_8 (Keyboard keyboard)
    {
        this.memory = new int[RAMSize];//4096 bytes of mem; 8 bites (1 byte) stored per array slot; 0x000 (0) - 0xFFF (4096); start of most programs is at 0x200 (512); 0x050 (80)-0x0A0 (160) stores text
        this.imageBuffer = new ImageBuffer();//64*32 Pixel display which is scaled by a factor of 10. Image array 1 (white) or 0 (black) uses XOR opp.
        this.stack = new CHIP8_Stack();
        this.vRegisters = new int[16];//16 8-bit registers; labeled Vx where x ranges from 0 - F; VF is a special register as it is used as a flag to hold information about the result
        delayTimer = soundTimer = iRegister = 0;
        pc = 0x200;//iRegister and PC are 16-bit because 0xFFF is too big to be stored in 8 bites
        this.keyboard = keyboard;

        System.arraycopy(fonts, 0, memory, fontStartAddress, fontSetSize);
    }

    public int[] getMemoryDump ()
    {
        return this.memory;
    }

    public int getMemoryAt (int address)
    {
        return this.memory[address];
    }

    public void setMemoryAt (int address, int data)
    {
        this.memory[address] = data;
    }

    public CHIP8_Stack getStack ()
    {
        return this.stack;
    }

    public ImageBuffer getImageBuffer ()
    {
        return this.imageBuffer;
    }

    public int[] getVRegisterDump ()
    {
        return this.vRegisters;
    }

    public int getVRegisterAt (int registerNumber)
    {
        return this.vRegisters[registerNumber];
    }

    public void setVRegister (int registerNumber, int value)
    {
        this.vRegisters[registerNumber] = value;
    }

    public int getPc()
    {
        return this.pc;
    }

    public void setPc(int pc)
    {
        this.pc = pc;
    }

    public void addToPcBy (int value)
    {
        this.pc += value;
    }

    public int getDelayTimer()
    {
        return this.delayTimer;
    }

    public void setDelayTimer(int delayTimer)
    {
        this.delayTimer = delayTimer;
    }

    public int getSoundTimer()
    {
        return this.soundTimer;
    }

    public void setSoundTimer(int soundTimer)
    {
        this.soundTimer = soundTimer;
    }

    public int getIRegister()
    {
        return this.iRegister;
    }

    public void setIRegister(int iRegister)
    {
        this.iRegister = iRegister;
    }

    public int getOpCode ()
    {
        return this.opCode;
    }
}
