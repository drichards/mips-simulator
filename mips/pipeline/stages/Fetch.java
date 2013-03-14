package mips.pipeline.stages;

import mips.MemoryStore;
import mips.ProgramCounter;
import mips.RegisterName;
import mips.pipeline.PipelineRegister;
import mips.pipeline.PipelineStage;

public class Fetch extends PipelineStage {
	private static final int HALT_INSTRUCTION = 0xFC000000; 
	
	private final PipelineRegister if_id;
	private final MemoryStore memory;
	private final ProgramCounter pc;
	
	public Fetch(
		PipelineRegister if_id, 
		MemoryStore memory, 
		ProgramCounter pc
	) {
		this.if_id = if_id;
		this.memory = memory;
		this.pc = pc;
	}
	
	@Override
	public void run() {
		long instruction = memory.getValue(pc.getValue());
		//System.out.println(Integer.toHexString(instruction));
		if_id.setValue(RegisterName.INSTRUCTION, instruction);
		if_id.setValue(RegisterName.OP_CODE, 0);
		if_id.setValue(RegisterName.PC, pc.getValue());
		
		if (instruction != HALT_INSTRUCTION) {
			pc.increment();
		}
	}

}
