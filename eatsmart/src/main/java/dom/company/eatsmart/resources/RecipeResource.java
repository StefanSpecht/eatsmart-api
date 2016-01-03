package dom.company.eatsmart.resources;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
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

import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.service.RecipeService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecipeResource {
	
	RecipeService recipeService = new RecipeService();
	
	@GET
	public List<Recipe> getRecipes(@PathParam("userId") long userId) {
		return recipeService.getRecipes(userId);
	}
	
	@GET
	@Path("/{recipeId}")
	public Recipe getResource(@PathParam("userId") long userId, @PathParam("recipeId") long recipeId) {
		return recipeService.getRecipe(userId, recipeId);
	}
	
	@PUT
	@Path("/{recipeId}")	
	public Recipe updateResource(@PathParam("recipeId") long recipeId, Recipe recipe) {
		recipe.setId(recipeId);
		return recipeService.updateRecipe(recipe);
	}
	
	@POST
	public Response addRecipe(Recipe recipe, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		Recipe newRecipe = recipeService.addRecipe(recipe, userId);
		String newId = String.valueOf(newRecipe.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
					.entity(newRecipe)
					.build();
	}
	
	@DELETE
	@Path("/{recipeId}")	
	public void deleteResource(@PathParam("recipeId") long recipeId, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		recipeService.removeRecipe(recipeId, userId);
	}
}
