package dom.company.eatsmart.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.model.Ingredient;
import dom.company.eatsmart.service.ShoppingListService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShoppingListResource {
	
	ShoppingListService shoppingListService = new ShoppingListService();
	
	@GET
	public Response getShoppingListItems(@PathParam("userId") long userId, @Context UriInfo uriInfo) {
		
		List<Ingredient> shoppingListItems = shoppingListService.getShoppingListItems(userId);
		GenericEntity<List<Ingredient>> entity = new GenericEntity<List<Ingredient>>(shoppingListItems) {};
		return Response.ok(entity)
				.links(getLinks(uriInfo, "GET"))
				.build();
	}
	
	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET").build();
		Link user = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("user").param("verb", "GET").build();
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET":
				return new Link[] {self, user, logout};
			default:
				return new Link[] {};
		}
	}
}
