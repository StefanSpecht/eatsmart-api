package dom.company.eatsmart.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ingredient {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private List<String> name;
	
	public Ingredient() {
	}	
	
	public Ingredient(List<String> name) {
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<String> getName() {
		return name;
	}
	public void setName(List<String> name) {
		this.name = name;
	}
	
	
	
	
	
	
	
	
	
}
