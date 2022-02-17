package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj);
	void update(Seller obj); 
	void deleteById(Integer id);
	Seller findById(Integer id);//responsavel por pegar o id e consultar no bd o objeto com esse id. Se existir retorna, cado contr�rio retorna null
	List<Seller> findAll();

}