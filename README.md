mips-simulator
==============

Data Structures
---------------
**MemoryStore:** The memory store deals with reading and writing data from and to the simulator’s memory.  This class loads all the data from the byte file at the beginning of the simulator’s execution, and provides access to that data to its callers.
**Register:** A register is essentially a class that contains two values, the current value of the register and the next value of the register.  A register also contains a “tick” method that when executed will write the next value into the current value.  This is the base framework for many other data structures in my simulator.
**RegisterFile:** The register file is the encapsulation of a collection of registers.  It contains methods to set and get the current values of a register, as well as tick over the whole register file.  Each register in the file is identified by a RegisterName and can be accessed or set using that name.  The register file also provides methods for disabling the writing of registers during the next tick, which is used for stalls.
**RegisterName:** A register name is the enumerated identifier for a register.  They are split into two groups: primitive registers (r0-r31) that would appear in the basic register file and non-primitive registers (control lines, pipeline registers, etc) that wouldn’t appear in the basic register file.
**ProgramCounter:** The program counter is a special case extension of Register that keeps track of the next instruction to be executed.
**PipelineRegister:** A pipeline register is an extension of the RegisterFile that allows for non-primitive registers to be stored.  It also provides methods for forwarding its values to the next pipeline register in the pipeline.
**PipelineStage:**  This is the interface that describes a single stage in the pipeline.  It provides a single method “run” that is the main entry point for the MIPS simulator to execute the pipeline stage. 
**Fetch:**  This is the pipeline stage for fetching the next instruction.
**Decode:** This is the pipeline stage for decoding the instruction.  It deals with setting control lines, branching and jumping, and detecting stalls necessary because of data hazards.
**Execute:** This pipeline stage deals with the execution of any ALU related code.  It also checks for data forwarding from either the previous execution stage or the memory stage.
**Memory:** This pipeline stage writes and reads data to and from the MemoryStore.
**Writeback:** This pipeline stage writes data to the RegisterFile.  Since it is the last pipeline stage it also deals with determining if the program should terminate and if the current instruction was a no-op and therefore shouldn’t be counted towards the total instruction count.
**Mips:** This is the main control class of the program.  It continually executes the pipeline stages in order (with the exception of writeback which must be run before decode) until the halt command hits the writeback stage.  It also deals with counting the total instructions and cycles and outputting the performance and register file at the completion of execution.

Performance Features
--------------------
-  branch detection at the decode stage
-	data hazard stall detection at the decode stage
-	data forwarding from execution to execution
-	data forwarding from memory to execution
