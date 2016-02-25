package dom.company.eatsmart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Menu {
	
	@Id @GeneratedValue
	@Column(name="MENU_ID")
	private long id;
}
