package dom.company.eatsmart2.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dom.company.eatsmart2.model.Recipe;
import dom.company.eatsmart2.service.RecipeService;

@Path("/recipes")
public class RecipeResource {
	
	RecipeService recipeService = new RecipeService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Recipe> getResource() {
		return recipeService.getAllRecipes();
	}
}
