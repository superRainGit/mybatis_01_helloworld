package cn.zhang.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import cn.zhang.beans.Employee;

public interface EmployeeMapper {
	
	public Employee getEmpById(Integer id);

	public void addEmp(Employee ee);

	public void updateEmp(Employee ee);

	public void deleteEmpById(Integer id);
	
	public Employee getEmpByIdAndLastName(@Param("id")Integer id, String lastName);
	
	public Employee getEmpByMap(Map<String, Object> map);
	
	public List<Employee> getEmps();
	
	// 返回一条记录的map:key就是列名，value就是对应的值[返回一条记录]
	public Map<String, Object> getEmpByIdWithMap(Integer id);
	
	// 多条记录封装一个map:Map<Integer, Employee>:键是这条记录的主键，值是记录封装后的javaBean
	// 需要使用一个注解(@MapKey):目的是告诉mybatis封装这个Map的时候使用JavaBean的哪个属性作为Map的键
	@MapKey("id")
	public Map<Integer, Employee> getEmpsReturnMap();
}
