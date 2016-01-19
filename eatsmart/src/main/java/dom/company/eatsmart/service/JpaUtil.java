package dom.company.eatsmart.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JpaUtil {
	
	protected static EntityManagerFactory entityManagerFactory;
	
	static {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("EatSmart");
		}
	}
	
	private JpaUtil() {
        // Exists only to defeat instantiation.
    }

	public static EntityManager getEntityManager() {
		return entityManagerFactory.createEntityManager();
	}	
	
}