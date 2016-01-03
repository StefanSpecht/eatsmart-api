package dom.company.eatsmart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import dom.company.eatsmart.model.User;

public class UserService {
	
	public List<User> getUsers() {
		EntityManager entityManager = JpaUtil.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	public User getUser(long id) {
		return JpaUtil.getEntityManager().find(User.class, id);
	}
	
	public User addUser(User user) {	
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		entityManager.close();
				
		return user;
	}
	
	public User updateUser(User user) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		User currentUser = entityManager.find(User.class, user.getId());
		
		if (currentUser != null) {
			entityManager.getTransaction().begin();
			currentUser.updateUser(user);
			entityManager.getTransaction().commit();
			return currentUser;
		}
		else {
			return null;
		}
	}
	
	public void removeUser(long id) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, id);
		if (user != null) {
			entityManager.getTransaction().begin();
			entityManager.remove(user);
			entityManager.getTransaction().commit();
		}
	}
	
}
