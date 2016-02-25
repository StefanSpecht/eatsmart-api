package dom.company.eatsmart.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import dom.company.eatsmart.model.ErrorMessage;

@Provider
public class ClassCastExceptionMapper implements ExceptionMapper<ClassCastException> {

	@Override
	public Response toResponse(ClassCastException ex) {
		
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400);
			return Response.status(400)
					.entity(errorMessage)
					.build();	
	}
}