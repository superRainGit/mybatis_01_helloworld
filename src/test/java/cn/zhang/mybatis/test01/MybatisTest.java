package cn.zhang.mybatis.test01;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import cn.zhang.beans.Employee;
import cn.zhang.dao.EmployeeMapper;
import cn.zhang.dao.EmployeeMapperAnnotation;

/**
 * 1、接口式编程
 * 		原生：	DAO    ======>		DaoImp
 * 		mybatis	Mapper ======>		XXMapper.xml
 * 2、SqlSession代表和数据库的一次会话，用完必须关闭
 * 3、SqlSession和Connection一样它都是非线程安全。每次使用都应该去获取新的对象
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象
 * 		(接口和xml绑定)
 * 	Employee empMapper = sqlSession.getMapper(EmployeeMapper.class)
 * 5、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息；通过该文件将sql语句抽取出来
 * @author zhang
 *
 */
public class MybatisTest {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(MybatisTest.class.getName());
	
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(in);
	}
	/**
	 * 1、根据xml配置文件（全局配置文件）创建一个sqlSessionFactory对象
	 * 		有数据源一些运行环境信息
	 * 2、sql映射文件：配置了每一个sql以及sql的封装规则等
	 * 3、将sql映射文件注册在全局映射文件中
	 * 4、写代码
	 * 		1）根据全局配置文件得到sqlSessionFactory
	 * 		2）使用sqlSession工厂，获取到sqlSession对象 使用它来执行增删改查
	 * 			一次sqlSession就是一次会话
	 * 		3）使用sql的唯一标识告诉mybatis执行哪个sql。sql都是保存在sql映射文件中的
	 * @throws IOException
	 */
	@Test
	public void test_01() throws IOException {
//		String resource = "mybatis-config.xml";
//		InputStream inputStream = Resources.getResourceAsStream(resource);
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee employee = session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 1);
			System.out.println(employee);
		} finally {
			session.close();
		}
	}
	
	@Test
	public void test2() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取一个接口的实现类对象
			// 会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void test3() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapperAnnotation mapper = sqlSession.getMapper(EmployeeMapperAnnotation.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
		} finally {
			sqlSession.close();
		}
	}
	
	/**
	 * 1、mybatis允许增删改直接定义以下返回值类型
	 * 	Integer、Long、Boolean、Void
	 * 		如果对应的增删改操作成功  那么mybatis会返回具体的影响的行数或者true
	 * 		如果操作失败  则会返回零或者false
	 * 2、需要手动提交数据
	 * 		sqlSessionFactory.openSession();--->需要手动提交
	 * 		sqlSessionFactory.openSession(true);--->自动提交
	 * @throws IOException
	 */
	@Test
	public void test4() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		// 注:获取到的SqlSession不会自动提交数据   需要进行手动提交
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			// 测试添加
			Employee ee = new Employee();
			ee.setLastName("Jack33");
			ee.setEmail("Jack33@163.com");
			ee.setGender("1");
			mapper.addEmp(ee);
			System.out.println(ee.getId());
			// 测试修改
//			Employee ee = new Employee(1, "Jack2", "Jack2@sina.com", "1");
//			mapper.updateEmp(ee);
			// 测试删除
//			mapper.deleteEmpById(2);
			// 因为上述获取的SqlSession对象不具有自动提交的功能 所以需要进行自动提交的动作
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}
	
	/**
	 * 测试使用Map以及POJO进行参数的映射
	 * @throws IOException
	 */
	@Test
	public void test5() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			// 获取一个接口的实现类对象
			// 会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee empById = mapper.getEmpByIdAndLastName(1, "Jack2");
			logger.info(empById);
			Map<String, Object> map = new HashMap<>();
			map.put("id", 1);
			map.put("lastName", "Jack2");
			Employee empByMap = mapper.getEmpByMap(map);
			logger.info(empByMap);
		} finally {
			sqlSession.close();
		}
	}
	
	/**
	 * 测试返回List集合的select语句
	 * @throws IOException 
	 */
	@Test
	public void test6() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
//			List<Employee> emps = employeeMapper.getEmps();
//			Map<String, Object> emps = employeeMapper.getEmpByIdWithMap(1);
//			logger.info(emps);
			Map<Integer, Employee> map = employeeMapper.getEmpsReturnMap();
			logger.info(map);
		} finally {
			sqlSession.close();
		}
	}
}
