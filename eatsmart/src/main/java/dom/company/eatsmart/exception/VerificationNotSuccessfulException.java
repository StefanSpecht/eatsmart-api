package dom.company.eatsmart.exception;

import javax.ws.rs.core.UriInfo;

public class VerificationNotSuccessfulException extends RuntimeException {
	
	private static final long serialVersionUID = 7603298316370568555L;
	
	private UriInfo uriInfo;

	public VerificationNotSuccessfulException(UriInfo uriInfo) {
		super("Token verification was not successful");
		this.uriInfo = uriInfo;
	}

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}
	
	
}
