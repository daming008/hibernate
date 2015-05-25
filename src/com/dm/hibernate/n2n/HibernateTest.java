package com.dm.hibernate.n2n;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HibernateTest {
	
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	@Before
	public void init(){
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = 
				new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
	}
	
	@After
	public void destory(){
		transaction.commit();
		session.close();
		sessionFactory.close();
	}
	
	@Test
	public void testGet(){
	}
	
	@Test
	public void testSave(){
		Category category1 = new Category();
		category1.setCategoryName("c_AA");
		Category category2 = new Category();
		category2.setCategoryName("c_BB");
		
		Item item1 = new Item();
		item1.setItemName("i_AA");
		Item item2 = new Item();
		item2.setItemName("i_BB");
		
		category1.getItems().add(item1);
		category1.getItems().add(item2);
		category2.getItems().add(item1);
		category2.getItems().add(item2);
		
		item1.getCategories().add(category1);
		item1.getCategories().add(category2);
		item2.getCategories().add(category1);
		item2.getCategories().add(category2);
		
		session.save(category1);
		session.save(category2);
		
		session.save(item1);
		session.save(item2);
	}
	
}
