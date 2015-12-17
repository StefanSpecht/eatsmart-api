package dom.company.eatsmart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dom.company.eatsmart.model.Recipe;

public class RecipeService {
	
	public List<Recipe> getRecipes() {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Query query = entityManager.createQuery("Select r from Recipe r");
		@SuppressWarnings("unchecked")
		List<Recipe> recipes = query.getResultList();
		return recipes;
	}
	
	public Recipe addRecipe(Recipe recipe) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(recipe);
		entityManager.getTransaction().commit();
		entityManager.close();
				
		return recipe;
	}
	
}
