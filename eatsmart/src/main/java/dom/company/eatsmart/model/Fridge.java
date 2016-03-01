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

@Entity
public class Fridge {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FRIDGE_ID")
	private long id;
	
	@OneToMany(targetEntity = Stock.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy="fridge")
	private List<Stock> stocks = new ArrayList<Stock>();

	public Fridge() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}
	
	public void addStock(Stock stock) {
		this.stocks.add(stock);
		
		if (stock.getFridge() != this) {
			stock.setFridge(this);
		}
	}
	
	public void removeStock(Stock stock) {
		
		if (this.stocks.contains(this)) {
			this.stocks.remove(stock);
			
			if (stock.getFridge().equals(this)) {
				stock.removeFridge();
			}
		}
    }
	
	
	
}
