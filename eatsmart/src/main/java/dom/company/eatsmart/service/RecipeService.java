package dom.company.eatsmart.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.model.User;

public class RecipeService {
	/*
	UserService userService = new UserService();
	
	public List<Recipe> getRecipes(long userId) {
		User user = userService.getUser(userId);
		
		if (user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		return user.getRecipes();
	}
	
	public Recipe getRecipe(long userId, long recipeId) {
		List<Recipe> allRecipes = this.getRecipes(userId);				
		List<Recipe> filteredRecipes = allRecipes.stream().filter(r -> r.getId() == recipeId).collect(Collectors.toList());
		
		if (filteredRecipes.isEmpty()) {		
			throw new DataNotFoundException("Recipe with ID " + recipeId + " not found for user with ID " + userId);
		}
		return filteredRecipes.get(0);
	}
	
	public Recipe addRecipe(Recipe recipe, long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = userService.getUser(userId);
		User managedUser = entityManager.find(User.class, user.getId());
		
		entityManager.getTransaction().begin();
		managedUser.addRecipe(recipe);
		entityManager.persist(recipe);
		entityManager.getTransaction().commit();
	
		return recipe;
	}
	
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
