package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbExceptions;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement("INSERT INTO seller "
					+ "		(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "		VALUES "
					+ "		(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);//retorna o id do novo vendedor inserido
			
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = pst.executeUpdate();
			
			if(rowsAffected > 0) {//se for maior que 0, significa que inseriu
				ResultSet rs = pst.getGeneratedKeys();
				if(rs.next()) {//se existir, pego o id gerado e atribuo dentro do objeto obj, assim ele ja fica populado com o novo id dele
					int id = rs.getInt(1);
					obj.setId(id);
				}
				
				DB.closeResultSet(rs);
				
			} else {
				throw new DbExceptions("Erro inesperado! Nenhuma linha foi alterada!");
			}
		} catch (SQLException e) {
			throw new DbExceptions(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement pst = null;
		
		try {
			pst = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			pst.setInt(6, obj.getId());
			
			pst.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbExceptions(e.getMessage());
		} finally {
			DB.closeStatement(pst);
		}		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			pst.setInt(1, id);
			rs = pst.executeQuery();
			
			if(rs.next()) {
				Department dep = instatiateDepartment(rs);
				Seller obj = instatiateSeller(rs, dep);
				return obj;
				
			}
			
			return null;
			
		} catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		} finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

	private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instatiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setNome(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {//mesma coisa do findByDepartment porém sem a restrição do where
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = pst.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//criei um map vazio e vou guardar dentro dele qualquer departamento que eu instanciar
			
			while (rs.next()) {//cada vez que passar aqui, terei que testar se o departamento já existe
				
				Department dep = map.get(rs.getInt("DepartmentId"));//busco o departamento aqui, se não existir retorna nulo
				
				if(dep == null) {
					dep = instatiateDepartment(rs);//caso não exista o departamento, instancio ele aqui
					map.put(rs.getInt("DepartmentId"), dep);//salvo o departamento dentro do map para que na próxima vez eu possa ver se existe ou não
				}
				
				Seller obj = instatiateSeller(rs, dep);
				list.add(obj);				
			}			
			return list;
			
		} catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		} finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			pst.setInt(1, department.getId());
			rs = pst.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//criei um map vazio e vou guardar dentro dele qualquer departamento que eu instanciar
			
			while (rs.next()) {//cada vez que passar aqui, terei que testar se o departamento já existe
				
				Department dep = map.get(rs.getInt("DepartmentId"));//busco o departamento aqui, se não existir retorna nulo
				
				if(dep == null) {
					dep = instatiateDepartment(rs);//caso não exista o departamento, instancio ele aqui
					map.put(rs.getInt("DepartmentId"), dep);//salvo o departamento dentro do map para que na próxima vez eu possa ver se existe ou não
				}
				
				Seller obj = instatiateSeller(rs, dep);
				list.add(obj);				
			}			
			return list;
			
		} catch(SQLException e) {
			throw new DbExceptions(e.getMessage());
		} finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
	}

}
