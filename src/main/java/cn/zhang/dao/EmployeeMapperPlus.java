package cn.zhang.dao;

import java.util.List;

import cn.zhang.beans.Employee;

public interface EmployeeMapperPlus {

	public Employee getEmpById(Integer id);
	
	public Employee getEmpAndDept(Integer id);
	
	public Employee getEmpByStep(Integer id);
	
	public List<Employee> getEmpsByDeptId(Integer id);
}
