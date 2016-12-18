package cz.cuni.mff.d3s.trupple.language.customvalues;

public class PascalArray implements ICustomValue {
	private final Object[] data;
	private final PascalOrdinal ordinalSource;
	
	public PascalArray(PascalOrdinal ordinalSource, Object[] data) {
		this.ordinalSource = ordinalSource;
		this.data = data;
	}
	
	public Object getValueAt(Object index) {
        // TODO: throw custom exception at array index out of range
		int realIndex = ordinalSource.getRealIndex(index);
		return data[realIndex];
	}
	
	public void setValueAt(Object index, Object value) {
		// TODO: throw custom exception at array index out of range
        this.data[ordinalSource.getRealIndex(index)] = value;
	}
	
	@Override
	public Object getValue() {
		return data;
	}
	
	public PascalArray createCopy() {
		Object[] dataCopy = new Object[this.data.length];
		System.arraycopy(this.data, 0, dataCopy, 0, this.data.length);
		return new PascalArray(this.ordinalSource, dataCopy);
	}
}
