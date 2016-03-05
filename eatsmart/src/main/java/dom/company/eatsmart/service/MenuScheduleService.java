package dom.company.eatsmart.service;

import java.sql.Date;
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
import javax.validation.ConstraintViolationException;

import dom.company.eatsmart.exception.BadRequestException;
import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.model.MenuSchedule;
import dom.company.eatsmart.model.Recipe;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.Ingredient;
import dom.company.eatsmart.model.Menu;
import dom.company.eatsmart.model.User;

public class MenuScheduleService {
	
	UserService userService = new UserService();
	
	public List<MenuSchedule> getMenuSchedules(long userId, Date startDate, Date endDate) {
		User user = userService.getUser(userId);
		
		if (user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		
		try {
			if (startDate.after(endDate)) {
				throw new BadRequestException("EndDate must not be before StartDate");
			}
		}
		catch (NullPointerException ex) {
		}
				
		List<MenuSchedule> menuSchedules = user.getMenu().getMenuSchedules();
		
		//filter by startDate
		if (startDate != null) {
			menuSchedules = menuSchedules
					.stream()
					.filter(menuSchedule -> (menuSchedule.getDate().equals(startDate) || menuSchedule.getDate().after(startDate)) )
					.collect(Collectors.toList());
		}
		
		//filter by endDate
				if (endDate != null) {
					menuSchedules = menuSchedules
							.stream()
							.filter(menuSchedule -> (menuSchedule.getDate().equals(endDate) || menuSchedule.getDate().before(startDate)) )
							.collect(Collectors.toList());
				}
				
		return menuSchedules;
	}
	
	public MenuSchedule getMenuSchedule(long userId, long menuScheduleId) {
		List<MenuSchedule> allMenuSchedules = this.getMenuSchedules(userId, null, null);				
		List<MenuSchedule> filteredMenuSchedules = allMenuSchedules.stream().filter(menuSchedule -> menuSchedule.getId() == menuScheduleId).collect(Collectors.toList());
		
		if (filteredMenuSchedules.isEmpty()) {		
			throw new DataNotFoundException("MenuSchedule with ID " + menuScheduleId + " not found for user with ID " + userId);
		}
		return filteredMenuSchedules.get(0);
	}
	
	public MenuSchedule addMenuSchedule(MenuSchedule menuSchedule, long userId) {
		
		EntityManager entityManager = JpaUtil.getEntityManager();	
		RecipeService recipeService = new RecipeService();
		
		///check if food was passed correctly		
		Recipe recipe = menuSchedule.getRecipe();
		recipeService.validateRecipe(recipe);
		
		User user = userService.getUser(userId);
		Menu managedMenu = entityManager.find(Menu.class, user.getMenu().getId());
		
		try {
			entityManager.getTransaction().begin();
			managedMenu.addMenuSchedule(menuSchedule);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("Food not found. Must be added to food catalogue first.");
		}
		return menuSchedule;
	}
	
	public void updateMenuSchedule(long userId, MenuSchedule updatedMenuSchedule) {
		
		MenuSchedule currentMenuSchedule = this.getMenuSchedule(userId, updatedMenuSchedule.getId());
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		MenuSchedule managedCurrentMenuSchedule = entityManager.find(MenuSchedule.class, currentMenuSchedule.getId());
		
		try {
			entityManager.getTransaction().begin();
			managedCurrentMenuSchedule.updateMenuSchedule(updatedMenuSchedule);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("All foods must be added to food catalogue first");
		}
	}
	
	public void deleteMenuSchedule(long userId, long menuScheduleId) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		MenuSchedule menuSchedule = this.getMenuSchedule(userId, menuScheduleId);
		User user = userService.getUser(userId);
		
		MenuSchedule managedMenuSchedule = entityManager.find(MenuSchedule.class, menuSchedule.getId());		
		User managedUser = entityManager.find(User.class, user.getId());
		Menu managedMenu = managedUser.getMenu();
		
		entityManager.getTransaction().begin();
		managedMenu.removeMenuSchedule(managedMenuSchedule);
		entityManager.getTransaction().commit();
	}
}
		