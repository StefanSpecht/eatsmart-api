package dom.company.eatsmart.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.service.UserService;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {
	
	@GET
	public Response getRoot(@Context UriInfo uriInfo) {
		return Response.noContent()
				.links(getLinks(uriInfo))
				.build();
	}
	
	@Path("/users")
	public UserResource getUserResource() {
		return new UserResource();
	}
	
	@Path("/login")
	public LoginResource getLoginResource() {
		return new LoginResource();
	}
	
	@Path("/pwdReset")
	public PwdResetResource getPwdResetResource() {
		return new PwdResetResource();
	}
	
	@Path("/foodCatalogue")
	public FoodCatalogueResource getFoodCatalogueResource() {
		return new FoodCatalogueResource();
	}
		
	private Link[] getLinks(UriInfo uriInfo) {
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET").build();
		Link register = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/users").build()).rel("register").param("verb", "POST").build();
		Link login = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/login").build()).rel("login").param("verb", "GET").build();
		Link pwdreset = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/pwdreset").build()).rel("pwdreset").param("verb", "POST").build();
		Link foodCatalogue = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/foodCatalogue").build()).rel("foodCatalogue").param("verb", "GET").build();
		
		return new Link[] {self, register, login, pwdreset, foodCatalogue};
	}
}
