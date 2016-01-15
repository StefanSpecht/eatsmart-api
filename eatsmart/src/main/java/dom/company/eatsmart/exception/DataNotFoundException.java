package dom.company.eatsmart.exception;

public class DataNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -6980751773137324211L;

	public DataNotFoundException(String message) {
		super(message);
	}
}
