package dom.company.eatsmart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class RecipeBook {

	@Id @GeneratedValue
	private long id;
	
	@OneToMany(targetEntity = Recipe.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(nullable = false, name = "RECIPEBOOK_ID")
	private List<Recipe> recipes;

	public RecipeBook() {
		
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void addRecipe(Recipe recipe) {
		this.recipes.add(recipe);
		
		if (recipe.getRecipeBook() != this) {
			recipe.setRecipeBook(this);
		}
	}
	
	public void removeRecipe(Recipe recipe) {
		
		if (this.recipes.contains(this)) {
			this.recipes.remove(recipe);
			
			if (recipe.getRecipeBook().equals(this)) {
				recipe.removeRecipeBook();
			}
		}
		
    }

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
		
	
}
