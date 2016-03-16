package dom.company.eatsmart.exception;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import dom.company.eatsmart.model.ErrorMessage;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

	@Override
	public Response toResponse(PersistenceException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500);
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(errorMessage)
				.build();
	}
}
