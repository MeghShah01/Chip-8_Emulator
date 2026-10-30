package Core;

import Core.Renderer.ImageBuffer;

import java.util.Random;

public class Interpreter
{
    private final CHIP_8 chip8;
    private Random rng;

    public Interpreter(CHIP_8 chip8)
    {
        this.chip8 = chip8;
        this.rng = new Random();
    }

    public void runFrames (int value)
    {
        for (int i = 0; i < value; i++)
        {
            cycle();
        }
    }

    private void cycle()
    {
        int opcode = (this.chip8.getMemoryAt(this.chip8.getPc()) << 8) | this.chip8.getMemoryAt(this.chip8.getPc() + 1);

        this.chip8.addToPcBy(2);

        switch (opcode & 0xF000)
        {
            case 0x0000 -> parse0x0OpCode(opcode);
            case 0x8000 -> parse0x8OpCode(opcode);
            case 0xE000 -> parse0xEOpCode(opcode);
            case 0xF000 -> parse0xFOpCode(opcode);
            default -> parseUniqueOpCode(opcode);
        }
    }

    public void updateTimers ()
    {
        if (this.chip8.getDelayTimer() > 0)
        {
            this.chip8.setDelayTimer(this.chip8.getDelayTimer() - 1);
        }

        if (this.chip8.getSoundTimer() > 0)
        {
            this.chip8.setSoundTimer(this.chip8.getSoundTimer() - 1);
        }
    }

    private void parse0xFOpCode (int opcode)
    {
        switch (opcode & 0x00FF)
        {
            case 0x000A -> OpCodeLDVxK(opcode);
            case 0x0007 -> OpCodeLDVxDT(opcode);
            case 0x0015 -> OpCodeLDDTVx(opcode);
            case 0x0018 -> OpCodeLDSTVx(opcode);
            case 0x001E -> OpCodeADDIVx(opcode);
            case 0x0029 -> OpCodeLDFVx(opcode);
            case 0x0033 -> OpCodeLDBVx(opcode);
            case 0x0055 -> OpCodeLDIVx(opcode);
            case 0x0065 -> OpCodeLDVxI(opcode);
        }
    }

    private void parse0xEOpCode (int opcode)
    {
        switch (opcode & 0x00FF)
        {
            case 0x00A1 -> OpCodeSKNP(opcode);
            case 0x009E -> OpCodeSKP(opcode);
        }
    }

    private void parse0x8OpCode (int opcode)
    {
        switch (opcode & 0x000F)
        {
            case 0x0000 -> OpCodeLDVxVy(opcode);
            case 0x0001 -> OPCodeORVxVy(opcode);
            case 0x0002 -> OPCodeANDVxVy(opcode);
            case 0x0003 -> OPCodeXORVxVy(opcode);
            case 0x0004 -> OPCodeADDVxVy(opcode);
            case 0x0005 -> OPCodeSUBVxVy(opcode);
            case 0x0006 -> OpCodeSHR(opcode);
            case 0x0007 -> OpCodeSNBN(opcode);
            case 0x000E -> OpCodeSHL(opcode);
        }
    }

    private void parse0x0OpCode (int opcode)
    {
        switch (opcode & 0x000F)
        {
            case 0x0000 -> OPCodeCLS();
            case 0x000E -> OPCodeRET();
        }
    }

    private void parseUniqueOpCode (int opcode)
    {
        switch (opcode & 0xF000)
        {
            case 0x1000 -> OPCodeJMP(opcode);
            case 0x2000 -> OPCodeCALL(opcode);
            case 0x3000 -> OPCodeSEVxByte(opcode);
            case 0x4000 -> OPCodeSNEVxByte(opcode);
            case 0x5000 -> OpCodeSEVxVy(opcode);
            case 0x6000 -> OpCodeLDVxByte(opcode);
            case 0x7000 -> OpCodeADDVxByte(opcode);
            case 0x9000 -> OpCodeSNEVxVy (opcode);
            case 0xA000 -> OpCodeLDIAddress(opcode);
            case 0xB000 -> OpCodeJPV0Address(opcode);
            case 0xC000 -> OpCodeRNDVxByte(opcode);
            case 0xD000 -> OpCodeDRW(opcode);
        }
    }

