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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="USER_ID")
	private long id;
	
	@Column(unique=true)
	@NotBlank(message = "Username must not be blank or null")
	private String username;
	
	@Column(unique=true)
	@NotBlank(message = "Email must not be blank or null")
	@Email(message = "Email syntactically not correct")
	private String email;
	
	@Size(min=5, max=32, message = "Password length must be between 5 and 32 characters")
	private String password;
	
	@Min(1)
	private int horizonInDays;
	
	@ElementCollection(targetClass = UserRole.class)
	@JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Collection<UserRole> userRoles;
	
	@OneToOne(targetEntity = RecipeBook.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private RecipeBook recipeBook;
	
	@OneToOne(targetEntity = Menu.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Menu menu;
	
	@OneToOne(targetEntity = Fridge.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Fridge fridge;
	
	@OneToOne(targetEntity = ProductCatalogue.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private ProductCatalogue productCatalogue;
	
	public User() {
		this.recipeBook = new RecipeBook();
		this.menu = new Menu();
		this.fridge = new Fridge();
		this.productCatalogue = new ProductCatalogue();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	public int getHorizonInDays() {
		return horizonInDays;
	}

	public void setHorizonInDays(int horizonInDays) {
		this.horizonInDays = horizonInDays;
	}

	public Collection<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	public void addUserRole(UserRole userRole) {
		if (!this.userRoles.contains(userRole)) {
			this.userRoles.add(userRole);	
		}
	}
	public void removeUserRole(UserRole userRole) {
		if (this.userRoles.contains(userRole)) {
			this.userRoles.remove(userRole);	
		}
	}
	
	@XmlTransient
	public RecipeBook getRecipeBook() {
		return recipeBook;
	}

	public void setRecipeBook(RecipeBook recipeBook) {
		this.recipeBook = recipeBook;
	}
	
	@XmlTransient	
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	@XmlTransient
	public Fridge getFridge() {
		return fridge;
	}

	public void setFridge(Fridge fridge) {
		this.fridge = fridge;
	}

	@XmlTransient
	public ProductCatalogue getProductCatalogue() {
		return productCatalogue;
	}

	public void setProductCatalogue(ProductCatalogue productCatalogue) {
		this.productCatalogue = productCatalogue;
	}

	public void updateUser(User sourceUser) {
		this.username = sourceUser.getUsername();
		this.email = sourceUser.getEmail();
		this.password = sourceUser.getPassword();
		this.horizonInDays = sourceUser.getHorizonInDays();
	}
	
}
