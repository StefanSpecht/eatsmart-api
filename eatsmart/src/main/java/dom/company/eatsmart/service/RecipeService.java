package dom.company.eatsmart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import dom.company.eatsmart.model.Recipe;

public class RecipeService {
	
	public List<Recipe> getRecipes() {
		EntityManager entityManager = JpaUtil.getEntityManager();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
		TypedQuery<Recipe> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	public Recipe getRecipe(long id) {
		return JpaUtil.getEntityManager().find(Recipe.class, id);
	}
	
	public Recipe addRecipe(Recipe recipe) {	
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(recipe);
		entityManager.getTransaction().commit();
		entityManager.close();
				
		return recipe;
	}
	
	public Recipe updateRecipe(Recipe recipe) {
		EntityManager entityManager = JpaUtil.getEntityManager();

		if (recipe.getId() <= 0) {
			return null;
		}
		else {
			return entityManager.merge(recipe);
		}
	}
	
	public void removeRecipe(long id) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		Recipe recipe = entityManager.find(Recipe.class, id);
		if (recipe != null) {
			entityManager.remove(recipe);
		}
	}
	
}
