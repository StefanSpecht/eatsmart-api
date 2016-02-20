package dom.company.eatsmart.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable //?
public class Ingredient {

	private long quantityInMg;
	private ScaleUnit scaleUnit;
	@Embedded
	private Food food;
	
	public Ingredient() {

	}
	
}
