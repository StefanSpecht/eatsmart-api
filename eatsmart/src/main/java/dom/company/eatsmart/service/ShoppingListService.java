package dom.company.eatsmart.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.Ingredient;
import dom.company.eatsmart.model.ShoppingList;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.Fridge;
import dom.company.eatsmart.model.User;

public class ShoppingListService {
	
	UserService userService = new UserService();
	
	public List<Ingredient> getShoppingListItems(long userId) {
		
		ShoppingList shoppingList = new ShoppingList(userId);
		return shoppingList.getShoppingListItems();
	}
	
}
