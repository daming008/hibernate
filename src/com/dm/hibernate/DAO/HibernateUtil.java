package com.dm.hibernate.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private SessionFactory sessionFactory;
	private HibernateUtil(){}
	private static HibernateUtil instance = new HibernateUtil();
	public static HibernateUtil getInstance() {
		return instance;
	}
	public SessionFactory getSessionFactory() {
		if(sessionFactory == null){
			Configuration configuration = new Configuration().configure();
			ServiceRegistry serviceRegistry = 
					new ServiceRegistryBuilder().applySettings(configuration.getProperties())
					.buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		return sessionFactory;
	}
	
	public Session getSession(){
		return getSessionFactory().getCurrentSession();
	}
}
