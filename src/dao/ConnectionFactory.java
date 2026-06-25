package dao;

import java.sql.Statement; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe para gerar conexões com o banco de dados para as classes DAO
public class ConnectionFactory {
	private static final String url = "jdbc:sqlite:database.db";
	
	public static Connection getConexao() {
		try {
			// Cria a conexão com o arquivo SQLite
			Connection conn = DriverManager.getConnection(url);
			
			// Ativa as chaves estrangeiras e o ON DELETE CASCADE para a conexão
			try (Statement stmt = conn.createStatement()){
				stmt.execute("PRAGMA foreign_keys = ON");
			}
			
			return conn;
		} catch (SQLException e) {
			System.out.println("Erro: Falha ao conectar com o banco de dados!");
			throw new RuntimeException(e);
		}
	}
}
