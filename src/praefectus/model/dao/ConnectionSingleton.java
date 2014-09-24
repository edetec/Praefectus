/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 *
 * @author soares
 */
public abstract class ConnectionSingleton {

    protected static SQLiteDataSource ds;
//URI db;
    private URL db;

    public ConnectionSingleton() {
        if (ds == null) {
            try {

                Class.forName("org.sqlite.JDBC");

                SQLiteConfig config = new SQLiteConfig();
                config.enableFullSync(true);
                config.setReadOnly(false);
                ds = new SQLiteDataSource(config);
                ds.setUrl("jdbc:sqlite::resource:" + getClass().getResource("/praefectus/base/db.sqlite").toString());
            } catch (ClassNotFoundException ex) {
                System.out.println("Erro ao conectar: " + ex.getLocalizedMessage());
            }
            
        }
    }

    public static Connection getConn() {
        try {
            return ds.getConnection();
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar: " + ex.getLocalizedMessage());
        }
        return null;
    }
}
