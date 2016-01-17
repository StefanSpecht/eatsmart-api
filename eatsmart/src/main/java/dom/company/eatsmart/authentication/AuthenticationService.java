package dom.company.eatsmart.authentication;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.CustomIOException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.exception.UnauthorizedException;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.service.UserService;

public class AuthenticationService {
	
	private String encodedCredentials;
	private Map<String,String> decodedCredentials;
	private User authenticatedUser;
	private Boolean authenticated;
	
	private static final String USER_PATH_PATTERN = "users\\/(\\d+)($|\\/.*)";
	
	public AuthenticationService() {
		authenticatedUser = null;
		authenticated = false;
	}
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}

	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}

	public Map<String,String> decode (String authCredentials) throws NoSuchElementException{
				
		final String encodedUserPassword = authCredentials.replaceFirst("Basic"	+ " ", "");
		String usernameAndPassword = null;
		byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
		try {
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		}
		catch (IOException ex) {
			throw new CustomIOException("Error while decoding username and password");
		}
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		
		Map<String,String> decodedAuthCredentials = new HashMap<String,String>();
		
		decodedAuthCredentials.put("username", username);
		decodedAuthCredentials.put("password", password);
		return decodedAuthCredentials;		
	}
	
	
	public void authenticate(String authCredentials) {

		if (authCredentials == null)
			throw new UnauthorizedException("Authentication failed. Please provide username and password");
		
		try {
			Map<String,String> decodedAuthCredentials = this.decode(authCredentials);
			
			if (decodedAuthCredentials == null)
				throw new UnauthorizedException("Authentication failed. Please provide username and password");
			
			String username = decodedAuthCredentials.get("username");
			String password = decodedAuthCredentials.get("password");
			UserService userService = new UserService();
			//Try to find user
			User user = userService.getUser(username);
		
			//Check password
			if (user.getUsername().equals(username) && user.getPassword().equals(password) ) {
				this.authenticatedUser = user;
				this.authenticated = true;
			}
			else {
				this.authenticated = false;
				throw new UnauthorizedException("Authentication failed. Wrong username or password");
			}
		}
		catch (NoResultException ex) {
			throw new UnauthorizedException("Authentication failed. Wrong username or password");
		}
		catch (NoSuchElementException ex) {
			throw new UnauthorizedException("Authentication failed. Username and password must not be empty");
		}
	}
	
	public boolean isAuthorized(User user, UriInfo uriInfo) {
		
		//Strategy: Deny any and allow only if one of the following rules apply
		Boolean isAuthorized = false;
		
		String requestedPath = uriInfo.getPath();
		
		//Rule 100: Allow users to access their private data
		if (requestedPath.matches(USER_PATH_PATTERN)) {
			Pattern pattern = Pattern.compile(USER_PATH_PATTERN);
			Matcher matcher = pattern.matcher(requestedPath);
			if (matcher.find()) {
				long requestedUserId = Long.parseLong(matcher.group(1));
			
				if (requestedUserId == user.getId())
					isAuthorized = true;		
			}
		}
		
		//some other rules
		
		return isAuthorized;
	}
}