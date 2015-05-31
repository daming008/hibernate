package com.dm.hibernate.DAO;

import org.hibernate.Session;

import com.dm.hibernate.entities.HQL.Department;

public class DepartmentDAO {
	public void save(Department dept){
		Session session = HibernateUtil.getInstance().getSession();
		System.out.println(session.hashCode());
		session.save(dept);
	}
}
