package dom.company.eatsmart.service;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.exception.ResourceAlreadyExistsException;
import dom.company.eatsmart.model.TokenType;
import dom.company.eatsmart.model.User;
import dom.company.eatsmart.model.UserRole;

public class UserService {
	
	public User registerUser(User user, UriInfo uriInfo) {
		
		VerificationTokenService tokenService = new VerificationTokenService();
		MailService mailService = new MailService();
		
		//Set standard user settings and disable user
		user.setUserRoles(new ArrayList<UserRole>());
		user.addUserRole(UserRole.USER);
		user.addUserRole(UserRole.DISABLED);
		
		//Check if username is available
		if (!this.isUsernameAvailable(user.getUsername())) {
			throw new ResourceAlreadyExistsException("Username already in use");
		}
		
		//Check if email is available
		if (!this.isEmailAvailable(user.getEmail())) {
			throw new ResourceAlreadyExistsException("Email address already in use");
		}
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		entityManager.close();
		
		String token = UUID.randomUUID().toString();
		tokenService.createVerificationToken(user, token, TokenType.REGISTRATION);
		
		//send mail
		mailService.sendRegistrationMail(user, token, uriInfo);
				
		return user;
	}
	
	public User getUser(long id) {
		User user =  JpaUtil.getEntityManager().find(User.class, id);
		if(user == null) {
			throw new DataNotFoundException("User with ID " + id + " not found");
		}
		return user;
	}
	
	public User getUser(String username) throws NoResultException {
		EntityManager entityManager = JpaUtil.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		
		Root<User> root=criteriaQuery.from(User.class);
		Predicate whereuser=criteriaBuilder.equal(root.<User>get("username"),username);
		CriteriaQuery<User> whereQuery = criteriaQuery.select(root).where(whereuser);
		TypedQuery<User> typedQuery = entityManager.createQuery(whereQuery);
		
		return typedQuery.getSingleResult();
	}
	
	public User updateUser(User updatedUser) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		User managedUser = entityManager.find(User.class, updatedUser.getId());
		
		//Validate that username and email didn't change
		if (!updatedUser.getUsername().equals(managedUser.getUsername())) {
			throw new DataConflictException("username must not be changed");
		}
		if (!updatedUser.getEmail().equals(managedUser.getEmail())) {
			throw new DataConflictException("email must not be changed");
		}
		
		entityManager.getTransaction().begin();
		managedUser.updateUser(updatedUser);
		entityManager.getTransaction().commit();
		return managedUser;
	}

	public boolean isUsernameAvailable(String username){
		EntityManager entityManager = JpaUtil.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		
		Root<User> root=criteriaQuery.from(User.class);
		Predicate whereuser=criteriaBuilder.equal(root.<User>get("username"),username);
		CriteriaQuery<User> whereQuery = criteriaQuery.select(root).where(whereuser);
		TypedQuery<User> typedQuery = entityManager.createQuery(whereQuery);
		
		if (typedQuery.getResultList().isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailAvailable(String email){
		EntityManager entityManager = JpaUtil.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		
		Root<User> root=criteriaQuery.from(User.class);
		Predicate whereuser=criteriaBuilder.equal(root.<User>get("email"),email);
		CriteriaQuery<User> whereQuery = criteriaQuery.select(root).where(whereuser);
		TypedQuery<User> typedQuery = entityManager.createQuery(whereQuery);
		
		if (typedQuery.getResultList().isEmpty()) {
			return true;
		}
		return false;
	}
	
}
