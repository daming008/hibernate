package com.dm.hibernate.entities.HQL;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.mapping.DependantValue;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dm.hibernate.DAO.DepartmentDAO;

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
	public void testBench(){
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				System.out.println(con);
				System.out.println(con);
				System.out.println(con);
			}
		});
	}
	
	@Test
	public void testDepaermentManage(){
		DepartmentDAO departmentDAO = new DepartmentDAO();
		Department dept = new Department();
		dept.setName("LDM");
		departmentDAO.save(dept);
		departmentDAO.save(dept);
		departmentDAO.save(dept);
	}
	
	@Test
	public void testQueryCache(){
		Query query = session.createQuery("FROM Employee");
		query.setCacheable(true);
		List<Employee> emp1 = query.list();
		System.out.println(emp1.size());
		//设置开启
		emp1 = query.list();
		System.out.println(emp1.size());
		
	}
	@Test
	public void testCollectionLevelCache(){
		Department dept1 = (Department) session.get(Department.class, 80);
		System.out.println(dept1);
		System.out.println(dept1.getEmps().size());
		
		transaction.commit();
		session.close();
		
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
		Department dept2 = (Department) session.get(Department.class, 80);
		System.out.println(dept2);
		System.out.println(dept2.getEmps().size());
		
	}
	@Test
	public void testSecondLevelCache(){
		Employee emp1 = (Employee) session.get(Employee.class, 100);
		System.out.println(emp1.getName());
		
		transaction.commit();
		session.close();
		
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		
		Employee emp2 = (Employee) session.get(Employee.class, 100);
		System.out.println(emp2.getName());
	}
	
	@Test
	public void testNativeSQL(){//本地SQL
		String sql ="INSERT into gg_department values(?,?)";
		Query query = session.createSQLQuery(sql);
		query.setInteger(0, 280).setString(1, "Lideing").executeUpdate();
	}
	
	@Test
	public void testQBC(){
		//创建一个Criteria对象
		Criteria criteria = session.createCriteria(Employee.class);
//		criteria.add(Restrictions.eq("email", "HBAER"));
		criteria.add(Restrictions.gt("salary", 5000F));
		
		List<Employee> employees = criteria.list();
		System.out.println(employees);
	}
	
	@Test
	public void testLeftJoinFetch2(){
		String hql = "select e from Employee e inner join e.dept";
		Query query = session.createQuery(hql);
		List<Employee> emps = query.list();
		int i=0;
		for(Employee emp : emps){
			System.out.println(emp.getName()+"::"+emp.getDept().getName()+":::"+i);
			i++;
			
		}
	}
	
	@Test
	public void testLeftJoinFetch(){
//		String hql = "SELECT DISTINCT d FROM Department d left join fetch d.emps";
		String hql = "FROM Department d inner join fetch d.emps";
		List<Department> depts = session.createQuery(hql).list();
		depts = new ArrayList<>(new LinkedHashSet(depts));
		for(Department dep:depts){
			System.out.println(dep.getName()+":"+dep.getEmps().size());
		}
	}
	
	@Test
	public void testReport(){
		String hql = "SELECT min(e.salary),max(e.salary) from Employee e where e.salary > :salary "
				+ "GROUP BY e.dept";
		List<Object[]> result = session.createQuery(hql).setFloat("salary", 8000).list();
		for(Object[] objs : result){
			System.out.println(Arrays.asList(objs));
		}
	}
	
	@Test
	public void testShadow(){
		Query query = session.createQuery("SELECT new Employee(e.email,e.salary,e.dept) FROM "
				+ "Employee e where e.salary > :salary and e.dept=:dept");
		Department dept = new Department();
		dept.setId(80);
		List<Employee> result = query.setEntity("dept", dept).setFloat("salary", 8000).list();
		
		for(Employee emp: result){
			System.out.println(emp.getId()+"::"+emp.getEmail()+"::"+emp.getSalary()+"::"+emp.getDept());
		}
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
