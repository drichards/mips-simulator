package mips;

/**
 * The implementation of a register.  A register contains a 
 * single value that can be set or retrieved.  When a value is set, the old
 * value is not overwritten with the new value until <code>tick</code> is
 * called.
 */
public class Register {
	/**
	 * The current value of the register
	 */
	private long value = 0;
	
	/**
	 * Used to keep track of whether or not we should allow the next value
	 * to be written when tick is called.
	 */
	private boolean disableWrite = false;
	
	/**
	 * The new value of the register.  If set to -1, the current value will
	 * be retained when tick is called.
	 */
	protected long newValue = -1;
	
	/**
	 * Create a new register and initialize the value to 0.
	 */
	public Register() {
		this(0);
	}
	
	/**
	 * Create a new register with the given value.
	 * 
	 * @param value The initial value of the register.
	 */
	public Register(long value) {
		this.value = value;
	}
	
	/**
	 * @return The current value of the register.
	 */
	public long getValue() {
		return value;
	}
	
	/**
	 * Set the value of the register. This won't go into place until
	 * after <code>tick</code> is called.
	 * 
	 * @param newValue The new value of the register.
	 */
	public void setValue(long newValue) {
		this.newValue = newValue;
	}
	
	/**
	 * Disable writing of the register when <code>tick</code> is called.
	 */
	public void disableWrite() {
		this.disableWrite = true;
	}
	
	/**
	 * Enable writing of the register when <code>tick</code> is called.
	 */
	public void enableWrite() {
		this.disableWrite = false;
	}
	
	/**
	 * Write the new value of the register into the current value of the
	 * register if writing is enabled and there's a new value to write.
	 */
	public void tick() {
		if (!disableWrite) {
			if (newValue != -1) {
				value = newValue;
				newValue = -1;
			}
		} else {
			disableWrite = false;
		}
	}
}
