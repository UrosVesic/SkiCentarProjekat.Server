/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.np.sc.server.db;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import rs.ac.bg.fon.np.sc.server.db.util.DBUtil;

/**
 *
 * @author UrosVesic
 */
public class ConnectionPool {

    private PoolDataSource ds;

    public void inicijalizacijaConnectionPoolDataSource() throws Exception {
        ds = PoolDataSourceFactory.getPoolDataSource();
        ds.setConnectionFactoryClassName(DBUtil.getInstance().getConnectionFactoryClassName());
        ds.setURL(DBUtil.getInstance().getUrl());
        ds.setUser(DBUtil.getInstance().getUser());
        ds.setPassword(DBUtil.getInstance().getPassword());

        ds.setInitialPoolSize(30);
        ds.setMaxPoolSize(100);
        ds.setAbandonedConnectionTimeout(5000);
    }

    public Connection uspostaviKonekciju() throws SQLException {
        return ds.getConnection();
    }

}
