package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

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
	protected String add(String left, char right) {
		return left.concat(new String(new char[] {right}));
	}

    @Specialization
    protected String add(String left, String right) {
        return left.concat(right);
    }

}
