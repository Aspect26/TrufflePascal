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

	public static SimpleOrdinal booleanOrdinalSingleton = new SimpleOrdinal(0, 2, IOrdinalType.Type.BOOLEAN);
    public static SimpleOrdinal charOrdinalSingleton = new SimpleOrdinal(0, 256, IOrdinalType.Type.CHAR);
	
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
