package cz.cuni.mff.d3s.trupple.language.customvalues;

import java.util.HashSet;
import java.util.Set;

public class SetTypeValue implements ICustomValue {

    private final Set<Object> data;

    public SetTypeValue() {
        this(new HashSet<>());
    }

    public SetTypeValue(Set<Object> data) {
        this.data = data;
    }

    @Override
    public Object getValue() {
        return this;
    }

    public SetTypeValue createDeepCopy() {
        Set<Object> dataCopy = new HashSet<>();
        for (Object item : data) {
            dataCopy.add(item);
        }

        return new SetTypeValue(dataCopy);
    }
}
