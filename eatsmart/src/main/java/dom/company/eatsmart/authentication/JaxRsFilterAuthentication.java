package dom.company.eatsmart.authentication;

import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.Uri;

import dom.company.eatsmart.exception.UnauthorizedException;
import dom.company.eatsmart.model.User;

@Provider
public class JaxRsFilterAuthentication implements ContainerRequestFilter {
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void filter(ContainerRequestContext containerRequestContext)
			throws WebApplicationException {

		String authCredentials = containerRequestContext.getHeaderString(AUTHENTICATION_HEADER);
		UriInfo uriInfo = containerRequestContext.getUriInfo();
		
		AuthenticationService authenticationService = new AuthenticationService();
		
		authenticationService.authenticate(authCredentials);		
		User authenticatedUser = authenticationService.getAuthenticatedUser();
		
		Boolean isUserAuthorized = authenticationService.isAuthorized(authenticatedUser, uriInfo);
		
		if (!isUserAuthorized) {
			throw new UnauthorizedException("You are not authorized to access this resource");
		}
		


	}
}