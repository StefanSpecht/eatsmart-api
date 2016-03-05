package dom.company.eatsmart.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class MenuSchedule {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MENUSCHEDULE_ID")
	private long id;
	
	@NotNull
	private Date date;
	
	@Min(1)
	private int servings;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "MENU_ID")
	private Menu menu;
	
	@OneToOne(targetEntity = Recipe.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "RECIPE_ID")
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
	
	
	
	
	
	
}
