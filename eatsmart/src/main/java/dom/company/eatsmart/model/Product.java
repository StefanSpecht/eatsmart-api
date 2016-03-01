package dom.company.eatsmart.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Product {

	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name="PRODUCT_ID")
	private long id;
	
	@NotBlank(message="EAN must not be blank or null")
	private String ean;
	
	@Embedded
	@NotNull(message="ingredient must not be null")
	private Ingredient ingredient;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "CATALOGUE_ID")
	private ProductCatalogue productCatalogue;

	public Product() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public ProductCatalogue getProductCatalogue() {
		return productCatalogue;
	}

	public void setProductCatalogue(ProductCatalogue productCatalogue) {
		this.productCatalogue = productCatalogue;
		if (!productCatalogue.getProducts().contains(this)) {
			productCatalogue.getProducts().add(this);
		}
	}
	
public void removeProductCatalogue() {
		
		ProductCatalogue currentProductCatalogue = this.getProductCatalogue();
		if (currentProductCatalogue != null){
			this.productCatalogue = null;
		}
		if (currentProductCatalogue.getProducts().contains(this)) {
			currentProductCatalogue.removeProduct(this);
		}
	}
	
	
	
}
