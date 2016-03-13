package dom.company.eatsmart.exception;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class VerificationNotSuccessfulExceptionMapper implements ExceptionMapper<VerificationNotSuccessfulException> {

	@Override
	public Response toResponse(VerificationNotSuccessfulException ex) {
		
		URI badResponse = ex.getUriInfo().getBaseUriBuilder().path("../../html/" + ex.getType() + "/failed.html").build();
		return Response.seeOther(badResponse)
				.build();
	}
}
