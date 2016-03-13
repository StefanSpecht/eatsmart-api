package dom.company.eatsmart.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import dom.company.eatsmart.model.ErrorMessage;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException ex) {
		Response response = ex.getResponse();
		
	
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), response.getStatus());
		return Response.fromResponse(response)
				.entity(errorMessage)
				.build();
		
	}
}
