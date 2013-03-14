package mips;

/**
 * The program counter that keeps track of what instruction in memory should
 * be executed next.
 */
public class ProgramCounter extends Register {
	private static final int FIRST_INSTRUCTION = 0x1000;
	
	/**
	 * Create a program counter and initialize it to the first instruction
	 * in the memory store.
	 */
	public ProgramCounter() {
		super(FIRST_INSTRUCTION);
	}
	
	/**
	 * Increment the program counter by one word.
	 */
	public void increment() {
		newValue = getValue() + 4;
	}
}
