package dom.company.eatsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Recipe {
	
	@Id @GeneratedValue
	@Column(name="RECIPE_ID")
	private long id;
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User owner;
	private String title;
	private String instruction;
	
	public Recipe() {
		
	}
	
	public Recipe(String title, String instruction) {
	this.title = title;
	this.instruction = instruction;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	@XmlTransient
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
		if (!owner.getRecipes().contains(this)) {
			owner.getRecipes().add(this);
		}
	}
	public void removeOwner() {
		
		User currentOwner = this.getOwner();
		if (currentOwner != null){
			this.owner = null;
		}
		if (currentOwner.getRecipes().contains(this)) {
			currentOwner.getRecipes().remove(this);
		}
	}
	public void updateRecipe(Recipe sourceRecipe) {
		this.id = sourceRecipe.getId();
		this.owner = sourceRecipe.getOwner();
		this.title = sourceRecipe.getTitle();
		this.instruction = sourceRecipe.getInstruction();
		
	}
	
}
