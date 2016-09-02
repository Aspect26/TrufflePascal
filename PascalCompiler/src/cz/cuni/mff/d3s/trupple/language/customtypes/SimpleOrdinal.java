package cz.cuni.mff.d3s.trupple.language.customtypes;

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

	@Override
	public int getRealIndex(Object index) {
		if(type == Type.NUMERIC) {
			//TODO: thisssssss casssssstsssssss :'(
			return (int)((Long)index - firstIndex);
		}
		else if(type == Type.BOOLEAN) {
			return ((Boolean)index)? 1 : 0;
		}
		else if(type == Type.CHAR) {
			return (Character)index;
		}
		
		//TODO throw exception
		return 0;
	}
}
