package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;

@CompilerDirectives.ValueType
public interface PascalArray {

    Object getValueAt(int index);

    void setValueAt(int index, Object value);

    Object createDeepCopy();

}