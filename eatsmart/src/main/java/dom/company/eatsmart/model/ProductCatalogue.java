package dom.company.eatsmart.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class ProductCatalogue {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PRODUCTCATALOGUE_ID")
	private long id;
	
	private int futureUse;

	@OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy="productCatalogue")
	private List<Product> products = new ArrayList<Product>();

	public ProductCatalogue() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@XmlTransient
	public int getFutureUse() {
		return futureUse;
	}

	public void setFutureUse(int futureUse) {
		this.futureUse = futureUse;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
		
		if (product.getProductCatalogue() != this) {
			product.setProductCatalogue(this);
		}
	}
	
	public void removeProduct(Product product) {
		
		if (this.products.contains(product)) {
			this.products.remove(product);
			
		}
    }
}
