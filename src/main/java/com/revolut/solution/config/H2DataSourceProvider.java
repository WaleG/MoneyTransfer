package com.revolut.solution.config;

import org.h2.jdbcx.JdbcConnectionPool;

import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
public class H2DataSourceProvider implements Provider<DataSource> {
    private DataSource dataSource;

    @Override
    public DataSource get() {
        if (dataSource == null) {
            dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test", "", "");
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = dataSource.getConnection();
                stmt = conn.createStatement();
                stmt.execute("CREATE TABLE account (accountId VARCHAR PRIMARY KEY, balance DECIMAL)");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }// do nothing
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return dataSource;
    }
}
