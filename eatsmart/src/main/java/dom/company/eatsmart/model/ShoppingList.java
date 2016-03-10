package dom.company.eatsmart.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.service.JpaUtil;
import dom.company.eatsmart.service.RecipeService;

public class ShoppingList {

	private User user;
	private List<Ingredient> consolidatedRecipeIngredients = new ArrayList<Ingredient>();
	private List<Ingredient> consolidatedStockIngredients = new ArrayList<Ingredient>();
	private List<Ingredient> shoppingListItems = new ArrayList<Ingredient>();

	public ShoppingList(long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, userId);
		if(user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		this.user = user;	
		
		this.updateShoppingListItems();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Ingredient> getConsolidatedRecipeIngredients() {
		return consolidatedRecipeIngredients;
	}

	private void updateConsolidatedRecipeIngredients() {
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, this.user.getId());
		List<Ingredient> newConsolidatedRecipeIngredients = new ArrayList<Ingredient>();
		
		List<MenuSchedule> menuSchedules = user.getMenu().getMenuSchedules();
		menuSchedules.size(); //fetch from jpa
		
		//calculate end date	
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date today = new Date(calendar.getTimeInMillis());
		calendar.add(Calendar.HOUR, (24 * this.user.getHorizonInDays()));
		calendar.add(Calendar.HOUR, 24);
		Date endDate = new Date(calendar.getTimeInMillis());
		
		//filter menu schedules	by end date
		List<Recipe> scheduledRecipes = new ArrayList<Recipe>();
		menuSchedules = menuSchedules
				.stream()
				.filter(schedule -> schedule.getDate().before(endDate))
				.filter(schedule -> schedule.getDate().after(today))
				.collect(Collectors.toList());
		
		//scale recipes and add to scheduled recipes list
		for(int i=0;i<menuSchedules.size();i++) {
			MenuSchedule menuSchedule = menuSchedules.get(i);
			Recipe currentRecipe = menuSchedule.getRecipe();
			Recipe scaledRecipe = currentRecipe.scale(menuSchedule.getServings());			
			scheduledRecipes.add(scaledRecipe);
		}
		
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		for(int i=0;i<scheduledRecipes.size();i++) {
			List<Ingredient> currentIngredients = scheduledRecipes.get(i).getIngredients();
			ingredients.addAll(currentIngredients);
		}
		
				
		Map<Food, List<Ingredient>> ingredientsByFood = ingredients.stream().collect(Collectors.groupingBy(Ingredient::getFood));
		for (Map.Entry<Food, List<Ingredient>> entry : ingredientsByFood.entrySet() ) {
			
			Ingredient consolidatedIngredient = new Ingredient();
			consolidatedIngredient.setFood(entry.getKey());
			consolidatedIngredient.setQuantityInMg(0);
			entry.getValue().forEach(ingredient -> {
				consolidatedIngredient.setDisplayUnit(ingredient.getDisplayUnit());
				consolidatedIngredient.addQuantityInMg(ingredient.getQuantityInMg());
			});
			newConsolidatedRecipeIngredients.add(consolidatedIngredient);
		}
		this.consolidatedRecipeIngredients = newConsolidatedRecipeIngredients;
	}

	public List<Ingredient> getConsolidatedStockIngredients() {
		return consolidatedStockIngredients;
	}

	private void updateConsolidatedStockIngredients() {
		EntityManager entityManager = JpaUtil.getEntityManager();
		User user = entityManager.find(User.class, this.user.getId());
		List<Ingredient> newConsolidatedStockIngredients = new ArrayList<Ingredient>();
		
		List<Stock> fridgeStocks = user.getFridge().getStocks();
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		
		fridgeStocks.forEach(stock -> {
			ingredients.add(stock.getIngredient());			
		});
		
		
		Map<Food, List<Ingredient>> ingredientsByFood = ingredients.stream().collect(Collectors.groupingBy(Ingredient::getFood));
		for (Map.Entry<Food, List<Ingredient>> entry : ingredientsByFood.entrySet() ) {
			
			Ingredient consolidatedIngredient = new Ingredient();
			consolidatedIngredient.setFood(entry.getKey());
			consolidatedIngredient.setQuantityInMg(0);
			entry.getValue().forEach(ingredient -> {
				consolidatedIngredient.setDisplayUnit(ingredient.getDisplayUnit());
				consolidatedIngredient.addQuantityInMg(ingredient.getQuantityInMg());
			});
			newConsolidatedStockIngredients.add(consolidatedIngredient);
		}
		this.consolidatedStockIngredients = newConsolidatedStockIngredients;
	}

	public List<Ingredient> getShoppingListItems() {
		return shoppingListItems;
	}

	public void updateShoppingListItems() {
		this.updateConsolidatedRecipeIngredients();
		this.updateConsolidatedStockIngredients();
		this.shoppingListItems.clear();
		
		
		this.consolidatedRecipeIngredients.forEach(recipeIngredient -> {
			List<Ingredient> relatedStockIngredients = this.consolidatedStockIngredients;
			List<Ingredient> filteredStockIngredients = new ArrayList<Ingredient>();
			filteredStockIngredients = relatedStockIngredients
					.stream()
					.filter(ingredient -> ingredient.getFood().getId() == recipeIngredient.getFood().getId())
					.collect(Collectors.toList());
			
			Ingredient shoppingListItem = recipeIngredient;
			if (filteredStockIngredients.size() == 1) {
				Ingredient relatedStockIngredient = relatedStockIngredients.get(0);
				shoppingListItem.removeQuantityInMg(relatedStockIngredient.getQuantityInMg());
			}
			
			if (shoppingListItem.getQuantityInMg()>0)
			{
				this.shoppingListItems.add(recipeIngredient);
			}
		});
	}
	
}
