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
	/*
	@ManyToOne
	@JoinColumn(name="USER_ID")

	private User owner;
		*/
	private String name;
	private byte[] picture;
	private String prepInstruction;
	private String prepTime;
	private int rating;
	private int servings;

	public Recipe() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getPrepInstruction() {
		return prepInstruction;
	}

	public void setPrepInstruction(String prepInstruction) {
		this.prepInstruction = prepInstruction;
	}

	public String getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}
	
	
	
	
	
/*
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
	*/
	
}
