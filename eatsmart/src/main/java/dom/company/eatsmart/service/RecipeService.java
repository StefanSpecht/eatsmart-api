package dom.company.eatsmart.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.model.RecipeBook;
import dom.company.eatsmart.model.User;

public class RecipeService {
	
	UserService userService = new UserService();
	
	public List<Recipe> getRecipes(long userId, String qName, String sort) {
		User user = userService.getUser(userId);
		
		if (user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		List<Recipe> recipes = user.getRecipeBook().getRecipes();
		
		//filter
		if (qName != null) {
			recipes = recipes
					.stream()
					.filter(recipe -> recipe.getName().matches(qName))
					.collect(Collectors.toList());
		}
		
		//sort
		if (sort != null) {
			switch (sort) {
				case "name":
					Collections.sort(recipes, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
					break;
				case "-name":
					Collections.sort(recipes, (a, b) -> b.getName().compareToIgnoreCase(a.getName()));
					break;
				case "-rating":
					Collections.sort(recipes, (a, b) -> b.getRating() < a.getRating() ? -1 : b.getRating() == a.getRating() ? 0 : 1);
					break;
				default:
					Collections.sort(recipes, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
			}
		}
		else {
			Collections.sort(recipes, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
		}
		return recipes;				
	}
	
	public Recipe getRecipe(long userId, long recipeId) {
		List<Recipe> allRecipes = this.getRecipes(userId, null, null);				
		List<Recipe> filteredRecipes = allRecipes.stream().filter(r -> r.getId() == recipeId).collect(Collectors.toList());
		
		if (filteredRecipes.isEmpty()) {		
			throw new DataNotFoundException("Recipe with ID " + recipeId + " not found for user with ID " + userId);
		}
		return filteredRecipes.get(0);
	}
	
	public Recipe addRecipe(Recipe recipe, long userId) {
			
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = userService.getUser(userId);
		RecipeBook managedRecipeBook = entityManager.find(RecipeBook.class, user.getRecipeBook().getId());
		
		try {
			entityManager.getTransaction().begin();
			managedRecipeBook.addRecipe(recipe);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("All foods must be added to food catalogue first");
		}
		return recipe;
	}
	
	public void updateRecipe(long userId, Recipe updatedRecipe) {
		
		Recipe currentRecipe = this.getRecipe(userId, updatedRecipe.getId());
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Recipe managedCurrentRecipe = entityManager.find(Recipe.class, currentRecipe.getId());
		
		try {
			entityManager.getTransaction().begin();
			managedCurrentRecipe.updateRecipe(updatedRecipe);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("All foods must be added to food catalogue first");
		}
	}
	
	public void deleteRecipe(long userId, long recipeId) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		Recipe recipe = this.getRecipe(userId, recipeId);
		User user = userService.getUser(userId);
		
		Recipe managedRecipe = entityManager.find(Recipe.class, recipe.getId());		
		User managedUser = entityManager.find(User.class, user.getId());
		RecipeBook managedRecipeBook = managedUser.getRecipeBook();
		
		entityManager.getTransaction().begin();
		managedRecipeBook.removeRecipe(managedRecipe);
		entityManager.remove(managedRecipe);
		entityManager.getTransaction().commit();
	}
	
}
