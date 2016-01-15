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
		
		if (response.getStatus() == 405) {
			ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 405);
			return Response.fromResponse(response)
					.entity(errorMessage)
					.build();
		}
		
		return ex.getResponse();	
	}
}
