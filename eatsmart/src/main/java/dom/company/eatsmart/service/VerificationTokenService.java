package dom.company.eatsmart.service;

import javax.persistence.EntityManager;

import dom.company.eatsmart.model.TokenType;
import dom.company.eatsmart.model.User;
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
}
