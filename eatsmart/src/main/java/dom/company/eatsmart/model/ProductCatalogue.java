package dom.company.eatsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductCatalogue {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PRODUCTCATALOGUE_ID")
	private long id;

}
