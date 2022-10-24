package ru.reosfire.temporarywhitelist.Lib.Sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
	private final ISqlConfiguration configuration;

	public SqlConnection(ISqlConfiguration config) {
		configuration = config;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(configuration.getConnectionString(), configuration.getUser(), configuration.getPassword());
	}
}