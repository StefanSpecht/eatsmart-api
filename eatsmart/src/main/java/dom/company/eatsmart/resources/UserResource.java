package dom.company.eatsmart.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	
	UserService userService = new UserService();
	@POST
	public Response registerUser(@Valid User user, @Context UriInfo uriInfo) {
		User newUser = userService.registerUser(user, uriInfo);
		String newId = String.valueOf(newUser.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
					.entity(newUser)
					.links(getLinks(uriInfo, "POST"))
					.build();
	}
	
	@Path("{userId}")
	@GET	
	public Response getUser(@PathParam("userId") long userId, @Context UriInfo uriInfo) {
		return Response.ok(userService.getUser(userId))
				.links(getLinks(uriInfo, "GET"))
				.build();
	}
	
	@PUT
	@Path("{userId}")	
	public Response updateUser(@PathParam("userId") long userId, @Valid User user, @Context UriInfo uriInfo) {
		user.setId(userId);		
		userService.updateUser(user);		
		return Response.noContent()
				.links(getLinks(uriInfo, "PUT"))
				.build();		
	}
	
	@Path("/{userId}/recipes")	
	public RecipeResource getRecipeResource() {
		return new RecipeResource();
	}
	
	@Path("/{userId}/foods")	
	public FoodResource getFoodResource() {
		return new FoodResource();
	}
	
	@Path("/{userId}/fridgeStocks")	
	public StockResource getStockResource() {
		return new StockResource();
	}
	
	@Path("/{userId}/menuSchedules")	
	public MenuScheduleResource getMenuScheduleResource() {
		return new MenuScheduleResource();
	}
	
	@Path("/{userId}/products")	
	public ProductResource getProductResource() {
		return new ProductResource();
	}
	
	@Path("/{userId}/shoppingList")
	public ShoppingListResource getShoppingListResource() {
		return new ShoppingListResource();
	}

	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,PUT").build();
		Link recipes = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/recipes").build()).rel("recipes").param("verb", "GET").build();
		Link fridgeStocks = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/fridgeStocks").build()).rel("fridgeStocks").param("verb", "GET").build();
		Link menuSchedules = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/menuSchedules").build()).rel("menuSchedules").param("verb", "GET").build();
		Link shoppingList = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/shoppingList").build()).rel("shoppingList").param("verb", "GET").build();
		Link foods = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("/foods").build()).rel("foods").param("verb", "GET").build();
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		Link next = Link.fromUri(uriInfo.getBaseUri()).rel("next").param("verb", "GET").build();
		switch (method) {
			case "GET":
				return new Link[] {self, recipes, fridgeStocks, menuSchedules, shoppingList, foods, logout};
			case "PUT":
				return new Link[] {self, logout};
			case "POST":
				return new Link[] {next};
			default:
				return new Link[] {};
		}
		 
		
	}
}
