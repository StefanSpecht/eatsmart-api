package dom.company.eatsmart.exception;

import javax.ws.rs.core.UriInfo;

public class VerificationNotSuccessfulException extends RuntimeException {
	
	private static final long serialVersionUID = 7603298316370568555L;
	
	private UriInfo uriInfo;
	private String type;
	
	public VerificationNotSuccessfulException(UriInfo uriInfo, String type) {
		super("Token verification was not successful");
		this.uriInfo = uriInfo;
		this.type = type;
	}

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
