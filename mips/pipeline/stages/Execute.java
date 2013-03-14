package mips.pipeline.stages;

import mips.RegisterName;
import mips.pipeline.PipelineRegister;
import mips.pipeline.PipelineStage;

public class Execute extends PipelineStage {
	private final PipelineRegister ex_mem;
	private final PipelineRegister id_ex;
	private final PipelineRegister mem_wb;
	
	public Execute(
		PipelineRegister id_ex,
		PipelineRegister ex_mem, 
		PipelineRegister mem_wb
	) {
		this.ex_mem = ex_mem;
		this.id_ex = id_ex;
		this.mem_wb = mem_wb;
	}

	@Override
	public void run() {
		long aluArg1, aluArg2, writeData;
		
		long dest = ex_mem.getValue(RegisterName.REG_DST) == 1 ?
						ex_mem.getValue(RegisterName.R_D) :
						ex_mem.getValue(RegisterName.R_T);
		long dest2 = mem_wb.getValue(RegisterName.REG_DST) == 1 ?
						mem_wb.getValue(RegisterName.R_D) :
						mem_wb.getValue(RegisterName.R_T);			
		
		if (ex_mem.getValue(RegisterName.REG_WRITE) == 1 &&
			dest != 0 && 
			dest == id_ex.getValue(RegisterName.R_S))
		{
			aluArg1 = ex_mem.getValue(RegisterName.ALU_RESULT);
		} else if (mem_wb.getValue(RegisterName.REG_WRITE) == 1 &&
			dest2 != 0 &&
			dest2 == id_ex.getValue(RegisterName.R_S)) {
			if (mem_wb.getValue(RegisterName.MEM_TO_REG) == 1) {
				aluArg1 = mem_wb.getValue(RegisterName.MEM_RESULT);
			} else {
				aluArg1 = mem_wb.getValue(RegisterName.ALU_RESULT);
			}
		} else {
			aluArg1 = id_ex.getValue(RegisterName.READ_DATA_1);
		}
		
	
		if (ex_mem.getValue(RegisterName.REG_WRITE) == 1 &&
			dest != 0 && 
			dest == id_ex.getValue(RegisterName.R_T))
		{
			writeData = ex_mem.getValue(RegisterName.ALU_RESULT);
		} else if (mem_wb.getValue(RegisterName.REG_WRITE) == 1 &&
			dest2 != 0 &&
			dest2 == id_ex.getValue(RegisterName.R_T)) {
			if (mem_wb.getValue(RegisterName.MEM_TO_REG) == 1) {
				writeData = mem_wb.getValue(RegisterName.MEM_RESULT);
			} else {
				writeData = mem_wb.getValue(RegisterName.ALU_RESULT);
			}
		} else {
			writeData = id_ex.getValue(RegisterName.READ_DATA_2);
		}
			
		if (id_ex.getValue(RegisterName.ALU_SRC) == 0) {
			aluArg2 = writeData;
		} else {
			aluArg2 = id_ex.getValue(RegisterName.IMMEDIATE);
		}
		
		id_ex.forwardValues(ex_mem);
		
		long result = 0;
		
		switch((int)id_ex.getValue(RegisterName.ALU_OP)) {
			case (0):
				result = aluArg1 + aluArg2;
				break;
			case (1):
				result = aluArg1 - aluArg2;
				break;
			case (2):
				result = aluArg1 & aluArg2;
				break;
			case (3):
				result = aluArg1 | aluArg2;
				break;
			case (4):
				result = ~(aluArg1 | aluArg2) & 0xFFFFFFFFl;
				break;
			case (5):
				result = aluArg1 < aluArg2 ? 1 : 0;
				break;
		}
		
		ex_mem.setValue(RegisterName.ALU_RESULT, result);
		ex_mem.setValue(RegisterName.WRITE_DATA, writeData);
	}
}
