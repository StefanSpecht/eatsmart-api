package dom.company.eatsmart.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class MenuSchedule {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MENUSCHEDULE_ID")
	private long id;
	
	@NotNull(message="date must not be null")
	private Date date;
	
	@Min(value = 1, message="servings must be equal or greater 1")
	private int servings;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "MENU_ID")
	private Menu menu;
	
	@OneToOne(targetEntity = Recipe.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "RECIPE_ID")
	@NotNull(message="Recipe must not be null")
	Recipe recipe;

	public MenuSchedule() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}
	
	@XmlTransient
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
		if (!menu.getMenuSchedules().contains(this)) {
			menu.getMenuSchedules().add(this);
		}
	}
	
	public void removeMenu() {
		
		Menu currentMenu = this.getMenu();
		if (currentMenu != null){
			this.menu = null;
		}
		if (currentMenu.getMenuSchedules().contains(this)) {
			currentMenu.removeMenuSchedule(this);
		}
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	public void updateMenuSchedule (MenuSchedule sourceMenuSchedule) {
		this.date = sourceMenuSchedule.getDate();
		this.servings = sourceMenuSchedule.getServings();
		this.recipe = sourceMenuSchedule.getRecipe();
	}
}
