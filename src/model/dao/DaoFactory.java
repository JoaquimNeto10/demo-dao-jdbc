package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	//Classe auxiliar para instanciar os meus DAO's
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
		//dessa forma a classe vai expor um m�todo que retorna o tipo da interface, mas internamente vai instaciar uma implementa��o
		//Isso � um macete para n�o precisar expor a implementa��o, somente a interface.
	}

}
