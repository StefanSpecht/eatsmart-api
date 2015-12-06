package dom.company.eatsmart2.model;

public class Recipe {
	
	
	private long id;
	private String title;
	private String instruction;
	
	public Recipe() {
		
	}
	
	public Recipe(long id, String title, String instruction) {
	super();
	this.id = id;
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
