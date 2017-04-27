package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PCharValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;

@NodeInfo(shortName = "+")
public abstract class AddNodeTP extends AddNode {

    AddNodeTP() {
        super();
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(CharDescriptor.getInstance(), CharDescriptor.getInstance()), StringDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(CharDescriptor.getInstance(), StringDescriptor.getInstance()), StringDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(StringDescriptor.getInstance(), CharDescriptor.getInstance()), StringDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(StringDescriptor.getInstance(), StringDescriptor.getInstance()), StringDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(new PointerDescriptor(PCharDesriptor.getInstance()), new PointerDescriptor(PCharDesriptor.getInstance())), new PointerDescriptor(PCharDesriptor.getInstance()));
    }

	@Specialization
	protected String add(char left, char right) {
		return new String(new char[] { left, right } );
	}

	@Specialization
	protected String add(char left, String right) {
		String result = new String(new char[] {left});
		return result.concat(right);
	}

	@Specialization
	protected PascalString add(PascalString left, char right) {
		return left.concatenate(right);
	}

    @Specialization
    protected PascalString add(PascalString left, PascalString right) {
        return left.concatenate(right);
    }

    @Specialization
    protected PCharValue add(PCharValue left, PCharValue right) {
	    return PCharValue.concat(left, right);
    }

}
