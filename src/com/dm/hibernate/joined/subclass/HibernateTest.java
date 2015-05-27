package com.dm.hibernate.joined.subclass;

import java.util.List;

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
	public void testQuery(){
		List<Person> persons = session.createQuery("from Person").list();
		System.out.println(persons.size());
	}
	
	@Test
	public void testSubClass(){
		Person person = new Person();
		person.setAge(25);
		person.setName("AA");
		session.save(person);
		
		Student student = new Student();
		student.setAge(20);
		student.setName("BB");
		student.setSchool("YSXY");
		session.save(student);
	}
}
