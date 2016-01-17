package dom.company.eatsmart.exception;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = -2694448988938777256L;

	public UnauthorizedException(String message) {
		super(message);
	}
}