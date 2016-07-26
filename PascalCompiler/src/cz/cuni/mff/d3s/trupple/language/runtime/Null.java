package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class Null implements TruffleObject {

	private Null() {
	}

	public static final Null SINGLETON = new Null();

	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Auto-generated method stub
		return null;
	}
}
