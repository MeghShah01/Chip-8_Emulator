package Core;

public class CHIP8_Stack
{
    public static final int StackSize = 16;

    private final int [] stack;
    private int stackPointer;

    public CHIP8_Stack ()
    {
        this.stack = new int[StackSize];//16-bit values; 0-16; used to preform subroutines; normally store state of PC (program counter)
        this.stackPointer = 0;
    }

    public void pushStack (int data)
    {
        this.stack[stackPointer++] = data;
    }

    public int popStack ()
    {
        return this.stack[--stackPointer];
    }

    public int getStackPointer ()
    {
        return this.stackPointer;
    }

    public void setStackPointer (int value)
    {
        this.stackPointer = value;
    }

    public int [] getStackDump ()
    {
        return this.stack;
    }

    public void printStack ()
    {
        int i = 0;
        for (int data: stack)
        {
            System.out.println("Address[" + i + "]: " + data);
            i++;
        }
    }

}
