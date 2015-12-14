package dom.company.eatsmart2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Recipe {
	
	@Id @GeneratedValue
	private long id;
	private String title;
	private String instruction;
	
	public Recipe() {
		
	}
	
	public Recipe(String title, String instruction) {
	super();
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
}
