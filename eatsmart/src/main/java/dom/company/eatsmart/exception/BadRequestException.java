package dom.company.eatsmart.exception;

public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 1828075574348893334L;

	public BadRequestException(String message) {
		super(message);
	}
}
