package dom.company.eatsmart.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.model.User;

public class RecipeService {
	
	UserService userService = new UserService();
	
	public List<Recipe> getRecipes(long userId) {
		return userService.getUser(userId).getRecipes();
		
	}
	
	public Recipe getRecipe(long userId, long recipeId) {
		List<Recipe> allRecipes = userService.getUser(userId).getRecipes();
		List<Recipe> filteredRecipes = allRecipes.stream().filter(r -> r.getId() == recipeId).collect(Collectors.toList());
		
		if (!filteredRecipes.isEmpty()) {		
			Recipe recipe = filteredRecipes.get(0);
			return recipe;
		}
		return null;
	}
	
	public Recipe addRecipe(Recipe recipe, long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, userId);
		
		entityManager.getTransaction().begin();
		user.addRecipe(recipe);
		entityManager.persist(recipe);
		entityManager.getTransaction().commit();
	
		return recipe;
	}
	
	public Recipe updateRecipe(Recipe recipe) {
		EntityManager entityManager = JpaUtil.getEntityManager();

		if (recipe.getId() <= 0) {
			return null;
		}
		else {
			entityManager.getTransaction().begin();
			Recipe updatedRecipe = entityManager.merge(recipe);
			entityManager.getTransaction().commit();
			
			return updatedRecipe;
		}
	}
	
	public void removeRecipe(long recipeId, long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		/*
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
		Root<Recipe> root = criteriaQuery.from(Recipe.class);
		ParameterExpression<Long> recipeIdParam = criteriaBuilder.parameter(Long.class);
		ParameterExpression<Long> userIdParam = criteriaBuilder.parameter(Long.class);
				
		criteriaQuery.select(root);
		criteriaQuery.where(
				criteriaBuilder.equal(root.get("RECIPE_ID"), recipeIdParam),
				criteriaBuilder.equal(root.get("USER_ID"), userIdParam)
				);
		 TypedQuery<Recipe> typedQuery = entityManager.createQuery(criteriaQuery)
		 	.setParameter("recipeIdParam", recipeId)
		 	.setParameter("userIdParam", userId);
		 	Recipe recipe = typedQuery.getSingleResult();
		 */
		 
		 User user = entityManager.find(User.class, userId);
		 Recipe recipe = entityManager.find(Recipe.class, recipeId);
		 
		 if (recipe != null && user != null && recipe.getOwner() == user) {
			 entityManager.getTransaction().begin();
			 user.removeRecipe(recipe);
			 entityManager.remove(recipe);
			 entityManager.getTransaction().commit();
		}
	}
	
}
