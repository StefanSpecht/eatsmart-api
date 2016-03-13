package dom.company.eatsmart.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
	
	public Ingredient(Ingredient sourceIngredient) {
		this.displayUnit = sourceIngredient.getDisplayUnit();
		this.food = sourceIngredient.getFood();
		this.quantityInMg = sourceIngredient.getQuantityInMg();
	}

	public long getQuantityInMg() {
		return quantityInMg;
	}

	public void setQuantityInMg(long quantityInMg) {
		this.quantityInMg = quantityInMg;
	}
	
	public void addQuantityInMg(long quantityInMg) {
		this.quantityInMg += quantityInMg;
	}
	
	public void removeQuantityInMg(long quantityInMg) {
		this.quantityInMg -= quantityInMg;
		if (this.quantityInMg < 0) {
			this.quantityInMg = 0;
		}
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
