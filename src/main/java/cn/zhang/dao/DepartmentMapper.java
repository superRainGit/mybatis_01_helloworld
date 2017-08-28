package cn.zhang.dao;

import cn.zhang.beans.Department;

public interface DepartmentMapper {

	public Department getDeptById(Integer id);
	
	public Department getDeptByIdPlus(Integer id);
	
	public Department getDeptByIdByStep(Integer id);
}
