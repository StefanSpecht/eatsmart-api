package dom.company.eatsmart.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class Stock {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STOCK_ID")
	private long id;
	
	@Embedded
	@NotNull
	private Ingredient ingredient;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "FRIDGE_ID")
	private Fridge fridge;

	public Stock() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	@XmlTransient
	public Fridge getFridge() {
		return fridge;
	}

	public void setFridge(Fridge fridge) {
		this.fridge = fridge;
		if (!fridge.getStocks().contains(this)) {
			fridge.getStocks().add(this);
		}
	}
	
	public void removeFridge() {
		
		Fridge currentFridge = this.getFridge();
		if (currentFridge != null){
			this.fridge = null;
		}
		if (currentFridge.getStocks().contains(this)) {
			currentFridge.removeStock(this);
		}
	}
	
	
	
	
}
