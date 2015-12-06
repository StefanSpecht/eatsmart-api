package dom.company.eatsmart2.service;

import java.util.ArrayList;
import java.util.List;

import dom.company.eatsmart2.model.Recipe;

public class RecipeService {
	public List<Recipe> getAllRecipes() {
		Recipe r1 = new Recipe(1L,"Spaghetti Carbonara","Nudel kochen, maggi fix dazu, fertig!");
		Recipe r2 = new Recipe(2L,"Spaghetti Gamba","Nudel kochen, gamba rein, maggi fix dazu, fertig!");
		
		List<Recipe> recipeList= new ArrayList<>();
		recipeList.add(r1);
		recipeList.add(r2);
		
		return recipeList;
	}
}
