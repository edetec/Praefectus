/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import praefectus.model.Status;

/**
 *
 * @author soares
 */
public class StatusDao extends ConnectionSingleton{
     public LinkedList<Status> selectStatus() throws SQLException {
        LinkedList<Status> listaStatus = new LinkedList<Status>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM status"
                + " ORDER BY idstatus ASC;");

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Status status = new Status();
            status.setId(result.getInt("idstatus"));
            status.setRotulo(result.getString("rotulo"));
            listaStatus.add(status);
        }
        return listaStatus;
    }
}
