package dom.company.eatsmart.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class MailAuthenticator extends Authenticator {
 
        private final String user;
        private final String password;
 
        public MailAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }
        
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.password);
        }
    }