package cn.zhang.mybatis.test02;

import org.apache.log4j.Logger;
import org.junit.Test;

import cn.zhang.beans.Department;
import cn.zhang.beans.Employee;
import cn.zhang.dao.DepartmentMapper;
import cn.zhang.dao.EmployeeMapperPlus;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.LogManager;

public class MybatisTest {
	private static final Logger logger = LogManager.getLogger(MybatisTest.class.getName());

	public static SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-configPlus.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}
	
	@Test
	public void test01() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		try {
			EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
//			Employee emp = mapper.getEmpById(1);
//			Employee emp = mapper.getEmpAndDept(1);
			// 使用懒加载示例
			Employee emp = mapper.getEmpByStep(4);
			// 当没有使用到联合对象的时候  sql语句只发需要的那一条
			logger.info(emp);// 这条语句打印的时候   只会向数据库发送一条数据
//			logger.info(emp);// 这条语句执行的时候  才会发送第二条需要的sql语句
		} finally {
			session.close();
		}
	}
	
	@Test
	public void test02() throws IOException {
		SqlSessionFactory sqlSesionFactory = getSqlSessionFactory();
		SqlSession session = sqlSesionFactory.openSession();
		try {
			DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
//			Department dept = mapper.getDeptByIdPlus(1);
			Department dept = mapper.getDeptByIdByStep(1);
			logger.info(dept.getDeptName());
			logger.info(dept.getEmps());
		} finally {
			session.close();
		}
	}
}
