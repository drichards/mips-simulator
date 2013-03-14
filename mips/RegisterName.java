package mips;

public enum RegisterName {
	R_0,
	R_1,
	R_2,
	R_3,
	R_4,
	R_5,
	R_6,
	R_7,
	R_8,
	R_9,
	R_10,
	R_11,
	R_12,
	R_13,
	R_14,
	R_15,
	R_16,
	R_17,
	R_18,
	R_19,
	R_20,
	R_21,
	R_22,
	R_23,
	R_24,
	R_25,
	R_26,
	R_27,
	R_28,
	R_29,
	R_30,
	R_31,
	INSTRUCTION(false),
	R_S(false),
	R_T(false),
	R_D(false),
	IMMEDIATE(false),
	SHAMT(false),
	ADDRESS(false), 
	REG_DST(false),
	ALU_SRC(false),
	MEM_TO_REG(false),
	REG_WRITE(false),
	MEM_READ(false),
	MEM_WRITE(false),
	BRANCH(false),
	BRANCH_NE(false),
	ALU_OP(false),
	JUMP(false),
	JUMP_SRC(false),
	READ_DATA_1(false),
	READ_DATA_2(false),
	ALU_RESULT(false),
	MEM_RESULT(false),
	WRITE_DATA(false),
	HALT(false),
	OP_CODE(false),
	PC(false);
	
	private final boolean isPrimitive;
	
	private RegisterName() {
		this(true);
	}
	
	private RegisterName(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}
	
	public boolean isPrimitive() {
		return isPrimitive;
	}
	
	public static RegisterName valueOf(long ordinal) {
		for (RegisterName name : RegisterName.values()) {
			if (name.ordinal() == ordinal) {
				return name;
			}
		}
		
		return null;
	}
}
