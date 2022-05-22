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

    /**
     * Objekat koji upravlja connection pool-om. Kreira konekcije i upravlja
     * njima.
     */
    private PoolDataSource ds;

    /**
     * Inicijalizuje PoolDataSource i postavlja parametre za konekciju sa bazom
     *
     * @throws Exception ako dodje do greske prilikom postavljanja vrednosti u PoolDateSource objekat
     */
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
    /**
     * Vraca konekciju iz connection pool-a
     * @return Konekciju kao objekat klase Connection
     * @throws SQLException ako PoolDataSource ne moze da vrati konekciju
     */
    public Connection vratiKonekciju() throws SQLException {
        return ds.getConnection();
    }

}
