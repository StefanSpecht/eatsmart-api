package dom.company.eatsmart.resources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.service.VerificationTokenService;

@Path("/verification")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VerificationResource {
	VerificationTokenService verificationTokenService = new VerificationTokenService();

	@Path("registration/{token}")
	@GET
	public Response verifyRegistrationToken(@PathParam("token") String token, @Context UriInfo uriInfo) {
		verificationTokenService.verifyRegistrationToken(token, uriInfo);
		
		URI badResponse = uriInfo.getBaseUriBuilder().path("../../html/registration/success.html").build();
		return Response.seeOther(badResponse)
				.build();
	}
}
