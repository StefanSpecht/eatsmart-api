package dom.company.eatsmart.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.service.RecipeService;

@Path("/recipes")
public class RecipeResource {
	
	RecipeService recipeService = new RecipeService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recipe> getResources() {
		return recipeService.getRecipes();
	}
	
	@GET
	@Path("/{recipeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe getResource(@PathParam("recipeId") long recipeId) {
		return recipeService.getRecipe(recipeId);
	}
	
	@PUT
	@Path("/{recipeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe updateResource(@PathParam("recipeId") long recipeId, Recipe recipe) {
		recipe.setId(recipeId);
		return recipeService.updateRecipe(recipe);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe addResource(Recipe recipe) {
		return recipeService.addRecipe(recipe);
	}
}
