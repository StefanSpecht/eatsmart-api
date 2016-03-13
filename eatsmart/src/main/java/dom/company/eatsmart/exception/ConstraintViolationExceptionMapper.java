package dom.company.eatsmart.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import dom.company.eatsmart.model.ErrorMessage;

//@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException ex) {
		
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400);
			return Response.status(400)
					.entity(errorMessage)
					.build();	
	}
}
