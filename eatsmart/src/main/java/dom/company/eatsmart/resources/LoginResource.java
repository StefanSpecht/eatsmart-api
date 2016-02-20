package dom.company.eatsmart.resources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.Uri;

import dom.company.eatsmart.authentication.AuthenticationService;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.service.UserService;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
	
	public static final String AUTHENTICATION_HEADER = "Authorization";
	UserService userService = new UserService();
	AuthenticationService authenticationService = new AuthenticationService();
	
	@GET
	public Response login(@Context UriInfo uriInfo, @Context ContainerRequestContext containerRequestContext) {
		
		String authCredentials = containerRequestContext.getHeaderString(AUTHENTICATION_HEADER);
		String username = authenticationService.decode(authCredentials).get("username");
		User user = userService.getUser(username);
		
		URI forwardUri = uriInfo.getBaseUriBuilder()
				.path("users/" + user.getId())
				.build();		
		
		return Response.seeOther(forwardUri)
				.build();
		
	}
}
