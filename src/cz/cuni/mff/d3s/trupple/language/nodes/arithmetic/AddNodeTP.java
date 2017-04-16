package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.PCharValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;

@NodeInfo(shortName = "+")
public abstract class AddNodeTP extends AddNode {

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
