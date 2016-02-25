package dom.company.eatsmart.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
public class Recipe {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RECIPE_ID")
	private long id;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "RECIPEBOOK_ID")
	private RecipeBook recipeBook;

	@NotBlank(message="Recipe name must not be blank or null")
	private String name;
	private byte[] picture;
	private String prepInstruction;
	private String prepTime;
	@Range(min=0, max=5, message="Rating must be between 0 and 5")
	private int rating;
	@Range(min=1, max=128, message="Number of servings must be between 1 and 128")
	private int servings;
	
	@ElementCollection(targetClass = Ingredient.class)
	@JoinTable(name = "RECIPE_INGREDIENTS", joinColumns = @JoinColumn(name = "RECIPE_ID"))
	private List<Ingredient> ingredients;
	
	public Recipe() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getPrepInstruction() {
		return prepInstruction;
	}

	public void setPrepInstruction(String prepInstruction) {
		this.prepInstruction = prepInstruction;
	}

	public String getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}
	
	@XmlTransient
	public RecipeBook getRecipeBook() {
		return recipeBook;
	}

	public void setRecipeBook(RecipeBook recipeBook) {
		this.recipeBook = recipeBook;
		if (!recipeBook.getRecipes().contains(this)) {
			recipeBook.getRecipes().add(this);
		}
	}
	public void removeRecipeBook() {
		
		RecipeBook currentRecipeBook = this.getRecipeBook();
		if (currentRecipeBook != null){
			this.recipeBook = null;
		}
		if (currentRecipeBook.getRecipes().contains(this)) {
			currentRecipeBook.removeRecipe(this);;
		}
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public void addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

	public void removeIngredient(Ingredient ingredient) {
		if (this.ingredients.contains(ingredient)) {
			this.ingredients.remove(ingredient);
		}
	}
	/*
	public void updateRecipe(Recipe sourceRecipe) {
		this.id = sourceRecipe.getId();
		this.owner = sourceRecipe.getOwner();
		this.title = sourceRecipe.getTitle();
		this.instruction = sourceRecipe.getInstruction();
		
	}
	*/
	
}
