package com.dm.hibernate.strategy;

import java.util.List;

import org.hibernate.Hibernate;
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
	public void testMany2OneStrategy(){
//		Order order = (Order) session.get(Order.class, 1);
//		System.out.println(order.getCustomer().getCustomerName());
//		System.out.println(order);
		
		List<Order> orders = session.createQuery("FROM Order o").list();
		for(Order order : orders){
			if(order.getCustomer() != null){
				System.out.println(order.getCustomer().getCustomerName());
			}
		}
	}
	
	@Test
	public void testConnection(){
		
	}
	
	@Test
	public void testSetFetch2(){
		Customer customer = (Customer) session.get(Customer.class, 1);
		System.out.println(customer.getOrders().size());
	}
	@Test
	public void testSetFetch(){
		@SuppressWarnings("unchecked")
		List<Customer> customers = session.createQuery("FROM Customer").list();
		System.out.println(customers.size());
		for(Customer customer : customers){
			if(customer.getOrders() != null){
				System.out.println(customer.getOrders().size());
			}
		}
	}
	
	@Test
	public void testSetBenchSize(){
		List<Customer> customers = session.createQuery("FROM Customer").list();
		System.out.println(customers.size());
		for(Customer customer : customers){
			if(customer.getOrders() != null){
				System.out.println(customer.getOrders().size());
			}
		}
	}
	
	@Test
	public void testOne2ManyLeverStrategy(){
		Customer customer = (Customer) session.get(Customer.class, 1);
		System.out.println(customer.getClass());
		System.out.println(customer.getCustomerName());
		System.out.println(customer.getOrders().size());
		
		Order order = new Order();
		order.setOrderID(1);
		System.out.println(customer.getOrders().contains(order));
		Hibernate.initialize(customer.getOrders());
	}
	
	@Test
	public void testClassLeverStrategy(){
		Customer customer = (Customer) session.load(Customer.class, 1);
		System.out.println(customer.getClass());
		System.out.println(customer.getCustomerID());
		System.out.println(customer.getCustomerName());
	}
	
}
