package dom.company.eatsmart.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.exception.ResourceAlreadyExistsException;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.User;

public class FoodService {
	
	UserService userService = new UserService();
	
	public List<Food> getFoods(long userId) {
		//catch
		User adminUser = userService.getUser("shared");
		
		if (adminUser == null) {
			throw new DataNotFoundException("Shared user not found");
		}
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, userId);
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Food> criteriaQuery = criteriaBuilder.createQuery(Food.class);
		
		Root<Food> root=criteriaQuery.from(Food.class);
		Predicate whereAdminUser=criteriaBuilder.equal(root.<Food>get("owner"),adminUser);
		Predicate whereUser=criteriaBuilder.equal(root.<Food>get("owner"),user);
		Predicate whereUserAndAdminUser=criteriaBuilder.or(whereUser,whereAdminUser);
		CriteriaQuery<Food> whereQuery = criteriaQuery.select(root).where(whereUserAndAdminUser);
		TypedQuery<Food> typedQuery = entityManager.createQuery(whereQuery);
		
		return typedQuery.getResultList();
		
	}
	
	public Food getFood(long userId, long foodId) {
		User adminUser = userService.getUser("shared");	
		
		if (adminUser == null) {
			throw new DataNotFoundException("Shared user not found");
		}
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Food food = entityManager.find(Food.class, foodId);
		
		try {
			if (food.getOwner().getId() != userId && food.getOwner().getId() != adminUser.getId()) {
				throw new DataNotFoundException("Food with ID " + foodId + " not found for user with ID " + userId);
			}
		}
		catch (NullPointerException ex) {
			throw new DataNotFoundException("Food with ID " + foodId + " not found for user with ID " + userId);
		}
		
		
		return food;
	}
	
	public Food getChildFood(long userId, long foodId, long parentFoodId) {
		
		Food food = this.getFood(userId, foodId);
		
		try {
			if (food.getParentFood().getId() != parentFoodId) {
				throw new DataNotFoundException("Child food with ID " + foodId + " not found for parent food with ID " + parentFoodId);
			}
		}
		catch (NullPointerException ex) {
			throw new DataNotFoundException("Child food with ID " + foodId + " not found for parent food with ID " + parentFoodId);
		}
		
		return food;
	}
	
	public List<Food> getChildFoods(long userId, long parentFoodId) {
		Food parentFood = this.getFood(userId, parentFoodId);
		return parentFood.getChildFoods();		
	}
	
	public Food addFood(Food food, long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		User owner = userService.getUser(userId);
		food.setOwner(owner);
		
		if (!this.isNameAvailable(food.getName(), userId)) {
			throw new ResourceAlreadyExistsException("Foodname already in use");
		}
		
		if (food.getChildFoods() != null) {
			food.getChildFoods().forEach((Food childFood) -> {childFood.setParentFood(food);childFood.setOwner(owner);});
		}
		if (food.getParentFood() != null) {
			food.getParentFood().addChildFood(food);
		}
		
		entityManager.getTransaction().begin();
		entityManager.persist(food);
		entityManager.getTransaction().commit();
	
		return food;
	}
	
public Food addFood(Food food, long userId, long parentFoodId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Food parentFood = this.getFood(userId, parentFoodId);
		User owner = userService.getUser(userId);
		
		User managedOwner = entityManager.find(User.class, owner.getId());		
		Food managedParentFood = entityManager.find(Food.class, parentFood.getId());
		
		//check if food name is available
		if (!this.isNameAvailable(food.getName(), userId)) {
			throw new ResourceAlreadyExistsException("Foodname already in use");
		}
		
		food.setOwner(managedOwner);
		food.setParentFood(managedParentFood);
				
		entityManager.getTransaction().begin();
		entityManager.persist(food);
		entityManager.getTransaction().commit();
		
		return food;
	}

	public void validateFood(Food food) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Food managedFood = entityManager.find(Food.class, food.getId());
		
		try {
			if (!food.getName().equals(managedFood.getName()) || food.getWeightPerUnit() != managedFood.getWeightPerUnit()) {
				throw new DataConflictException("Food not found. Must be added to food catalogue first.");
			}
		}
		catch (NullPointerException ex) {
			throw new DataConflictException("Food not found. Must be added to food catalogue first.");
		}
	}
	
	public boolean isNameAvailable(String name, long userId){
		EntityManager entityManager = JpaUtil.getEntityManager();
		User owner = entityManager.find(User.class, userId);
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Food> criteriaQuery = criteriaBuilder.createQuery(Food.class);
		
		Root<Food> root=criteriaQuery.from(Food.class);
		List<Predicate> wherePredicates = new ArrayList<Predicate>();
		wherePredicates.add(criteriaBuilder.equal(root.<Food>get("owner"),owner));
		wherePredicates.add(criteriaBuilder.equal(root.<Food>get("name"),name));

		CriteriaQuery<Food> whereQuery = criteriaQuery.select(root).where(wherePredicates.toArray(new Predicate[]{}));
		TypedQuery<Food> typedQuery = entityManager.createQuery(whereQuery);
		
		if (typedQuery.getResultList().isEmpty()) {
			return true;
		}
		return false;
	}
}
