package dom.company.eatsmart.resources;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.service.FoodService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FoodResource {
	
	private long parentFoodId;	
	
	
	public FoodResource() {
		this.parentFoodId = 0;
	}
	
	public FoodResource(long parentFoodId) {
		this.parentFoodId = parentFoodId;
	}



	FoodService foodService = new FoodService();
	
	@GET
	public Response getFoods(@PathParam("userId") long userId, @Context UriInfo uriInfo) {
		if (this.parentFoodId == 0) {
			List<Food> foods = foodService.getFoods(userId);
			GenericEntity<List<Food>> entity = new GenericEntity<List<Food>>(foods) {};
			
			return Response.ok(entity)
					.links(getLinks(uriInfo, "GET"))
					.build();
		}
		else {
			List<Food> foods = foodService.getChildFoods(userId, parentFoodId);
			GenericEntity<List<Food>> entity = new GenericEntity<List<Food>>(foods) {};
			
			return Response.ok(entity)
					.links(getLinks(uriInfo, "GET"))
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
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,PUT,POST").build();
		Link findByName = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("qName=").build()).rel("findByName").param("verb", "GET").build();
		Link sortByNameAsc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=name").build()).rel("sortByNameAsc").param("verb", "GET").build();
		Link sortByNameDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-name").build()).rel("sortByNameDesc").param("verb", "GET").build();
		Link sortByRatingDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-rating").build()).rel("sortByRatingDesc").param("verb", "GET").build();
		Link sortBySmartRankingDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-SmartRanking").build()).rel("sortBySmartRankingDesc").param("verb", "GET").build();
		
		Link newfood = Link.fromUri(uriInfo.getAbsolutePath()).rel("new").param("verb", "POST").build();
		Link user = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("user").param("verb", "GET").build();
		
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET":
				return new Link[] {self, findByName, sortByNameAsc, sortByNameDesc, sortByRatingDesc, sortBySmartRankingDesc, newfood, user, logout};
			case "POST":
				return new Link[] {self, user, logout};
			default:
				return new Link[] {};
		}
	}
}
