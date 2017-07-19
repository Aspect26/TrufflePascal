package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of set-type variables.
 */
@CompilerDirectives.ValueType
public class SetTypeValue implements Serializable {

    private final Set<Object> data;

    /**
     * Creates an empty set.
     */
    public SetTypeValue() {
        this(new HashSet<>());
    }

    /**
     * Creates a set with predefined values.
     * @param data the values
     */
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

    /**
     * Two set-type variables are equal if the contain the same values.
     */
    public boolean equals(SetTypeValue comp) {
        Set<Object> compData = comp.getData();
        return this.data.containsAll(compData) && compData.containsAll(this.data);
    }

    /**
     * Returns the number of elements stored in the set.
     */
    public int getSize() {
        return this.data.size();
    }

    public boolean contains(Object o) {
        return this.data.contains(o);
    }

    /**
     * Does set union operation on the arguments and returns the result.
     */
    public static SetTypeValue union(SetTypeValue left, SetTypeValue right) {
        SetTypeValue result = left.createDeepCopy();
        result.getData().addAll(right.getData());

        return result;
    }

    /**
     * Does set difference operation on the arguments and returns the result.
     */
    public static SetTypeValue difference(SetTypeValue left, SetTypeValue right) {
        SetTypeValue result = left.createDeepCopy();
        result.getData().removeAll(right.getData());

        return result;
    }

    /**
     * Does set intersection operation on the arguments and returns the result.
     */
    public static SetTypeValue intersect(SetTypeValue left, SetTypeValue right) {
        SetTypeValue result = left.createDeepCopy();
        result.getData().retainAll(right.getData());

        return result;
    }

    /**
     * Does symmetric difference operation on the arguments and returns the result.
     */
    public static SetTypeValue symmetricDifference(SetTypeValue left, SetTypeValue right) {
        SetTypeValue union = SetTypeValue.union(left, right);
        SetTypeValue intersection = SetTypeValue.intersect(left, right);

        return SetTypeValue.difference(union, intersection);
    }
}
