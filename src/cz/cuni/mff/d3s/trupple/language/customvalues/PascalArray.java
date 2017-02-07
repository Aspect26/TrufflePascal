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
	
	public PascalArray createDeepCopy() {
		Object[] dataCopy = new Object[this.data.length];

        if (this.data[0] instanceof PascalArray) {
            for (int i = 0; i < dataCopy.length; ++i) {
                dataCopy[i] = ((PascalArray)this.data[i]).createDeepCopy();
            }
        } else {
            System.arraycopy(this.data, 0, dataCopy, 0, this.data.length);
        }

		return new PascalArray(this.ordinalSource, dataCopy);
	}
}
