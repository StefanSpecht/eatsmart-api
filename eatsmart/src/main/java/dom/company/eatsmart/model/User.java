package dom.company.eatsmart.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class User {
	
	@Id @GeneratedValue
	@Column(name="USER_ID")
	private long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "owner")
	private List<Recipe> recipes;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlTransient
	public List<Recipe> getRecipes() {
		return recipes;
	}
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	public void addRecipe(Recipe recipe) {
		this.recipes.add(recipe);
		if (recipe.getOwner() != this) {
            recipe.setOwner(this);
        }
	}
	public void removeRecipe(Recipe recipe) {
		this.recipes.remove(recipe);
		if (recipe.getOwner() != null) {
            recipe.removeOwner();
        }
	}
		
	public void updateUser(User sourceUser) {
		this.id = sourceUser.getId();
		this.firstName = sourceUser.getFirstName();
		this.lastName = sourceUser.getLastName();
		this.email = sourceUser.getEmail();
		
	}
}
