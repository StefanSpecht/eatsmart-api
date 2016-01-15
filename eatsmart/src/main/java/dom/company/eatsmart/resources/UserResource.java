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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.service.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	
	UserService userService = new UserService();
	
	@GET
	public List<User> getUsers() {
		return userService.getUsers();
	}
	
	@POST
	public Response addUser(User user, @Context UriInfo uriInfo) {
		User newUser = userService.addUser(user);
		String newId = String.valueOf(newUser.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
					.entity(newUser)
					.build();
	}
	
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") long userId) {
		return Response.ok(userService.getUser(userId))
					.build();
	}
	
	@PUT
	@Path("/{userId}")	
	public Response updateUser(@PathParam("userId") long userId, User user) {
		user.setId(userId);		
		User updatedUser = userService.updateUser(user);		
		return Response.ok(updatedUser)
					.build();		
	}
	
	@DELETE
	@Path("/{userId}")	
	public Response deleteUser(@PathParam("userId") long userId) {
		userService.deleteUser(userId);
		return Response.noContent()
					.build();		
	}
		
	@Path("/{userId}/recipes")	
	public RecipeResource getRecipeResource() {
		return new RecipeResource();
	}
}
