package dom.company.eatsmart.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -4084260349729464585L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
}
