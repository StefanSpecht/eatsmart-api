package dom.company.eatsmart.resources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;

import dom.company.eatsmart.service.IngredientService;

@Path("/ingredients")

public class IngredientResource {
	
	IngredientService ingredientService = new IngredientService();
	
	@GET
	public void getResources() {
		ingredientService.addIngredient();
	}
	
}
