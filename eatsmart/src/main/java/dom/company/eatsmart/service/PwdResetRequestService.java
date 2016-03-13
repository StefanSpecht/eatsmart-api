package dom.company.eatsmart.service;

import java.util.UUID;

import javax.persistence.NoResultException;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.PwdResetRequest;
import dom.company.eatsmart.model.TokenType;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.model.UserRole;

public class PwdResetRequestService {

	UserService userService = new UserService();
	VerificationTokenService tokenService = new VerificationTokenService();
	MailService mailService = new MailService();
	
	public void resetPassword(PwdResetRequest pwdResetRequest, UriInfo uriInfo) {
		
		try {
			User user = userService.getUser(pwdResetRequest.getUsername());
			
			//Verify posted data
			if (!pwdResetRequest.getEmail().equals(user.getEmail())) {
				throw new DataNotFoundException("Username and Mail combination not found");
			}
			
			//check that user has no valid registration verification token
			if (user.getUserRoles().contains(UserRole.DISABLED)) {
				throw new DataConflictException("Please complete registration process first");
			}
			
			String token = UUID.randomUUID().toString();
			tokenService.createVerificationToken(user, token, TokenType.PASSWORD_RESET);
			
			//send mail
			mailService.sendPwdResetMail(user, token, uriInfo);
			
		}
		catch (NoResultException ex) {
			throw new DataNotFoundException("Username and Mail combination not found");
		}
	}
}
