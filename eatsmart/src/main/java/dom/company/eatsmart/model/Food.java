package dom.company.eatsmart.model;

import java.util.Set;

import javax.persistence.Embeddable;

@Embeddable //?
public class Food {

	private String name;
	private long weightPerUnit;
	private Food parentFood;
	private Set<Food> childFoods;
	
	public Food() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getWeightPerUnit() {
		return weightPerUnit;
	}

	public void setWeightPerUnit(long weightPerUnit) {
		this.weightPerUnit = weightPerUnit;
	}

	public Food getParentFood() {
		return parentFood;
	}

	public void setParentFood(Food parentFood) {
		this.parentFood = parentFood;
	}

	public Set<Food> getChildFoods() {
		return childFoods;
	}

	public void setChildFoods(Set<Food> childFoods) {
		this.childFoods = childFoods;
	}
	
	
	
}
