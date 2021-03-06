package dom.company.eatsmart.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {
	
	@GET
	public Response getRoot(@Context UriInfo uriInfo) {
		return Response.noContent()
				.links(getLinks(uriInfo))
				.build();
	}
	
	@Path("users")
	public UserResource getUserResource() {
		return new UserResource();
	}
	
	@Path("login")
	public LoginResource getLoginResource() {
		return new LoginResource();
	}
	
	@Path("pwdResetRequest")
	public PwdResetResource getPwdResetResource() {
		return new PwdResetResource();
	}
	
	@Path("verification")
	public VerificationResource getVerificationResource() {
		return new VerificationResource();
	}
	
	private Link[] getLinks(UriInfo uriInfo) {
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET").build();
		Link register = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/users").build()).rel("register").param("verb", "POST").build();
		Link login = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/login").build()).rel("login").param("verb", "GET").build();
		Link pwdResetRequest = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/pwdResetRequest").build()).rel("pwdResetRequest").param("verb", "POST").build();
		
		return new Link[] {self, register, login, pwdResetRequest};
	}
}
