package dom.company.eatsmart.model;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;


public class SmartRankRecipe {

	private long id;
	private RecipeBook recipeBook;
	private String name;
	private byte[] picture;
	private String prepInstruction;
	private String prepTime;
	private int rating;
	private int servings;
	private double smartRanking;
	
	private List<Ingredient> ingredients;
	
	public SmartRankRecipe() {

	}
	
	public SmartRankRecipe(Recipe sourceRecipe) {
		this.id = sourceRecipe.getId();
		this.recipeBook = sourceRecipe.getRecipeBook();
		this.name = sourceRecipe.getName();
		this.ingredients = sourceRecipe.getIngredients();
		this.picture = sourceRecipe.getPicture();
		this.prepInstruction = sourceRecipe.getPrepInstruction();
		this.prepTime = sourceRecipe.getPrepTime();
		this.rating = sourceRecipe.getRating();
		this.servings = sourceRecipe.getServings();	
	}
	
	public SmartRankRecipe(Recipe sourceRecipe, double smartRanking) {
		this.id = sourceRecipe.getId();
		this.recipeBook = sourceRecipe.getRecipeBook();
		this.name = sourceRecipe.getName();
		this.ingredients = sourceRecipe.getIngredients();
		this.picture = sourceRecipe.getPicture();
		this.prepInstruction = sourceRecipe.getPrepInstruction();
		this.prepTime = sourceRecipe.getPrepTime();
		this.rating = sourceRecipe.getRating();
		this.servings = sourceRecipe.getServings();	
		this.smartRanking = smartRanking;
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
	
	public double getSmartRanking() {
		return smartRanking;
	}

	public void setSmartRanking(double smartRanking) {
		this.smartRanking = smartRanking;
	}

	@XmlTransient
	public RecipeBook getRecipeBook() {
		return recipeBook;
	}

	public void setRecipeBook(RecipeBook recipeBook) {
		this.recipeBook = recipeBook;
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
}
