package dom.company.eatsmart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Food {

	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name="FOOD_ID")
	private long id;
	
	@NotBlank(message="name must not be blank or null")
	private String name;
	
	private long weightPerUnit;
	
	@ManyToOne
	@JoinColumn(name="PARENT_ID")
	private Food parentFood;
	
	@OneToMany(mappedBy="parentFood",cascade=CascadeType.ALL)
	private List<Food> childFoods = new ArrayList<Food>();
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User owner;
	
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
	@XmlTransient
	public Food getParentFood() {
		return parentFood;
	}

	public void setParentFood(Food parentFood) {
		this.parentFood = parentFood;
		
		if (!parentFood.getChildFoods().contains(this)) {
			parentFood.getChildFoods().add(this);
		}
	}
	
	public void removeParentFood() {
		Food currentParentFood = this.getParentFood();
		if (currentParentFood != null){
			this.parentFood = null;
		}
		if (currentParentFood.getChildFoods().contains(this)) {
			currentParentFood.removeChildFood(this);
		}
	}
	
	@XmlTransient
	public List<Food> getChildFoods() {
		return childFoods;
	}

	public void setChildFoods(List<Food> childFoods) {
		this.childFoods = childFoods;
	}
	
	public void addChildFood(Food food) {
		this.childFoods.add(food);
		
		if (!food.getParentFood().equals(this)) {
			food.setParentFood(this);
		}
	}
	
	public void removeChildFood(Food food) {
		if (this.childFoods.contains(this)) {
			
			this.childFoods.remove(this);
			
			if (food.getParentFood().equals(this)) {
				food.setParentFood(null);
			}
		}
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlTransient
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
		
}
