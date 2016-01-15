package dom.company.eatsmart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import dom.company.eatsmart.exception.DataNotFoundException;
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
		User user =  JpaUtil.getEntityManager().find(User.class, id);
		if(user == null) {
			throw new DataNotFoundException("User with ID " + id + " not found");
		}
		return user;
	}
	
	public User addUser(User user) {	
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		entityManager.close();
				
		return user;
	}
	
	public User updateUser(User updatedUser) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = this.getUser(updatedUser.getId());
		User managedUser = JpaUtil.getEntityManager().find(User.class, user.getId());
		
		entityManager.getTransaction().begin();
		managedUser.updateUser(updatedUser);
		entityManager.getTransaction().commit();
		return managedUser;		
	}
	
	public void deleteUser(long id) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, id);
		if (user == null) {
			throw new DataNotFoundException("User with ID " + id + " not found");
		}
		entityManager.getTransaction().begin();
		entityManager.remove(user);
		entityManager.getTransaction().commit();
	}
	
}
