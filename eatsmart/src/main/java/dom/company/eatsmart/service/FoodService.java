package dom.company.eatsmart.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.model.RecipeBook;
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
		//catch
		//User adminUser = userService.getUser("shared");
		
		Food parentFood = this.getFood(userId, parentFoodId);
		
		return parentFood.getChildFoods();		
	}
	
	
	public Food addFood(Food food, long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		User owner = userService.getUser(userId);
		food.setOwner(owner);
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
		
		//food.setParentFood(managedParentFood);
		food.setOwner(managedOwner);
		food.setParentFood(managedParentFood);
		
		/*
		if (food.getChildFoods() != null) {
			food.getChildFoods().forEach((Food childFood) -> {childFood.setParentFood(food);childFood.setOwner(owner);});
		}
		if (food.getParentFood() != null) {
			food.getParentFood().addChildFood(food);
		}
		*/
		
		entityManager.getTransaction().begin();
		entityManager.persist(food);
		entityManager.getTransaction().commit();
		
		return food;
	}
	/*
	public Recipe updateRecipe(long userId, Recipe updatedRecipe) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		Recipe recipe = this.getRecipe(userId, updatedRecipe.getId());
		Recipe managedRecipe = JpaUtil.getEntityManager().find(Recipe.class, recipe.getId());
		
		entityManager.getTransaction().begin();
		managedRecipe.updateRecipe(updatedRecipe);
		entityManager.getTransaction().commit();
			
		return recipe;		
	}
	
	public void deleteRecipe(long userId, long recipeId) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		Recipe recipe = this.getRecipe(userId, recipeId);
		User user = userService.getUser(userId);
		
		Recipe managedRecipe = entityManager.find(Recipe.class, recipe.getId());		
		User managedUser = entityManager.find(User.class, user.getId());
		
		entityManager.getTransaction().begin();
		managedUser.removeRecipe(managedRecipe);
		entityManager.remove(managedRecipe);
		entityManager.getTransaction().commit();
	}
	*/
}
