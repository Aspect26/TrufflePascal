package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.IndexOutOfBoundsException;

public class FixedPascalArray implements PascalArray {

    private final Object[] data;
    private final PascalOrdinal ordinalSource;

    public FixedPascalArray(PascalOrdinal ordinalSource, Object[] data) {
        this.ordinalSource = ordinalSource;
        this.data = data;
    }

    public Object getValueAt(Object index) {
        int realIndex = ordinalSource.getRealIndex(index);
        this.checkArrayIndex(realIndex);
        return data[realIndex];
    }

    public Object getValueAt(Object[] indexes) {
        Object value = this;

        for (Object index : indexes) {
            value = ((PascalArray) value).getValueAt(index);
        }

        return value;
    }

    public void setValueAt(Object index, Object value) {
        int indexValue = ordinalSource.getRealIndex(index);
        this.checkArrayIndex(indexValue);
        this.data[indexValue] = value;
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

        return new FixedPascalArray(this.ordinalSource, dataCopy);
    }

    private void checkArrayIndex(int index) {
        if (index >= this.data.length) {
            throw new IndexOutOfBoundsException();
        }
    }
}
