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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import dom.company.eatsmart.exception.DataConflictException;
import dom.company.eatsmart.exception.DataNotFoundException;
import dom.company.eatsmart.exception.ResourceAlreadyExistsException;
import dom.company.eatsmart.model.Product;
import dom.company.eatsmart.model.Food;
import dom.company.eatsmart.model.ProductCatalogue;
import dom.company.eatsmart.model.User;

public class ProductService {
	
	UserService userService = new UserService();
	
	public List<Product> getProducts(long userId, String qEan) {
		User user = userService.getUser(userId);
		
		if (user == null) {
			throw new DataNotFoundException("User with ID " + userId + " not found");
		}
		List<Product> products = user.getProductCatalogue().getProducts();
		
		//filter
		if (qEan != null) {
			products = products
					.stream()
					.filter(product -> product.getEan().matches(qEan))
					.collect(Collectors.toList());
		}
		
		return products;				
	}
	
	public Product getProduct(long userId, long productId) {
		List<Product> allProducts = this.getProducts(userId, null);				
		List<Product> filteredProducts = allProducts.stream().filter(product -> product.getId() == productId).collect(Collectors.toList());
		
		if (filteredProducts.isEmpty()) {		
			throw new DataNotFoundException("Product with ID " + productId + " not found for user with ID " + userId);
		}
		return filteredProducts.get(0);
	}
	
	public Product addProduct(Product product, long userId) {
			
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		//check if ean is available
		if (!this.isEanAvailable(userId, product.getEan())) {
			throw new ResourceAlreadyExistsException("Ean already in use");
		}
		
		//check if food was passed correctly
		FoodService foodService = new FoodService();
		foodService.validateFood(product.getIngredient().getFood());
		
		//Get user's (managed) productCatalogue and the the product to it
		User user = userService.getUser(userId);
		ProductCatalogue managedProductCatalogue = entityManager.find(ProductCatalogue.class, user.getProductCatalogue().getId());
		
		try {
			entityManager.getTransaction().begin();
			managedProductCatalogue.addProduct(product);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("Food not found. Must be added to food catalogue first.");
		}
		return product;
	}
	
	public void updateProduct(long userId, Product updatedProduct) {
		
		Product currentProduct = this.getProduct(userId, updatedProduct.getId());
		
		//verify that EAN didn't change
		if (!updatedProduct.getEan().equals(currentProduct.getEan())) {
			throw new DataConflictException("EAN must not be changed");
		}
		
		EntityManager entityManager = JpaUtil.getEntityManager();
		Product managedCurrentProduct = entityManager.find(Product.class, currentProduct.getId());
		
		try {
			entityManager.getTransaction().begin();
			managedCurrentProduct.updateProduct(updatedProduct);
			entityManager.getTransaction().commit();
		}
		catch(RollbackException ex) {
			throw new DataConflictException("All foods must be added to food catalogue first");
		}
	}
	
	public void deleteProduct(long userId, long productId) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		Product product = this.getProduct(userId, productId);
		User user = userService.getUser(userId);
		
		Product managedProduct = entityManager.find(Product.class, product.getId());		
		User managedUser = entityManager.find(User.class, user.getId());
		ProductCatalogue managedProductCatalogue = managedUser.getProductCatalogue();
		
		entityManager.getTransaction().begin();
		managedProductCatalogue.removeProduct(managedProduct);
		entityManager.getTransaction().commit();
	}
	
	public boolean isEanAvailable(long userId, String ean){
		List<Product> products = this.getProducts(userId, ean);
		
		return products.isEmpty();
	
	}
	
}
