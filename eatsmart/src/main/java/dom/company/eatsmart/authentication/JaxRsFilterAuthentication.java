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
	private static final String VERIFICATION_PATH_PATTERN = "verification.+";

	@Override
	public void filter(ContainerRequestContext containerRequestContext)
			throws WebApplicationException {

		String authCredentials = containerRequestContext.getHeaderString(AUTHENTICATION_HEADER);
		UriInfo uriInfo = containerRequestContext.getUriInfo();
		
		//Allow access to free area
		if (isAuthenticationRequired(uriInfo)) {
		
			AuthenticationService authenticationService = new AuthenticationService();
			
			authenticationService.authenticate(authCredentials);		
			User authenticatedUser = authenticationService.getAuthenticatedUser();
			
			Boolean isUserAuthorized = authenticationService.isAuthorized(authenticatedUser, uriInfo);
			
			if (!isUserAuthorized) {
				throw new UnauthorizedException("You are not authorized to access this resource");
			}
		}
		
	}

	private boolean isAuthenticationRequired(UriInfo uriInfo) {
		String requestedPath = uriInfo.getPath();
		
		//Allow everybody to access root
		if (requestedPath.equals("")) return false;
		
		//Allow everybody to access user registration
		if (requestedPath.equals("users")) return false;
		
		//Allow everybody to submit a password reset request
		if (requestedPath.equals("pwdResetRequest")) return false;
		
		//Allow everybody to Token verification
		if (requestedPath.matches(VERIFICATION_PATH_PATTERN)) return false;
			
		//in any other case, authentication is required
		return true;
	}
}