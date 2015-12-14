package dom.company.eatsmart.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import dom.company.eatsmart.model.Recipe;

public class RecipeService {
	
	public List<Recipe> getAllRecipes() {
		Recipe r1 = new Recipe("Spaghetti Carbonara","Nudel kochen, maggi fix dazu, fertig!");
		Recipe r2 = new Recipe("Spaghetti Gamba","Nudel kochen, gamba rein, maggi fix dazu, fertig!");
		
		List<Recipe> recipeList= new ArrayList<>();
		recipeList.add(r1);
		recipeList.add(r2);
		
		return recipeList;
	}
	
	public Recipe addRecipe(Recipe recipe) {
		
		//Recipe testRecipe = new Recipe("Spaghetti mit Speck","Speck und Spaghetti in den Topf und warten");
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(recipe);
		entityManager.getTransaction().commit();
		entityManager.close();
		
		
		return recipe;
	}
	
}
