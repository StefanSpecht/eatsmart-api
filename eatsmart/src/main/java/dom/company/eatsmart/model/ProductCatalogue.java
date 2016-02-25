package dom.company.eatsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ProductCatalogue {
	
	@Id @GeneratedValue
	@Column(name="PRODUCTCATALOGUE_ID")
	private long id;

}
