package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	//Classe auxiliar para instanciar os meus DAO's
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
		//dessa forma a classe vai expor um método que retorna o tipo da interface, mas internamente vai instaciar uma implementação
		//Isso é um macete para não precisar expor a implementação, somente a interface.
	}

}
