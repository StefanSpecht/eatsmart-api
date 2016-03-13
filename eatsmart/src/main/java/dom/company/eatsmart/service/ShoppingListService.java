package dom.company.eatsmart.service;

import java.util.List;

import dom.company.eatsmart.model.Ingredient;
import dom.company.eatsmart.model.ShoppingList;

public class ShoppingListService {
	
	UserService userService = new UserService();
	
	public List<Ingredient> getShoppingListItems(long userId) {
		
		ShoppingList shoppingList = new ShoppingList(userId);
		return shoppingList.getShoppingListItems();
	}
	
}
