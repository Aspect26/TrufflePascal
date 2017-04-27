package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.BinaryNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

@NodeInfo(shortName = "in")
public abstract class InNode extends BinaryNode {

    @Override
    public boolean verifyChildrenNodeTypes() {
        if (!(getRightNode().getType() instanceof SetDescriptor)) {
            return false;
        }

        TypeDescriptor valueType = getLeftNode().getType();
        SetDescriptor set = (SetDescriptor) getRightNode().getType();
        return set.getInnerType() == valueType || valueType.convertibleTo(set.getInnerType());
    }

    @Specialization
    boolean inOperation(Object o, SetTypeValue set) {
        return set.contains(o);
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
