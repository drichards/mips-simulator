package mips.pipeline.stages;

import mips.MemoryStore;
import mips.RegisterName;
import mips.pipeline.PipelineRegister;
import mips.pipeline.PipelineStage;

public class Memory extends PipelineStage {

	private final PipelineRegister ex_mem;
	private final PipelineRegister mem_wb;
	private final MemoryStore memory;
	
	public Memory(
		PipelineRegister ex_mem,
		PipelineRegister mem_wb,
		MemoryStore memory
	) {
		this.ex_mem = ex_mem;
		this.mem_wb = mem_wb;
		this.memory = memory;
	}
	
	@Override
	public void run() {
		if (ex_mem.getValue(RegisterName.MEM_READ) == 1) {
			mem_wb.setValue(
				RegisterName.MEM_RESULT, 
				memory.getValue(ex_mem.getValue(RegisterName.ALU_RESULT))
			);
		}
		
		if (ex_mem.getValue(RegisterName.MEM_WRITE) == 1) {
			memory.storeValue(
				ex_mem.getValue(RegisterName.ALU_RESULT), 
				ex_mem.getValue(RegisterName.WRITE_DATA)
			);
		}
		
		ex_mem.forwardValues(mem_wb);
	}

}
