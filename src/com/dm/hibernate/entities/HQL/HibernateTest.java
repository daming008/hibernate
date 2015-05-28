package com.dm.hibernate.entities.HQL;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.DependantValue;
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
	public void testFieldQuery(){
		Query query = session.createQuery("SELECT e.email,e.salary,e.dept FROM Employee e where e.dept=:dept");
		Department dept = new Department();
		dept.setId(80);
		List<Object[]> result = query.setEntity("dept", dept).list();
		for(Object[] objs: result){
			System.out.println(Arrays.asList(objs));
		}
	}
	
	@Test
	public void testNamedQuery(){
		Query query = session.getNamedQuery("salQuery");
		List<Employee> employees = 
				query.setFloat("minSal", 5000)
					 .setFloat("maxSal", 10000)
					 .list();
		
		System.out.println(employees.size());
	}
	
	@Test
	public void testPageQuery(){
		String hql = "FROM Employee";
		Query query = session.createQuery(hql);
		int pageNo = 30;
		int pageSize = 5;
		
		List<Employee> employees =
		query.setFirstResult((pageNo - 1) * pageSize)
			 .setMaxResults(pageSize)
			 .list();
		
		System.out.println(employees);
	}
	
	@Test
	public void testHQLNamedPrarm(){
		String hql = "from Employee e where e.salary > :sal and e.email like :email";
		Query createQuery = session.createQuery(hql);
		createQuery.setFloat("sal", 7000)
				   .setString("email", "%A%");
		
		List<Employee> employees = createQuery.list();
		
		System.out.println(employees.size());
	}
	@Test
	public void testHQL(){
		String hql = "from Employee e where e.salary > ? and e.email like ?";
		Query createQuery = session.createQuery(hql);
		createQuery.setFloat(0, 6000)
		.setString(1, "%A%");
		
		List<Employee> employees = createQuery.list();
		
		System.out.println(employees.size());
	}
	
}
