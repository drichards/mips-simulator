package mips;

import java.util.HashMap;

/**
 * A grouping of registers composed entirely of registers $r0-$r31
 */
public class RegisterFile {
	/**
	 * The internal storage for the registers.
	 */
	protected HashMap<RegisterName, Register> registers = 
		new HashMap<RegisterName, Register>();
	/**
	 * Used to keep track of whether or not we should write to the register
	 * groups when tick is called.
	 */
	private boolean disableWrite = false;
	
	/**
	 * Set the value of the register of the given name.  Note that this won't
	 * take effect until <code>tick</code> is called.
	 * 
	 * @param registerName The name of the register to set.
	 * @param value The value of the register to set.
	 */
	public void setValue(RegisterName registerName, long value) {
		Register register = getRegister(registerName);
		register.setValue(value);
	}
	
	public long getValue(RegisterName registerName) {
		return getRegister(registerName).getValue();
	}
	
	public void disableWrite() {
		disableWrite = true;
	}
	
	public void tick() {
		if (!disableWrite) {
			for (Register register : registers.values()) {
				register.tick();
			}
		} else {
			disableWrite = false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 32; i++) {
			builder.append("$r");
			builder.append(i);
			builder.append('\t');
			builder.append(Long.toHexString(getRegister(RegisterName.valueOf(i)).getValue()));
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	protected boolean validRegister(RegisterName registerName) {
		return registerName.isPrimitive();
	}
	
	private Register getRegister(RegisterName registerName) {
		if (!validRegister(registerName)) {
			throw new IllegalArgumentException(
				"Invalid register: " + registerName.name()
			);
		}
		Register register = registers.get(registerName);
		
		if (register == null) {
			register = new Register();
			registers.put(registerName, register);
		}
		
		return register;
	}
}
