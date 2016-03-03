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

import dom.company.eatsmart.model.Stock;
import dom.company.eatsmart.service.StockService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {
	
	StockService stockService = new StockService();
	
	@GET
	public Response getStocks(@PathParam("userId") long userId, @Context UriInfo uriInfo, @QueryParam("qName") String qName, @QueryParam("sort") String sort) {
		
		List<Stock> stocks = stockService.getStocks(userId, qName, sort);
		GenericEntity<List<Stock>> entity = new GenericEntity<List<Stock>>(stocks) {};
		return Response.ok(entity)
				.links(getLinks(uriInfo, "GET_ALL"))
				.build();
	}
	
	@GET
	@Path("/{stockId}")
	public Response getStock(@PathParam("userId") long userId, @PathParam("stockId") long stockId, @Context UriInfo uriInfo) {
		Stock stock = stockService.getStock(userId, stockId);
		return Response.ok(stock)
				.links(getLinks(uriInfo, "GET"))
				.build();
	}
	
	@PUT
	@Path("/{stockId}")	
	public Response updateStock(@PathParam("userId") long userId, @PathParam("stockId") long stockId, @Valid Stock stock, @Context UriInfo uriInfo) {
		stock.setId(stockId);
		stockService.updateStock(userId, stock);
		return Response.noContent()
				.links(getLinks(uriInfo, "PUT"))
				.build();		
	}
	
	
	@POST
	public Response addStock(@Valid Stock stock, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		Stock newStock = stockService.addStock(stock, userId);
		String newId = String.valueOf(newStock.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.links(getLinks(uriInfo, "POST"))
				.entity(newStock)
				.build();
	}
	
	@DELETE
	@Path("/{stockId}")	
	public Response deleteStock(@PathParam("stockId") long stockId, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		stockService.deleteStock(userId, stockId);
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
		Link sortByQuantityAsc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=quantity").build()).rel("sortByQuantityAsc").param("verb", "GET").build();
		Link sortByQuantityDesc = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("sort=-quantity").build()).rel("sortByQuantityDesc").param("verb", "GET").build();
		
		Link newStock = Link.fromUri(uriInfo.getAbsolutePath()).rel("new").param("verb", "POST").build();
		Link user_all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("user").param("verb", "GET").build();
		Link user = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("../..").build()).rel("user").param("verb", "GET").build();
		
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET_ALL":
				return new Link[] {self_all, findByName, sortByNameAsc, sortByNameDesc, sortByQuantityAsc, sortByQuantityDesc, newStock, user_all, logout};
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
