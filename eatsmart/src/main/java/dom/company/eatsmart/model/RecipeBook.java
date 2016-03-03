package dom.company.eatsmart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class RecipeBook {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private int futureUse;
	
	@OneToMany(targetEntity = Recipe.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy="recipeBook")
	private List<Recipe> recipes = new ArrayList<Recipe>();

	public RecipeBook() {
		
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@XmlTransient
	public int getFutureUse() {
		return futureUse;
	}

	public void setFutureUse(int futureUse) {
		this.futureUse = futureUse;
	}
	
	public void addRecipe(Recipe recipe) {
		this.recipes.add(recipe);
		
		if (recipe.getRecipeBook() != this) {
			recipe.setRecipeBook(this);
		}
	}
	
	public void removeRecipe(Recipe recipe) {
		
		if (this.recipes.contains(recipe)) {
			this.recipes.remove(recipe);
			/*
			if (recipe.getRecipeBook().equals(this)) {
				recipe.removeRecipeBook();
			}
			*/
		}
    }

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
}
