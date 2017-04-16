package cz.cuni.mff.d3s.trupple.language.customvalues;

public interface PascalArray {

    Object getValueAt(Object index);

    Object getValueAt(Object[] indexes);

    void setValueAt(Object index, Object value);

    void setValueAt(Object[] indexes, Object value);

    Object createDeepCopy();

}