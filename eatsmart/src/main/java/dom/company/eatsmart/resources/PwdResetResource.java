package dom.company.eatsmart.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.model.PwdResetRequest;
import dom.company.eatsmart.service.PwdResetRequestService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PwdResetResource {
	
	PwdResetRequestService pwdResetRequestService = new PwdResetRequestService();
	
	@POST
	public Response resetPassword(@Valid PwdResetRequest pwdResetRequest, @Context UriInfo uriInfo) {
		pwdResetRequestService.resetPassword(pwdResetRequest, uriInfo);
		
		return Response.accepted()
				.links(getLinks(uriInfo,"POST"))
				.build();
	}
	
	private Link[] getLinks(UriInfo uriInfo, String method) {
		Link self = Link.fromUri(uriInfo.getAbsolutePath()).rel("self").param("verb", "POST").build();
		Link next = Link.fromUri(uriInfo.getBaseUri()).rel("next").param("verb", "GET").build();
		switch (method) {
			case "POST":
				return new Link[] {self, next};
			default:
				return new Link[] {};
		}
	}
}
