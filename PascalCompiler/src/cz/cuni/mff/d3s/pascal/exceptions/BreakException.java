package cz.cuni.mff.d3s.pascal.exceptions;

public class BreakException extends PascalRuntimeException{

	private static final long serialVersionUID = 1461738434684232542L;
	
	protected BreakException(){
		
	}
	
	public static BreakException SINGLETON = new BreakException();
}
