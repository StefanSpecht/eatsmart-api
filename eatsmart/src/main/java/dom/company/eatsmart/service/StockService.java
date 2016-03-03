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
import dom.company.eatsmart.model.Stock;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.Fridge;
import dom.company.eatsmart.model.User;

public class StockService {
	
	UserService userService = new UserService();
	
	public List<Stock> getStocks(long userId, String qName, String sort) {
		User user = userService.getUser(userId);
		
		if (user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		List<Stock> stocks = user.getFridge().getStocks();
		
		//filter
		if (qName != null) {
			stocks = stocks
					.stream()
					.filter(stock -> stock.getIngredient().getFood().getName().matches(qName))
					.collect(Collectors.toList());
		}
		
		//sort
		if (sort != null) {
			switch (sort) {
				case "name":
					Collections.sort(stocks, (a, b) -> a.getIngredient().getFood().getName().compareToIgnoreCase(b.getIngredient().getFood().getName()));
					break;
				case "-name":
					Collections.sort(stocks, (a, b) -> b.getIngredient().getFood().getName().compareToIgnoreCase(a.getIngredient().getFood().getName()));
					break;
				case "quantity":
					Collections.sort(stocks, (a, b) -> b.getIngredient().getQuantityInMg() > a.getIngredient().getQuantityInMg() ? -1 : b.getIngredient().getQuantityInMg() == a.getIngredient().getQuantityInMg() ? 0 : 1);
					break;
				case "-quantity":
					Collections.sort(stocks, (a, b) -> b.getIngredient().getQuantityInMg() < a.getIngredient().getQuantityInMg() ? -1 : b.getIngredient().getQuantityInMg() == a.getIngredient().getQuantityInMg() ? 0 : 1);
					break;
				default:
					Collections.sort(stocks, (a, b) -> a.getIngredient().getFood().getName().compareToIgnoreCase(b.getIngredient().getFood().getName()));
			}
		}
		else {
			Collections.sort(stocks, (a, b) -> a.getIngredient().getFood().getName().compareToIgnoreCase(b.getIngredient().getFood().getName()));
		}
		return stocks;				
	}
	
	public Stock getStock(long userId, long stockId) {
		List<Stock> allStocks = this.getStocks(userId, null, null);				
		List<Stock> filteredStocks = allStocks.stream().filter(stock -> stock.getId() == stockId).collect(Collectors.toList());
		
		if (filteredStocks.isEmpty()) {		
			throw new DataNotFoundException("Stock with ID " + stockId + " not found for user with ID " + userId);
		}
		return filteredStocks.get(0);
	}
	
	public Stock addStock(Stock stock, long userId) {
			
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		///check if food was passed correctly
		Food food = stock.getIngredient().getFood();
		Food managedFood = entityManager.find(Food.class, food.getId());
		
		try {
			if (!food.getName().equals(managedFood.getName()) || food.getWeightPerUnit() != managedFood.getWeightPerUnit()) {
				throw new DataConflictException("Food not found. Must be added to food catalogue first.");
			}
		}
		catch (NullPointerException ex) {
			throw new DataConflictException("Food not found. Must be added to food catalogue first.");
		}
		
		User user = userService.getUser(userId);
		Fridge managedFridge = entityManager.find(Fridge.class, user.getFridge().getId());
		
		try {
			entityManager.getTransaction().begin();
			managedFridge.addStock(stock);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("Food not found. Must be added to food catalogue first.");
		}
		return stock;
	}
	
	public void updateStock(long userId, Stock updatedStock) {
		
		Stock currentStock = this.getStock(userId, updatedStock.getId());
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Stock managedCurrentStock = entityManager.find(Stock.class, currentStock.getId());
		
		try {
			entityManager.getTransaction().begin();
			managedCurrentStock.updateStock(updatedStock);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("All foods must be added to food catalogue first");
		}
	}
	
	public void deleteStock(long userId, long stockId) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		Stock stock = this.getStock(userId, stockId);
		User user = userService.getUser(userId);
		
		Stock managedStock = entityManager.find(Stock.class, stock.getId());		
		User managedUser = entityManager.find(User.class, user.getId());
		Fridge managedFridge = managedUser.getFridge();
		
		entityManager.getTransaction().begin();
		managedFridge.removeStock(managedStock);
		entityManager.getTransaction().commit();
	}
	
}
