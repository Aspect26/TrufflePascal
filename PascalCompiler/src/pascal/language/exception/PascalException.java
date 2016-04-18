package pascal.language.exception;

public class PascalException extends RuntimeException {

	private static final long serialVersionUID = 2357051481037768345L;
	private static String message;
	
	public PascalException(String msg){
		message = msg;
	}

	@Override
	public String getMessage(){
		return message;
	}
}
