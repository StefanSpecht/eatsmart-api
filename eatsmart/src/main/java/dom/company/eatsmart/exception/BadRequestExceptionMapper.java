package dom.company.eatsmart.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import dom.company.eatsmart.model.ErrorMessage;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

	@Override
	public Response toResponse(BadRequestException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 409);
		return Response.status(Status.CONFLICT)
				.entity(errorMessage)
				.build();
	}
}
