package dom.company.eatsmart.exception;

public class DataConflictException extends RuntimeException {
	
	private static final long serialVersionUID = 4152005874165224840L;

	public DataConflictException(String message) {
		super(message);
	}
}
