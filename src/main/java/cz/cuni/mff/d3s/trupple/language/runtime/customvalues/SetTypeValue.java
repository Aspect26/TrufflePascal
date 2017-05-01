package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@CompilerDirectives.ValueType
public class SetTypeValue implements Serializable {

    private final Set<Object> data;

    public SetTypeValue() {
        this(new HashSet<>());
    }

    public SetTypeValue(Set<Object> data) {
        this.data = data;
    }

    public Set<Object> getData() {
        return this.data;
    }

    public SetTypeValue createDeepCopy() {
        Set<Object> dataCopy = new HashSet<>();
        for (Object item : data) {
            dataCopy.add(item);
        }

        return new SetTypeValue(dataCopy);
    }

    public boolean equals(SetTypeValue comp) {
        Set<Object> compData = comp.getData();
        return this.data.containsAll(compData) && compData.containsAll(this.data);
    }

    public int getSize() {
        return this.data.size();
    }

    public boolean contains(Object o) {
        return this.data.contains(o);
    }

    public static SetTypeValue union(SetTypeValue left, SetTypeValue right) {
        SetTypeValue result = left.createDeepCopy();
        result.getData().addAll(right.getData());

        return result;
    }

    public static SetTypeValue difference(SetTypeValue left, SetTypeValue right) {
        SetTypeValue result = left.createDeepCopy();
        result.getData().removeAll(right.getData());

        return result;
    }

    public static SetTypeValue intersect(SetTypeValue left, SetTypeValue right) {
        SetTypeValue result = left.createDeepCopy();
        result.getData().retainAll(right.getData());

        return result;
    }

    public static SetTypeValue symmetricDifference(SetTypeValue left, SetTypeValue right) {
        SetTypeValue union = SetTypeValue.union(left, right);
        SetTypeValue intersection = SetTypeValue.intersect(left, right);

        return SetTypeValue.difference(union, intersection);
    }
}
