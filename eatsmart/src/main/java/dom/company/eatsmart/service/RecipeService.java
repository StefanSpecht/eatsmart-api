package dom.company.eatsmart.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.Ingredient;
import dom.company.eatsmart.model.Menu;
import dom.company.eatsmart.model.MenuSchedule;
import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.model.RecipeBook;
import dom.company.eatsmart.model.SmartRankRecipe;
import dom.company.eatsmart.model.Stock;
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
	
	public List<SmartRankRecipe> getSmartRankRecipes(long userId) {
		User user = userService.getUser(userId);
		
		if (user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		List<Recipe> recipes = user.getRecipeBook().getRecipes();
		
		return this.generateSmartRanking(user, recipes);
	}
	
	public Recipe getRecipe(long userId, long recipeId) {
		List<Recipe> allRecipes = this.getRecipes(userId, null, null);				
		List<Recipe> filteredRecipes = allRecipes.stream().filter(r -> r.getId() == recipeId).collect(Collectors.toList());
		
		if (filteredRecipes.isEmpty()) {		
			throw new DataNotFoundException("Recipe with ID " + recipeId + " not found for user with ID " + userId);
		}
		
		return filteredRecipes.get(0);
	}
	
	public Recipe getRecipe(long userId, long recipeId, int servings) {
		Recipe unscaledRecipe = this.getRecipe(userId, recipeId);
		if (servings > 0) {
			return unscaledRecipe.scale(servings);
		}
		return unscaledRecipe;
	}
	
	
	
	public Recipe addRecipe(Recipe recipe, long userId) {
			
		EntityManager entityManager = JpaUtil.getEntityManager();
			
		//Check if food was passed correctly
		FoodService foodService = new FoodService();
		List<Ingredient> ingredients = recipe.getIngredients();
		ingredients.forEach(ingredient -> foodService.validateFood(ingredient.getFood()));
		
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
		
		//Check if food was passed correctly
		FoodService foodService = new FoodService();
		List<Ingredient> ingredients = updatedRecipe.getIngredients();
		ingredients.forEach(ingredient -> foodService.validateFood(ingredient.getFood()));
		
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
		
		Menu managedMenu = managedUser.getMenu();
		
		List<MenuSchedule> managedMenuSchedules = managedMenu.getMenuSchedules();
	
		List<MenuSchedule> filteredMenuSchedule = managedMenuSchedules
				.stream()
				.filter(schedule -> schedule.getRecipe().getId() == recipe.getId())
				.collect(Collectors.toList());
		
		entityManager.getTransaction().begin();
		managedMenu.removeMenuSchedules(filteredMenuSchedule);
		managedRecipeBook.removeRecipe(managedRecipe);
		entityManager.getTransaction().commit();
	}
	
	public void validateRecipe (Recipe recipe) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		FoodService foodService = new FoodService();
		Recipe managedRecipe = entityManager.find(Recipe.class, recipe.getId());
		
		try {
			if (!recipe.getName().equals(managedRecipe.getName()) 
					|| recipe.getRating() != managedRecipe.getRating()
					|| recipe.getServings() != managedRecipe.getServings() ) {
				throw new DataConflictException("Recipe not found. Must be added to recipe book first.");
			}
		}
		catch (NullPointerException ex) {
			throw new DataConflictException("Recipe not found. Must be added to recipe book first.");
		}
			
		//check ingredients
		List<Ingredient> ingredients = recipe.getIngredients();
		List<Ingredient> managedIngredients = managedRecipe.getIngredients();
		
		try {
			for (int i = 0; i < ingredients.size(); i++) {
				if (!ingredients.get(i).getDisplayUnit().equals(managedIngredients.get(i).getDisplayUnit())
						|| ingredients.get(i).getQuantityInMg() != managedIngredients.get(i).getQuantityInMg()
						) {
					throw new DataConflictException("Ingredient not found. Must be added to recipe first.");
				}
				foodService.validateFood(ingredients.get(i).getFood());
			}
		}
		catch (NullPointerException ex) {
			throw new DataConflictException("Ingredient not found. Must be added to recipe first.");
		}
		
	}
	
	public List<SmartRankRecipe> generateSmartRanking(User user, List<Recipe> recipes) {
		
		FoodService foodService = new FoodService();
		
		//HashMap<double, Recipe> recipeMap = new HashMap<double, Recipe>();
		List<SmartRankRecipe> rankedRecipes = new ArrayList<SmartRankRecipe>();
		
		for (Recipe recipe : recipes) {
			List<Ingredient> ingredients = recipe.getIngredients();
			
			double score = 0;
			
			for (Ingredient ingredient : ingredients) {
				Food food = foodService.getFood(user.getId(), ingredient.getFood().getId());
				
				//get available quantity of exact food from fridge stock
				double availableFoodQuantity = this.getAvailableFoodQuantity(food, user);
				if (availableFoodQuantity < 0) {
					availableFoodQuantity = 0;
				}
				
				//get available quantity of parent(s) food from fridge stock
				double availableFoodQuantityOfParents = this.getAvailableFoodQuantityOfParents(food.getParentFood(), user);
				
				//get available quantity of of child(s) food from fridge stock
				double availableFoodQuantityOfChilds = this.getAvailableFoodQuantityOfChilds(food.getChildFoods(), user);
				
				//get available quantity of similar foods from fridge stock
				double availableFoodQuantityOfRelatives = this.getAvailableFoodQuantityOfRelatives(food, user);
				
				//calculate value of available quantity of food - exact
				double availableFoodQuantityExact = availableFoodQuantity + availableFoodQuantityOfParents + availableFoodQuantityOfChilds;
				
				//calculate total available qunatity of this food (exact + related)
				double availableFoodQuantityTotal = availableFoodQuantityExact + availableFoodQuantityOfRelatives;
				
				//required quantity
				double requiredFoodQuantity = ingredient.getQuantityInMg();
				
				if (availableFoodQuantityExact >= requiredFoodQuantity) {
					score += 100;
				}
				
				if (availableFoodQuantityTotal >= requiredFoodQuantity && availableFoodQuantityExact < requiredFoodQuantity) {
					score += ( (availableFoodQuantityExact / requiredFoodQuantity) 
							+ ( (requiredFoodQuantity - availableFoodQuantityExact) / requiredFoodQuantity * 0.75) )
							* 100;
				}	
				
				if (availableFoodQuantityTotal < requiredFoodQuantity) {
					score += ( (availableFoodQuantityExact / requiredFoodQuantity) 
							+ ( availableFoodQuantityOfRelatives / requiredFoodQuantity * 0.75) )
							* 0.3 * 100;
				}				
			}
			score = score / ingredients.size();
			rankedRecipes.add(new SmartRankRecipe(recipe,score));
		}
		
		Collections.sort(rankedRecipes, (b, a) -> a.getSmartRanking() < b.getSmartRanking() ? -1 : a.getSmartRanking() == b.getSmartRanking() ? 0 : 1);
		return rankedRecipes;
	}
	
	private double getAvailableFoodQuantity(Food food, User user) {
		
		//Get all available stocks for this food
		List<Stock> stocks = user.getFridge().getStocks();		
		stocks = stocks
				.stream()
				.filter(stock -> stock.getIngredient().getFood().getId() == food.getId())
				.collect(Collectors.toList());
				
		if (stocks.isEmpty()) {
			return 0;
		}		
		
		//Add all available quantities for this food
		double quantityOnStock = 0;		
		for (Stock stock : stocks) {
			quantityOnStock += stock.getIngredient().getQuantityInMg();
		}
		
		//Get all scheduled menus of future within users horizon
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date today = new Date(calendar.getTimeInMillis());
		calendar.add(Calendar.HOUR, (24 * user.getHorizonInDays()));
		calendar.add(Calendar.HOUR, 24);
		Date endDate = new Date(calendar.getTimeInMillis());
		
		//filter menu schedules	by end date
		List<MenuSchedule> menuSchedules = user.getMenu().getMenuSchedules();
		menuSchedules = menuSchedules
				.stream()
				.filter(schedule -> schedule.getDate().before(endDate))
				.filter(schedule -> schedule.getDate().after(today))
				.collect(Collectors.toList());
		
		
		//Get ingredients of all scheduled menus
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		for(int i=0;i<menuSchedules.size();i++) {
			Recipe scaledRecipe = menuSchedules.get(i).getRecipe().scale(menuSchedules.get(i).getServings());
			List<Ingredient> currentIngredients = scaledRecipe.getIngredients();
			ingredients.addAll(currentIngredients);
		}
		
		//filter ingredients for those containing the requested food
		ingredients = ingredients
				.stream()
				.filter(ingredient -> ingredient.getFood().getId() == food.getId())
				.collect(Collectors.toList());
		
		//Add all planned quantities for this food
		double plannedQuantityBySchedules = 0;	
		
		for (Ingredient ingredient : ingredients) {
			plannedQuantityBySchedules += ingredient.getQuantityInMg();
		}
					
		//final calculation of available quantity
		return quantityOnStock - plannedQuantityBySchedules;	
	}
	
	private double getAvailableFoodQuantityOfParents(Food parentFood, User user) {
		
		if (parentFood != null) {
			double availableFoodQuantity = this.getAvailableFoodQuantity(parentFood, user);
			Food parentParentFood = parentFood.getParentFood();
			
			if (parentParentFood == null) {
				return availableFoodQuantity;
			}
			return availableFoodQuantity + this.getAvailableFoodQuantityOfParents(parentParentFood, user);
		}
		return 0;
	}
	
	private double getAvailableFoodQuantityOfChilds(List<Food> childFoods, User user) {
		
		if (childFoods != null) {
			double availableFoodQuantity = 0;
			
			for(Food childFood : childFoods) {
				availableFoodQuantity += this.getAvailableFoodQuantity(childFood, user);
				
				List<Food> childChildFoods = childFood.getChildFoods();
				availableFoodQuantity += this.getAvailableFoodQuantityOfChilds(childChildFoods, user);
			}
		return availableFoodQuantity;
		}
		return 0;
	}
	
	private double getAvailableFoodQuantityOfRelatives(Food food, User user) {
		
		try {
			List<Food> relatedFoods = food.getParentFood().getChildFoods();
			relatedFoods.remove(food);
			
			double availableFoodQuantityOfRelatives = 0;			
			for (Food relatedFood : relatedFoods) {
				availableFoodQuantityOfRelatives += this.getAvailableFoodQuantity(relatedFood, user);
			}
			
			return availableFoodQuantityOfRelatives;
		}
		catch (NullPointerException ex) {
			return 0;
		}
	}
}
