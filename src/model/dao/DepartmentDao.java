package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj);
	void update(Department obj); 
	void deleteById(Integer id);
	Department findById(Integer id);//responsavel por pegar o id e consultar no bd o objeto com esse id. Se existir retorna, cado contrário retorna null
	List<Department> findAll();

}
