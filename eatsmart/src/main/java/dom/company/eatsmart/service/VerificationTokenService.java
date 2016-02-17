package dom.company.eatsmart.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.exception.VerificationNotSuccessfulException;
import dom.company.eatsmart.model.TokenType;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.model.UserRole;
import dom.company.eatsmart.model.VerificationToken;

public class VerificationTokenService {
	
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
				throw new VerificationNotSuccessfulException(uriInfo);
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
			throw new VerificationNotSuccessfulException(uriInfo);
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
}
