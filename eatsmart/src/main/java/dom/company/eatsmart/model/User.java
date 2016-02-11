package dom.company.eatsmart.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class User {
	
	@Id @GeneratedValue
	@Column(name="USER_ID")
	private long id;
	@Column(unique=true)
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	@Column(unique=true)
	private String email;
	//private Boolean admin;
	@ElementCollection(targetClass = UserRoles.class)
	@JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Collection<UserRoles> userRoles;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "owner")
	private List<Recipe> recipes;
	
	
	
	public User() {
		//this.admin = false;
		//this.userRoles.add(UserRoles.ADMIN);
	}
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	/*
	public Boolean isAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	*/
	
	@XmlTransient
	public List<Recipe> getRecipes() {
		return recipes;
	}
	public Collection<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<UserRoles> userRoles) {
		this.userRoles = userRoles;
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
		this.firstName = sourceUser.getFirstName();
		this.lastName = sourceUser.getLastName();
		this.email = sourceUser.getEmail();
		this.password = sourceUser.getPassword();
		
	}
}
