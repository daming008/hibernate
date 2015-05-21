package com.dm.hibernate.entities.n21.both;

import static org.junit.Assert.*;

import java.util.Date;

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
	public void testCascade(){
		Customer customer = (Customer) session.get(Customer.class, 3);
		customer.getOrders().clear();
	}
	
	@Test
	public void testDelete(){
		Customer customer = (Customer) session.get(Customer.class, 2);
		session.delete(customer);
	}
	
	@Test
	public void testGet(){
		Customer customer = (Customer) session.get(Customer.class, 3);
		System.out.println(customer.getOrders());
		
	}
	@Test
	public void testMany2One(){
		Customer customer = new Customer();
		customer.setCustomerName("lideming");
		
		Order order1 = new Order();
		order1.setOrderName("order-1");
		
		Order order2 = new Order();
		order2.setOrderName("order-2");
		
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		
		session.save(customer);
//		session.save(order1);
//		session.save(order2);
	}
	
}
