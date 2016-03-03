package dom.company.eatsmart.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Embeddable
public class Ingredient {

	private long quantityInMg;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="displayUnit must not be null")
	private ScaleUnit displayUnit;

	@OneToOne (targetEntity = Food.class, fetch = FetchType.EAGER)
	@NotNull(message="food must not be null")
	@Valid
	private Food food;
	
	public Ingredient() {

	}

	public long getQuantityInMg() {
		return quantityInMg;
	}

	public void setQuantityInMg(long quantityInMg) {
		this.quantityInMg = quantityInMg;
	}

	public ScaleUnit getDisplayUnit() {
		return displayUnit;
	}

	public void setDisplayUnit(ScaleUnit displayUnit) {
		this.displayUnit = displayUnit;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}
	
	
	
}
