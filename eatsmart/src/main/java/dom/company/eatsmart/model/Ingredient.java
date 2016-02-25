package dom.company.eatsmart.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;

@Embeddable //?
public class Ingredient {

	private long quantityInMg;
	@Enumerated(EnumType.STRING)
	private ScaleUnit displayUnit;
	//@Embedded
	@OneToOne(targetEntity = Food.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "FOOD_ID")
	@XmlTransient
	private Food food;
	
	public Ingredient() {

	}

	public long getQuantityInMg() {
		return quantityInMg;
	}

	public void setQuantityInMg(long quantityInMg) {
		this.quantityInMg = quantityInMg;
	}

	public ScaleUnit getScaleUnit() {
		return displayUnit;
	}

	public void setScaleUnit(ScaleUnit scaleUnit) {
		this.displayUnit = scaleUnit;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}
	
	
	
}
