package dom.company.eatsmart.exception;

public class InternalServerErrorException extends RuntimeException {
	
	private static final long serialVersionUID = 1088685820717540977L;

	public InternalServerErrorException(String message) {
		super(message);
	}
}
