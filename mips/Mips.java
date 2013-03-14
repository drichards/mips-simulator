package mips;

import java.io.IOException;

import mips.pipeline.PipelineRegister;
import mips.pipeline.stages.Decode;
import mips.pipeline.stages.Execute;
import mips.pipeline.stages.Fetch;
import mips.pipeline.stages.Memory;
import mips.pipeline.stages.Writeback;

/**
 * The main class for the MIPS simulator.  This deals with constructing
 * and running all the various pipeline stages, as well as counting total
 * cycles and instructions.
 */
public class Mips {
	/**
	 * The pipeline register between the fetch and decode stages
	 */
	private final PipelineRegister if_id = new PipelineRegister();
	
	/**
	 * The pipeline register between the decode and execute stages
	 */
	private final PipelineRegister id_ex = new PipelineRegister();
	
	/**
	 * The pipeline register between the execute and memory stages
	 */
	private final PipelineRegister ex_mem = new PipelineRegister();
	
	/**
	 * The pipeline register between the memory and writeback stages
	 */
	private final PipelineRegister mem_wb = new PipelineRegister();
	
	/**
	 * The program counter
	 */
	private final ProgramCounter programCounter = new ProgramCounter();
	
	/**
	 * The register file for the CPU
	 */
	private final RegisterFile registerFile = new RegisterFile();
	
	/**
	 * The fetch pipeline stage
	 */
	private final Fetch fetch;
	
	/**
	 * The decode pipeline stage
	 */
	private final Decode decode;
	
	/**
	 * The execute pipeline stage
	 */
	private final Execute execute;
	
	/**
	 * The memory pipeline stage
	 */
	private final Memory memory;
	
	/**
	 * The writeback pipeline stage
	 */
	private final Writeback writeback;
	
	/**
	 * The main launching point for the program.  This should take as input
	 * a single string that is the path to the input binary file.
	 * 
	 * @param args The path to the input file.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("You must supply an input filename");
		} else {
			try {
				// create the simulator
				Mips mips = new Mips(args[0]);
				
				// run the simulator
				mips.run();
			} catch (IOException e) {
				System.out.println("Error opening file named \"" + args[0] + "\"");
			}
		}
	}
	
	/**
	 * Create a new MIPS simulator with the given filename as the location
	 * of the file that provides the input to the simulator.
	 * 
	 * @param filename The location of the input file.
	 * 
	 * @throws IOException Thrown if there's a problem reading the input file.
	 */
	public Mips(String filename) throws IOException {
		// create the memory store
		MemoryStore memoryStore = new MemoryStore(filename);
		
		// create the various pipeline stages
		fetch = new Fetch(if_id, memoryStore, programCounter);
		decode = new Decode(if_id, id_ex, registerFile, programCounter);
		execute = new Execute(id_ex, ex_mem, mem_wb);
		memory = new Memory(ex_mem, mem_wb, memoryStore);
		writeback = new Writeback(mem_wb, registerFile);	
	}
	
	/**
	 * Runs the MIPS simulator and prints the results out after completion.
	 */
	public void run() {
		// keep track of the instructions run and the cycles run
		int instructionCount = 0;
		int cycleCount = 0;
		
		// while we haven't halted
		while(!writeback.done()) {
			// run all the pipeline stages
			fetch.run();
			// note: writeback has to run before decode to 
			// avoid data hazards
			writeback.run();
			decode.run();
			execute.run();
			memory.run();
			
			// if we completed a non-stall instruction, increment
			// the instruction counter
			if (!writeback.isNop()) {
				instructionCount++;
			}
			
			// increment the cycle counter
			cycleCount++;
			
			// tick over all our register values
			tick();
		}
		
		// compute the cycles per instruction
		float cpi = (float)cycleCount / instructionCount;
		
		// output the results
		System.out.println("Instruction count: \t" + instructionCount);
		System.out.println("Cycle count: \t\t" + cycleCount);
		System.out.println("CPI: \t\t\t" + cpi);
		System.out.println(registerFile);
	}
	
	/**
	 * Tick all the various registers of the program.  This is called
	 * once per cycle.
	 */
	private void tick() {
		programCounter.tick();
		if_id.tick();
		id_ex.tick();
		ex_mem.tick();
		mem_wb.tick();
	}
}
