package cz.cuni.mff.d3s.trupple.language.customvalues;

public class PascalArray {
	private final Object[] data;
	private final PascalOrdinal ordinalSource;
	
	public PascalArray(PascalOrdinal ordinalSource, Object[] data) {
		this.ordinalSource = ordinalSource;
		this.data = data;
	}
	
	private Object getValueAt(Object index) {
        // TODO: throw custom exception at array index out of range
		int realIndex = ordinalSource.getRealIndex(index);
		return data[realIndex];
	}

	public Object getValueAt(Object[] indexes) {
        Object value = this;

        for (Object index : indexes) {
            value = ((PascalArray) value).getValueAt(index);
        }

        return value;
    }
	
	private void setValueAt(Object index, Object value) {
		// TODO: throw custom exception at array index out of range
        this.data[ordinalSource.getRealIndex(index)] = value;
	}

	public void setValueAt(Object[] indexes, Object value) {
        PascalArray innerArray = this;

        for (int i = 0; i < indexes.length - 1; ++i) {
            Object index = indexes[i];
            innerArray = (PascalArray) innerArray.getValueAt(index);
        }

        innerArray.setValueAt(indexes[indexes.length - 1], value);
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
