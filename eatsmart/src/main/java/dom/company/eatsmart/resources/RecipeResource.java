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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.service.RecipeService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecipeResource {
	
	RecipeService recipeService = new RecipeService();
	
	@GET
	public Response getRecipes(@PathParam("userId") long userId, @Context UriInfo uriInfo, @QueryParam("qName") String qName, @QueryParam("sort") String sort) {
		
		List<Recipe> recipes = recipeService.getRecipes(userId, qName, sort);
		GenericEntity<List<Recipe>> entity = new GenericEntity<List<Recipe>>(recipes) {};
		return Response.ok(entity)
				.links(getLinks(uriInfo, "GET_ALL"))
				.build();
	}
	
	@GET
	@Path("/{recipeId}")
	public Response getRecipe(@PathParam("userId") long userId, @PathParam("recipeId") long recipeId, @Context UriInfo uriInfo, @QueryParam("servings") int servings) {
		Recipe recipe = recipeService.getRecipe(userId, recipeId, servings);
		return Response.ok(recipe)
				.links(getLinks(uriInfo, "GET"))
				.build();
	}
	
	@PUT
	@Path("/{recipeId}")	
	public Response updateRecipe(@PathParam("userId") long userId, @PathParam("recipeId") long recipeId, @Valid Recipe recipe, @Context UriInfo uriInfo) {
		recipe.setId(recipeId);
		recipeService.updateRecipe(userId, recipe);
		return Response.noContent()
				.links(getLinks(uriInfo, "PUT"))
				.build();		
	}
	
	
	@POST
	public Response addRecipe(@Valid Recipe recipe, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		Recipe newRecipe = recipeService.addRecipe(recipe, userId);
		String newId = String.valueOf(newRecipe.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.links(getLinks(uriInfo, "POST"))
				.entity(newRecipe)
				.build();
	}
	
	@DELETE
	@Path("/{recipeId}")	
	public Response deleteRecipe(@PathParam("recipeId") long recipeId, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		recipeService.deleteRecipe(userId, recipeId);
		return Response.noContent()
				.links(getLinks(uriInfo, "DELETE"))
				.build();
	}
	
	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self_all = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,POST").build();
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,PUT,DELETE").build();
		Link all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("all").param("verb", "GET").build();
		Link findByName = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("qName=").build()).rel("findByName").param("verb", "GET").build();
		Link sortByNameAsc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=name").build()).rel("sortByNameAsc").param("verb", "GET").build();
		Link sortByNameDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-name").build()).rel("sortByNameDesc").param("verb", "GET").build();
		Link sortByRatingDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-rating").build()).rel("sortByRatingDesc").param("verb", "GET").build();
		Link sortBySmartRankingDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-SmartRanking").build()).rel("sortBySmartRankingDesc").param("verb", "GET").build();
		
		Link newRecipe = Link.fromUri(uriInfo.getAbsolutePath()).rel("new").param("verb", "POST").build();
		Link user_all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("user").param("verb", "GET").build();
		Link user = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("../..").build()).rel("user").param("verb", "GET").build();
		
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET_ALL":
				return new Link[] {self_all, findByName, sortByNameAsc, sortByNameDesc, sortByRatingDesc, sortBySmartRankingDesc, newRecipe, user_all, logout};
			case "GET":
				return new Link[] {self, all, user, logout};
			case "POST":
				return new Link[] {self_all, user_all, logout};
			case "PUT":
				return new Link[] {self, all, user, logout};
			case "DELETE":
				return new Link[] {self, all, user, logout};
			default:
				return new Link[] {};
		}
	}
}
