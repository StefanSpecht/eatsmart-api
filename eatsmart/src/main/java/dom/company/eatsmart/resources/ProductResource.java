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

import dom.company.eatsmart.model.Product;
import dom.company.eatsmart.service.ProductService;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	
	ProductService productService = new ProductService();
	
	@GET
	public Response getProducts(@PathParam("userId") long userId, @Context UriInfo uriInfo, @QueryParam("qEan") String qEan) {
		
		List<Product> products = productService.getProducts(userId, qEan);
		GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
		return Response.ok(entity)
				.links(getLinks(uriInfo, "GET_ALL"))
				.build();
	}
	
	@GET
	@Path("/{productId}")
	public Response getProduct(@PathParam("userId") long userId, @PathParam("productId") long productId, @Context UriInfo uriInfo) {
		Product product = productService.getProduct(userId, productId);
		return Response.ok(product)
				.links(getLinks(uriInfo, "GET"))
				.build();
	}
	
	@PUT
	@Path("/{productId}")	
	public Response updateProduct(@PathParam("userId") long userId, @PathParam("productId") long productId, @Valid Product product, @Context UriInfo uriInfo) {
		product.setId(productId);
		productService.updateProduct(userId, product);
		return Response.noContent()
				.links(getLinks(uriInfo, "PUT"))
				.build();		
	}
	
	
	@POST
	public Response addProduct(@Valid Product product, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		Product newProduct = productService.addProduct(product, userId);
		String newId = String.valueOf(newProduct.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.links(getLinks(uriInfo, "POST"))
				.entity(newProduct)
				.build();
	}
	
	@DELETE
	@Path("/{productId}")	
	public Response deleteProduct(@PathParam("productId") long productId, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		productService.deleteProduct(userId, productId);
		return Response.noContent()
				.links(getLinks(uriInfo, "DELETE"))
				.build();
	}
	
	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self_all = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,POST").build();
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,PUT,DELETE").build();
		Link all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("all").param("verb", "GET").build();
		Link findByEan = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("qEan=").build()).rel("findByEan").param("verb", "GET").build();
		
		Link newProduct = Link.fromUri(uriInfo.getAbsolutePath()).rel("new").param("verb", "POST").build();
		Link user_all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("user").param("verb", "GET").build();
		Link user = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("../..").build()).rel("user").param("verb", "GET").build();
		
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET_ALL":
				return new Link[] {self_all, findByEan, newProduct, user_all, logout};
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
