package com.dm.hibernate.helloword;

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
	public void testINsertData() {
		News news = new News("java", "ldm", new Date());
		session.save(news);
	}
	
	@Test
	public void testSessionCash(){
		News news1 = (News) session.get(News.class, 1);
		System.out.println(news1);
		News news2 = (News) session.get(News.class, 1);
		System.out.println(news2);
	}

	/**
	 * flush: 使数据表中的记录和 Session 缓存中的对象的状态保持一致. 为了保持一致, 则可能会发送对应的 SQL 语句.
	 * 1. 在 Transaction 的 commit() 方法中: 先调用 session 的 flush 方法, 再提交事务
	 * 2. flush() 方法会可能会发送 SQL 语句, 但不会提交事务. 
	 * 3. 注意: 在未提交事务或显式的调用 session.flush() 方法之前, 也有可能会进行 flush() 操作.
	 * 1). 执行 HQL 或 QBC 查询, 会先进行 flush() 操作, 以得到数据表的最新的记录
	 * 2). 若记录的 ID 是由底层数据库使用自增的方式生成的, 则在调用 save() 方法时, 就会立即发送 INSERT 语句. 
	 * 因为 save 方法后, 必须保证对象的 ID 是存在的!
	 */
	@Test
	public void testSessionFlush(){
		News news1 = (News) session.get(News.class, 1);
		news1.setAuthor("ldm");
	}
	
	@Test
	public void testSessionRefresh(){
		News news = (News) session.get(News.class, 1);
		System.out.println(news);
		session.refresh(news);
		System.out.println(news);
	}
}
