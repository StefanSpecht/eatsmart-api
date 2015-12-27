package dom.company.eatsmart.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ingredient {
	
	@Id @GeneratedValue
	private int id;
	private List<String> name;
	
	public Ingredient() {	
	}
	
	public Ingredient(List<String> nameList) {
		this.name = nameList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}
	
	
	
	
	
	
	
}
