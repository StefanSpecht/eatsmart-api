package dom.company.eatsmart.resources;

import java.net.URI;
import java.sql.Date;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.model.MenuSchedule;
import dom.company.eatsmart.service.MenuScheduleService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuScheduleResource {
	MenuScheduleService menuScheduleService = new MenuScheduleService();
	
	@GET
	public Response getMenuSchedules(@PathParam("userId") long userId, @Context UriInfo uriInfo, @QueryParam("qStartDate") Date qStartDate, @QueryParam("qEndDate") Date qEndDate) {
		
		List<MenuSchedule> menuSchedules = menuScheduleService.getMenuSchedules(userId, qStartDate, qEndDate);
		GenericEntity<List<MenuSchedule>> entity = new GenericEntity<List<MenuSchedule>>(menuSchedules) {};
		return Response.ok(entity)
				.links(getLinks(uriInfo, "GET_ALL"))
				.build();
	}
	
	@GET
	@Path("/{menuScheduleId}")
	public Response getMenuSchedule(@PathParam("userId") long userId, @PathParam("menuScheduleId") long menuScheduleId, @Context UriInfo uriInfo) {
		MenuSchedule menuSchedule = menuScheduleService.getMenuSchedule(userId, menuScheduleId);
		return Response.ok(menuSchedule)
				.links(getLinks(uriInfo, "GET"))
				.build();
	}
	
	@PUT
	@Path("/{menuScheduleId}")	
	public Response updateMenuSchedule(@PathParam("userId") long userId, @PathParam("menuScheduleId") long menuScheduleId, @Valid MenuSchedule menuSchedule, @Context UriInfo uriInfo) {
		menuSchedule.setId(menuScheduleId);
		menuScheduleService.updateMenuSchedule(userId, menuSchedule);
		return Response.noContent()
				.links(getLinks(uriInfo, "PUT"))
				.build();		
	}
		
	@POST
	public Response addMenuSchedule(@Valid MenuSchedule menuSchedule, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		MenuSchedule newMenuSchedule = menuScheduleService.addMenuSchedule(menuSchedule, userId);
		String newId = String.valueOf(newMenuSchedule.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.links(getLinks(uriInfo, "POST"))
				.entity(newMenuSchedule)
				.build();
	}
	
	@DELETE
	@Path("/{menuScheduleId}")	
	public Response deleteMenuSchedule(@PathParam("menuScheduleId") long menuScheduleId, @PathParam("userId") long userId, @Context UriInfo uriInfo) {
		menuScheduleService.deleteMenuSchedule(userId, menuScheduleId);
		return Response.noContent()
				.links(getLinks(uriInfo, "DELETE"))
				.build();
	}
	
	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self_all = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,POST").build();
		Link filterByStartDate = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("qStartDate=").build()).rel("filterByStartDate").param("verb", "GET").build();
		Link filterByEndDate = Link.fromUri(uriInfo.getAbsolutePathBuilder().replaceQuery("qEndDate=").build()).rel("filterByEndDate").param("verb", "GET").build();
		Link newMenuSchedule = Link.fromUri(uriInfo.getAbsolutePath()).rel("new").param("verb", "POST").build();
		Link user_all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("user").param("verb", "GET").build();
		
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "GET,PUT,DELETE").build();
		Link all = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("..").build()).rel("all").param("verb", "GET").build();
		Link user = Link.fromUri(uriInfo.getAbsolutePathBuilder().path("../..").build()).rel("user").param("verb", "GET").build();
		
		Link logout = Link.fromUri(uriInfo.getBaseUri()).rel("logout").param("verb", "GET").build();
		
		switch (method) {
			case "GET_ALL":
				return new Link[] {self_all, filterByStartDate, filterByEndDate, newMenuSchedule, user_all, logout};
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
