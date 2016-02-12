package dom.company.eatsmart.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;
 
    @Id
    @GeneratedValue
    private long id;    
    private String token;   
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;     
    private Date expiryDate;
    private TokenType tokenType;
 
    public VerificationToken() {
    }
    
    public VerificationToken(String token, User user, TokenType tokenType) {
        this.token = token;
        this.user = user;
        this.tokenType = tokenType;
        this.expiryDate = calculateExpiryDate(EXPIRATION);	
    }
     
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
