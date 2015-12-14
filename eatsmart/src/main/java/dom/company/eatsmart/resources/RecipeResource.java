package dom.company.eatsmart.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.service.RecipeService;

@Path("/recipes")
public class RecipeResource {
	
	RecipeService recipeService = new RecipeService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recipe> getAllResource() {
		return recipeService.getAllRecipes();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Recipe addResource(Recipe recipe) {
		return recipeService.addRecipe(recipe);
	}
}
