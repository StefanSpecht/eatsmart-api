package dom.company.eatsmart.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import dom.company.eatsmart.model.Ingredient;

public class IngredientService {
	
	public void addIngredient() {	
		List<String> tempList = new ArrayList<String>();
		tempList.add("Tomate");
		tempList.add("Zuchini");
		tempList.add("Zutat99");	
		
		Ingredient ingredient = new Ingredient(tempList);
		EntityManager entityManager = JpaUtil.getEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(ingredient);
		entityManager.getTransaction().commit();
		entityManager.close();
	}	
}
