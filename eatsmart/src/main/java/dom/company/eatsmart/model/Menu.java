package dom.company.eatsmart.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Menu {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MENU_ID")
	private long id;
	
	private int futureUse;
	
	@OneToMany(targetEntity = MenuSchedule.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy="menu")
	private List<MenuSchedule> menuSchedules = new ArrayList<MenuSchedule>();

	public Menu() {
	
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlTransient
	public int getFutureUse() {
		return futureUse;
	}

	public void setFutureUse(int futureUse) {
		this.futureUse = futureUse;
	}
	
	public List<MenuSchedule> getMenuSchedules() {
		return menuSchedules;
	}

	public void setMenuSchedules(List<MenuSchedule> menuSchedules) {
		this.menuSchedules = menuSchedules;
	}
	
	public void addMenuSchedule(MenuSchedule menuSchedule) {
		this.menuSchedules.add(menuSchedule);
		
		if (menuSchedule.getMenu() != this) {
			menuSchedule.setMenu(this);
		}
	}
	
	public void removeMenuSchedule(MenuSchedule menuSchedule) {
		
		if (this.menuSchedules.contains(menuSchedule)) {
			this.menuSchedules.remove(menuSchedule);
			
		}
    }	
}
