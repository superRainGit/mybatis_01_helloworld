package cn.zhang.dao;

import org.apache.ibatis.annotations.Select;

import cn.zhang.beans.Employee;

public interface EmployeeMapperAnnotation {

	@Select("SELECT * FROM employee WHERE id = #{id}")
	public Employee getEmpById(Integer id);
}
