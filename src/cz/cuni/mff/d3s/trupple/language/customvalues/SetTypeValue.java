package cz.cuni.mff.d3s.trupple.language.customvalues;

import java.util.HashSet;
import java.util.Set;

public class SetTypeValue implements ICustomValue {

    private final PascalOrdinal baseOrdinal;
    private final Set<Object> data;

    public SetTypeValue(PascalOrdinal baseOrdinal) {
        this.baseOrdinal = baseOrdinal;
        this.data = new HashSet<Object>();
    }

    @Override
    public Object getValue() {
        return this;
    }
}
