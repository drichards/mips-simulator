package mips;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simulator for memory that stores and retrieves data on word boundaries.
 * 
 * This memory contains both the instruction memory and the data memory.
 * 
 * Note that although use longs in both getting and setting values in memory
 * we are actually using values of length 32 bits.  The reason we use
 * longs is because Java has no concept of unsigned ints, and we want to 
 * avoid using negative numbers.
 */
public class MemoryStore {
	/**
	 * The internal representation of the memory.
	 */
	private ArrayList<Long> memory = new ArrayList<Long>();
	            
	/**
	 * Create a new memory store from the given input file.
	 * 
	 * @param filename The name of the input file.
	 * @throws IOException Thrown if there's a problem opening the
	 * file of the given name.
	 */
	public MemoryStore(String filename) throws IOException {
		// create a new input stream
		FileInputStream input = new FileInputStream(filename);
		
		// create a place to store the words we read in
		byte[] b = new byte[4];
		
		// read in the data in the given file
		while (input.read(b) != -1) {
			if (memory.size() == 1027) {
				int foo = 1;
			}
			// create a word from the 4 bytes
			long word = getUnsignedValue(b[0]);
			word += getUnsignedValue(b[1]) << 8;
			word += getUnsignedValue(b[2]) << 16;
			word += getUnsignedValue(b[3]) << 24;
			
			// add the word to our memory store
			memory.add(word);
		}
	}
	
	/**
	 * Get the value at the given location in memory.  The given location should
	 * be a byte location, but if it isn't word-aligned it will be truncated.
	 * 
	 * @param location The byte address of the word to retrieve.
	 * @return The word at the given byte address.
	 */
	public long getValue(long location) {
		// we want to access the memory on a word boundary
		long memoryAddress = location >> 2;
		
		// error if we're out of bounds
		if (memoryAddress >= memory.size()) {
			throw new IllegalArgumentException("Memory address out of bounds");
		}
		
		// Return the word we want.  Note that it should be safe to
		// cast memoryAddress to the int required by ArrayList.get because
		// we know that the given location is at most 32 bits long, so shifting
		// right two bits should give us a non-negative number every time we
		// go to cast it.
		return memory.get((int)memoryAddress);
	}
	
	/**
	 * Store the given value at the given location in memory.
	 * The given location should be a byte location, but if it isn't 
	 * word-aligned it will be truncated.
	 * 
	 * @param location The byte address to write.
	 * @param value The value to write at the given location.
	 */
	public void storeValue(long location, long value) {
		// get the word address from the byte address
		long memoryAddress = location >> 2;
		
		// error if we're out of bounds
		if (memoryAddress >= memory.size()) {
			throw new IllegalArgumentException("Memory address out of bounds");
		}
		
		// Set the word we want.  Note that it should be safe to
		// cast memoryAddress to the int required by ArrayList.get because
		// we know that the given location is at most 32 bits long, so shifting
		// right two bits should give us a non-negative number every time we
		// go to cast it.
		memory.set((int)memoryAddress, value);
	}
	
	public long getUnsignedValue(byte b) {
		if (b >= 0) { return b; }
		
		return 256 + b;
	}
}
