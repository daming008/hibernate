package com.dm.hibernate.DAO;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.dm.hibernate.entities.HQL.Department;

public class DepartmentManage {
	@Test
	public void test() {
		DepartmentDAO departmentDAO = new DepartmentDAO();
		Department dept = new Department();
		dept.setName("LDM");
		
		Session session = HibernateUtil.getInstance().getSession();
		Transaction transaction = session.beginTransaction();
		departmentDAO.save(dept);
		departmentDAO.save(dept);
		departmentDAO.save(dept);
		transaction.commit();
		System.out.println(session.isOpen());
	}
}
