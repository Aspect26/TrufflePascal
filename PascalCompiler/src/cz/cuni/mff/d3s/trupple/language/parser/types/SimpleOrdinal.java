package cz.cuni.mff.d3s.trupple.language.parser.types;

public class SimpleOrdinal implements IOrdinalType {

	private final int firstIndex;
	private final int size; 
	private IOrdinalType.Type type;
	
	public SimpleOrdinal(int firstIndex, int size, IOrdinalType.Type type) {
		this.firstIndex = firstIndex;
		this.size = size;
		this.type = type;
	}
	
	@Override
	public int getFirstIndex() {
		return this.firstIndex;
	}

	@Override
	public int getSize() {
		return this.size;
	}
	
	@Override
	public IOrdinalType.Type getType() {
		return this.type;
	}
}
