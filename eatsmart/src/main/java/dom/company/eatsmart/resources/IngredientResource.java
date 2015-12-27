package dom.company.eatsmart.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dom.company.eatsmart.model.Ingredient;
import dom.company.eatsmart.service.IngredientService;

@Path("/ingredients")

public class IngredientResource {
	
	IngredientService ingredientService = new IngredientService();
	
	@GET
	public void getResources() {
		ingredientService.addIngredient();
	}
	
}
