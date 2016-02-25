package dom.company.eatsmart.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity
public class Stock {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RECIPE_ID")
	private long id;
	
	@Embedded
	private Ingredient ingredient;
}