    private void OPCodeCLS() //00E0
    {
        this.chip8.getImageBuffer().clearImage();
    }

    private void OPCodeRET() //00EE
    {
        this.chip8.setPc(this.chip8.getStack().popStack());
    }

    private void OPCodeJMP(int opcode) //1nnn
    {
        int address = opcode & 0x0FFF;

        this.chip8.setPc(address);
    }

    private void OPCodeCALL(int opcode) //2nnn
    {
        this.chip8.getStack().pushStack(this.chip8.getPc());

        int address = opcode & 0x0FFF;

        this.chip8.setPc(address);
    }

    private void OPCodeSEVxByte(int opcode) //3xkk
    {
        int registerNumber = (opcode & 0x0F00) >>> 8, byteValue = opcode & 0x00FF;

        if (chip8.getVRegisterAt(registerNumber) == byteValue)
        {
            this.chip8.addToPcBy(2);
        }
    }

    private void OPCodeSNEVxByte(int opcode) //4xkk
    {
        int registerNumber = (opcode & 0x0F00) >>> 8, byteValue = opcode & 0x00FF;

        if (chip8.getVRegisterAt(registerNumber) != byteValue)
        {
            this.chip8.addToPcBy(2);
        }
    }

    private void OpCodeSEVxVy(int opcode) //5xy0
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        if (this.chip8.getVRegisterAt(vxNum) == this.chip8.getVRegisterAt(vyNum))
        {
            this.chip8.addToPcBy(2);
        }
    }

    private void OpCodeLDVxByte(int opcode) //6xkk
    {
        int vxNum = (opcode & 0x0F00) >>> 8, value = opcode & 0x00FF;

        this.chip8.setVRegister(vxNum, value);
    }

    private void OpCodeADDVxByte(int opcode) //7xkk
    {
        int vxNum = (opcode & 0x0F00) >>> 8, value = opcode & 0x00FF;

        this.chip8.setVRegister(vxNum, this.chip8.getVRegisterAt(vxNum) + value);
    }

    private void OpCodeLDVxVy(int opcode) //8xy0
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        this.chip8.setVRegister(vxNum, this.chip8.getVRegisterAt(vyNum));
    }


    private void OPCodeORVxVy(int opcode) //8xy1
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        int bitwiseORResult = vxValue | vyValue;

        this.chip8.setVRegister(vxNum, bitwiseORResult);
    }

    private void OPCodeANDVxVy(int opcode) //8xy2
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        int bitwiseANDResult = vxValue & vyValue;

        this.chip8.setVRegister(vxNum, bitwiseANDResult);
    }

    private void OPCodeXORVxVy(int opcode) //8xy3
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        int bitwiseXORResult = vxValue ^ vyValue;

        this.chip8.setVRegister(vxNum, bitwiseXORResult);
    }

    private void OPCodeADDVxVy (int opcode) //8xy4
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        int sum = vxValue + vyValue;

        if (sum > 255)
        {
            this.chip8.setVRegister(0xF, 1);
        }
        else
        {
            this.chip8.setVRegister(0xF, 0);
        }

        this.chip8.setVRegister(vxNum, sum & 0xFF);
    }

    private void OPCodeSUBVxVy (int opcode) //8xy5
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        int subResult = Byte.toUnsignedInt((byte)(vxValue - vyValue));

        if (vxValue >= vyValue)
        {
            this.chip8.setVRegister(0xF, 1);
        }
        else
        {
            this.chip8.setVRegister(0xF, 0);
        }

        this.chip8.setVRegister(vxNum, subResult);
    }

    private void OpCodeSHR (int opcode) //8xy6
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int vxValue = this.chip8.getVRegisterAt(vxNum);

        this.chip8.setVRegister(0xF, (vxValue & 0x1));

        int result = vxValue >>> 1;

        this.chip8.setVRegister(vxNum, result);
    }

    private void OpCodeSNBN (int opcode) //8xy7
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        int subResult = Byte.toUnsignedInt((byte)(vyValue - vxValue));

        if (vyValue > vxValue)
        {
            this.chip8.setVRegister(0xF, 1);
        }
        else
        {
            this.chip8.setVRegister(0xF, 0);
        }

        this.chip8.setVRegister(vxNum, subResult);
    }

    private void OpCodeSHL (int opcode) //8xyE
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int vxValue = this.chip8.getVRegisterAt(vxNum);

        this.chip8.setVRegister(0xF, ((vxValue & 0x80) >>> 7));

        int result = vxValue << 1;

        this.chip8.setVRegister(vxNum, result);

    }

    private void OpCodeSNEVxVy (int opcode) //9xy0
    {
        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxValue = this.chip8.getVRegisterAt(vxNum), vyValue = this.chip8.getVRegisterAt(vyNum);

        if (vxValue != vyValue)
        {
            this.chip8.addToPcBy(2);
        }
    }

    private void OpCodeLDIAddress (int opcode) //Annn
    {
       this.chip8.setIRegister((opcode & 0x0FFF));
    }

    private void OpCodeJPV0Address (int opcode) //Bnnn
    {
        this.chip8.setPc(this.chip8.getVRegisterAt(0) + (opcode & 0x0FFF));
    }

    private void OpCodeRNDVxByte (int opcode) //Cxkk
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int randomValue = rng.nextInt(256);

        int value = (opcode & 0x00FF);

        int result = randomValue & value;

        this.chip8.setVRegister(vxNum, result);
    }

    private void OpCodeDRW (int opcode) //Dxyn
    {
        int height = opcode & 0x000F;

        int vxNum = (opcode & 0x0F00) >>> 8, vyNum = (opcode & 0x00F0) >>> 4;

        int vxCoordinate = this.chip8.getVRegisterAt(vxNum) % ImageBuffer.bufferWidth, vyCoordinate = this.chip8.getVRegisterAt(vyNum) % ImageBuffer.bufferHeight;

        this.chip8.setVRegister(0xF, 0);

        for (int row = 0; row < height; row++)
        {
            int spriteByte = this.chip8.getMemoryAt(row + this.chip8.getIRegister());

            for (int col = 0; col < 8; col++)
            {
                int spritePixel =  spriteByte & (0x80 >>> col);

                int xPos = (vxCoordinate + col) % ImageBuffer.bufferWidth, yPos = (vyCoordinate + row) % ImageBuffer.bufferHeight;

                int screenPixel = chip8.getImageBuffer().getImageBufferArray()[yPos * ImageBuffer.bufferWidth + xPos];

                if (spritePixel != 0)
                {
                    if (screenPixel == 1)
                    {
                        this.chip8.setVRegister(0xF, 1);
                    }

                    this.chip8.getImageBuffer().setImageBufferArrayAt(xPos, yPos, (screenPixel ^ 0x1));
                }
            }
        }

        this.chip8.getImageBuffer().drawImage();
    }

    private void OpCodeSKP (int opcode) //Ex9E
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int keycode = this.chip8.getVRegisterAt(vxNum);

        if (this.chip8.keyboard.isKeyPressed(keycode))
        {
            this.chip8.addToPcBy(2);
        }
    }

    private void OpCodeSKNP (int opcode) //ExA1
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int keycode = this.chip8.getVRegisterAt(vxNum);

        if (!this.chip8.keyboard.isKeyPressed(keycode))
        {
            this.chip8.addToPcBy(2);
        }
    }

    private void OpCodeLDVxDT (int opcode) //Fx07
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        this.chip8.setVRegister(vxNum, this.chip8.getDelayTimer());
    }

    private void OpCodeLDVxK (int opcode) //Fx0A
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        if (chip8.keyboard.isKeyPressed(0x0))
        {
            this.chip8.setVRegister(vxNum, 0x0);
        }
        else if (chip8.keyboard.isKeyPressed(0x1))
        {
            this.chip8.setVRegister(vxNum, 0x1);
        }
        else if (chip8.keyboard.isKeyPressed(0x2))
        {
            this.chip8.setVRegister(vxNum, 0x2);
        }
        else if (chip8.keyboard.isKeyPressed(0x3))
        {
            this.chip8.setVRegister(vxNum, 0x3);
        }
        else if (chip8.keyboard.isKeyPressed(0x4))
        {
            this.chip8.setVRegister(vxNum, 0x4);
        }
        else if (chip8.keyboard.isKeyPressed(0x5))
        {
            this.chip8.setVRegister(vxNum, 0x5);
        }
        else if (chip8.keyboard.isKeyPressed(0x6))
        {
            this.chip8.setVRegister(vxNum, 0x6);
        }
        else if (chip8.keyboard.isKeyPressed(0x7))
        {
            this.chip8.setVRegister(vxNum, 0x7);
        }
        else if (chip8.keyboard.isKeyPressed(0x8))
        {
            this.chip8.setVRegister(vxNum, 0x8);
        }
        else if (chip8.keyboard.isKeyPressed(0x9))
        {
            this.chip8.setVRegister(vxNum, 0x9);
        }
        else if (chip8.keyboard.isKeyPressed(0xA))
        {
            this.chip8.setVRegister(vxNum, 0xA);
        }
        else if (chip8.keyboard.isKeyPressed(0xB))
        {
            this.chip8.setVRegister(vxNum, 0xB);
        }
        else if (chip8.keyboard.isKeyPressed(0xC))
        {
            this.chip8.setVRegister(vxNum, 0xC);
        }
        else if (chip8.keyboard.isKeyPressed(0xD))
        {
            this.chip8.setVRegister(vxNum, 0xD);
        }
        else if (chip8.keyboard.isKeyPressed(0xE))
        {
            this.chip8.setVRegister(vxNum, 0xE);
        }
        else if (chip8.keyboard.isKeyPressed(0xF))
        {
            this.chip8.setVRegister(vxNum, 0xF);
        }
        else
        {
            this.chip8.setPc(this.chip8.getPc() - 2);
        }
    }

    private void OpCodeLDDTVx (int opcode) //Fx15
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        this.chip8.setDelayTimer(this.chip8.getVRegisterAt(vxNum));
    }

    private void OpCodeLDSTVx (int opcode) //Fx18
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        this.chip8.setSoundTimer(this.chip8.getVRegisterAt(vxNum));
    }

    private void OpCodeADDIVx (int opcode) //Fx1E
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        this.chip8.setIRegister(this.chip8.getIRegister() + this.chip8.getVRegisterAt(vxNum));
    }

    private void OpCodeLDFVx (int opcode) //Fx29
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int vxValue = this.chip8.getVRegisterAt(vxNum);

        this.chip8.setIRegister(CHIP_8.fontStartAddress + (5*vxValue));
    }

    private void OpCodeLDBVx (int opcode) //Fx33
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int iAddress = chip8.getIRegister();

        int vxValue = this.chip8.getVRegisterAt(vxNum);

        this.chip8.setMemoryAt(iAddress + 2, vxValue%10);

        vxValue /= 10;

        this.chip8.setMemoryAt(iAddress + 1, vxValue%10);

        vxValue /= 10;

        this.chip8.setMemoryAt(iAddress, vxValue%10);
    }

    private void OpCodeLDIVx (int opcode) //Fx55
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int iAddress = this.chip8.getIRegister();

        for (int i = 0; i <= vxNum; i++)
        {
            this.chip8.setMemoryAt(iAddress + i, this.chip8.getVRegisterAt(i));
        }
    }

    private void OpCodeLDVxI (int opcode) //Fx65
    {
        int vxNum = (opcode & 0x0F00) >>> 8;

        int iAddress = this.chip8.getIRegister();

        for (int i = 0; i <= vxNum; i++)
        {
           this.chip8.setVRegister(i, this.chip8.getMemoryAt(iAddress + i));
        }
    }
}
