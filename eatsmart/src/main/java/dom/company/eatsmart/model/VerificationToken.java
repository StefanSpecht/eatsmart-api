package dom.company.eatsmart.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;
 
    @Id  @GeneratedValue
    private long id;    
    private String token;   
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;     
    private Date expiryDate;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
 
    public VerificationToken() {
    }
    
    public VerificationToken(User user, String token, TokenType tokenType) {
        this.token = token;
        this.user = user;
        this.tokenType = tokenType;
        this.expiryDate = calculateExpiryDate(EXPIRATION);	
    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
	
	public boolean isExpired() {
		Calendar calendar = Calendar.getInstance();
		if ((this.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
			return true;
		}
		return false;
	}
}
