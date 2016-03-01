package dom.company.eatsmart.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.exception.InternalServerErrorException;
import dom.company.eatsmart.exception.VerificationNotSuccessfulException;
import dom.company.eatsmart.model.TokenType;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.model.UserRole;
import dom.company.eatsmart.model.VerificationToken;

public class VerificationTokenService {
	
	private static final String PWDGEN_CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int PWDGEN_STRING_LENGTH = 10;
	
	
	public void createVerificationToken (User user, String token, TokenType tokenType) {
		
		VerificationToken verificationToken = new VerificationToken(user, token, tokenType);
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(verificationToken);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	public VerificationToken getVerificationToken (String token) throws NoResultException{		
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VerificationToken> criteriaQuery = criteriaBuilder.createQuery(VerificationToken.class);
		
		Root<VerificationToken> root=criteriaQuery.from(VerificationToken.class);
		Predicate wheretoken=criteriaBuilder.equal(root.<VerificationToken>get("token"),token);
		CriteriaQuery<VerificationToken> whereQuery = criteriaQuery.select(root).where(wheretoken);
		TypedQuery<VerificationToken> typedQuery = entityManager.createQuery(whereQuery);
		
		return typedQuery.getSingleResult();		
	}
	
	public void verifyRegistrationToken (String token, UriInfo uriInfo) {
		try {
			VerificationToken verificationToken = this.getVerificationToken(token);
			if (verificationToken.getTokenType() != TokenType.REGISTRATION || verificationToken.isExpired()) {
				throw new VerificationNotSuccessfulException(uriInfo, "registration");
			}
			
			//enable user
			EntityManager entityManager = JpaUtil.getEntityManager();
			User managedUser = entityManager.find(User.class, verificationToken.getUser().getId());
			entityManager.getTransaction().begin();
			managedUser.removeUserRole(UserRole.DISABLED);
			entityManager.getTransaction().commit();
			
			//delete token
			this.deleteVerificationToken(verificationToken.getId());
			
		}
		catch(NoResultException ex) {
			throw new VerificationNotSuccessfulException(uriInfo, "registration");
		}
	}
	
	public void verifyPwdResetToken (String token, UriInfo uriInfo) {
		
		String hashAlgorithm = "MD5";
		String byteEncoding = "UTF-8";
		try {
			VerificationToken verificationToken = this.getVerificationToken(token);
			if (verificationToken.getTokenType() != TokenType.PASSWORD_RESET || verificationToken.isExpired()) {
				throw new VerificationNotSuccessfulException(uriInfo, "pwdResetRequest");
			}
			
			//generate new Password
			String newPassword = this.generateRandomString();
			
			// hash to MD5
			MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
			//byte[] newPasswordHashBytes = messageDigest.digest(newPassword.getBytes(byteEncoding));
			messageDigest.update(newPassword.getBytes(byteEncoding),0,newPassword.length());
			String newPasswordHashString = new BigInteger(1, messageDigest.digest()).toString(16);
			
			//reset password
			EntityManager entityManager = JpaUtil.getEntityManager();
			User managedUser = entityManager.find(User.class, verificationToken.getUser().getId());
			entityManager.getTransaction().begin();
			managedUser.setPassword(newPasswordHashString);
			entityManager.getTransaction().commit();			
			
			//send password by mail
			MailService mailService = new MailService();
			mailService.sendNewPwdMail(managedUser, newPassword);
			//delete token
			this.deleteVerificationToken(verificationToken.getId());
			
		}
		catch(NoResultException ex) {
			throw new VerificationNotSuccessfulException(uriInfo, "pwdResetRequest");
		}
		catch (NoSuchAlgorithmException ex) {
			throw new InternalServerErrorException("Algorithm '" + hashAlgorithm + "' not found");
		}
		catch (UnsupportedEncodingException  ex) {
			throw new InternalServerErrorException("Encoding '" + byteEncoding + "' not supported");
		}
	}
	
	public void deleteVerificationToken(long id) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		VerificationToken verificationToken = entityManager.find(VerificationToken.class, id);
		if (verificationToken == null) {
			throw new DataNotFoundException("VerificationToken with ID " + id + " not found");
		}
		entityManager.getTransaction().begin();
		entityManager.remove(verificationToken);
		entityManager.getTransaction().commit();
	}
	
	 private String generateRandomString(){
		 
		 StringBuffer randStr = new StringBuffer();
	     
		 for(int i=0; i<PWDGEN_STRING_LENGTH; i++){
			 int number = getRandomNumber();
	            char ch = PWDGEN_CHAR_LIST.charAt(number);
	            randStr.append(ch);
	        }
	        return randStr.toString();
	 }
	 
	 private int getRandomNumber() {
		 int randomInt = 0;
	     Random randomGenerator = new Random();
	     randomInt = randomGenerator.nextInt(PWDGEN_CHAR_LIST.length());
	     
	     if (randomInt - 1 == -1) {
	    	 return randomInt;
	        }
	     return randomInt - 1;
	 }
}
