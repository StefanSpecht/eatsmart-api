package dom.company.eatsmart.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import dom.company.eatsmart.exception.InternalServerErrorException;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.service.FoodService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FoodResource {
	
	FoodService foodService = new FoodService();
	private long parentFoodId;	
		
	public FoodResource() {
		this.parentFoodId = 0;
	}
	
	public FoodResource(long parentFoodId) {
		this.parentFoodId = parentFoodId;
	}
	
	@GET
	public Response getFoods(@PathParam("userId") long userId, @Context UriInfo uriInfo) {
		if (this.parentFoodId == 0) {
			List<Food> foods = foodService.getFoods(userId);
			GenericEntity<List<Food>> entity = new GenericEntity<List<Food>>(foods) {};
			
			return Response.ok(entity)
					.links(getLinks(uriInfo, "GET_ALL"))
					.build();
		}
		else {
			List<Food> foods = foodService.getChildFoods(userId, parentFoodId);
			GenericEntity<List<Food>> entity = new GenericEntity<List<Food>>(foods) {};
			
			return Response.ok(entity)
					.links(getLinks(uriInfo, "GET_CHILDFOOD"))
					.build();
		}		
	}
	
	@Path("{foodId}")
	@GET
	public Response getFood(@PathParam("userId") long userId, @PathParam("foodId") long foodId, @Context UriInfo uriInfo) {
		
		if (parentFoodId == 0) {
			Food food = foodService.getFood(userId, foodId);
			
			return Response.ok(food)
					.links(getLinks(uriInfo, "GET"))
					.build();
		}
		else {
			Food food = foodService.getChildFood(userId, foodId, parentFoodId);
			
			return Response.ok(food)
					.links(getLinks(uriInfo, "GET"))
					.build();
		}
	}
		
	@POST
	public Response addFood(@Valid Food food, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		if (this.parentFoodId == 0) {
			Food newFood = foodService.addFood(food, userId);
			
			String newId = String.valueOf(newFood.getId());
			URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
			return Response.created(uri)
					.links(getLinks(uriInfo, "POST"))
					.entity(newFood)
					.build();
		}
		else {
			Food newFood = foodService.addFood(food, userId, parentFoodId);
			
			String newId = String.valueOf(newFood.getId());
			URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
			return Response.created(uri)
					.links(getLinks(uriInfo, "POST"))
					.entity(newFood)
					.build();
		}
		
		
	}
	
	@Path("/{parentFoodId}/childFoods")	
	public FoodResource getChildFoodResource(@PathParam("parentFoodId") long parentFoodId) {
		return new FoodResource(parentFoodId);
	}
	
	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,POST").build();
		Link all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("all").param("verb", "GET,POST").build();
		Link newFood = Link.fromUri(uriInfo.getAbsolutePath()).rel("new").param("verb", "POST").build();
		Link childs = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("childFoods").build()).rel("childs").param("verb", "GET,POST").build();
		Link parent = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("parent").param("verb", "GET,POST").build();
		Link user = Link.fromUri(this.buildUserUri(uriInfo)).rel("user").param("verb", "GET").build();
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET_ALL":
				return new Link[] {self, newFood, user, logout};
			case "POST":
				return new Link[] {self, user, logout};
			case "GET":
				return new Link[] {self, all, childs, user, logout};
			case "GET_CHILDFOOD":
				return new Link[] {self, newFood, parent, user, logout};
			default:
				return new Link[] {};
		}		
	}
	private URI buildUserUri(UriInfo uriInfo) {
		String currentUri = uriInfo.getAbsolutePath().toString();
		
		do {
			currentUri = currentUri.substring(0,currentUri.lastIndexOf("/"));
		} while (!currentUri.matches(".+\\/users\\/[0-9]+"));
		
		try {
			URI userUri = new URI(currentUri);
			return userUri;		
		} 
		catch (URISyntaxException ex) {
			throw new InternalServerErrorException("Could not build URI to user resource");
		}
		
		
	}
}
