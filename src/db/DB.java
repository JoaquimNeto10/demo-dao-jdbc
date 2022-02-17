package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		if (conn == null) {
			try {				
				Properties props = loadProperties();//peguei as propriedades
				String url = props.getProperty("dburl");//passei a url informada no db.properties				
				conn = DriverManager.getConnection(url, props);//conectei no banco de dados(instanciei um objeto do tipo connection)
			} catch(SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
		return conn;
	}
	
	public static Connection closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
		return conn;
	}

	private static Properties loadProperties() {// método para carregar as informmações do db.properties
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);//load = leitura do arquivo properites apontado pelo InputStream fs e vai guardar os dados dentro do objeto props.
			return props;
		} catch(IOException e) {
			throw new DbExceptions(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch(SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
		
	}
	
	public static void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch(SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
		
	}

}
