/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import praefectus.model.ItemBacklog;
import praefectus.model.Projeto;

/**
 *
 * @author soares
 */
public class BacklogDao extends ConnectionSingleton {

    public List<ItemBacklog> selectBacklog(Projeto projeto) throws SQLException {
        List<ItemBacklog> itens = new ArrayList<ItemBacklog>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM backlog where id_projeto = ? and idbacklog NOT IN(SELECT id_backlog FROM sprint_backlog) ORDER BY prioridade DESC;");
        
        statement.setLong(1, projeto.getId());

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            itens.add(getItem(projeto, result));
        }
        return itens;
    }
    
    public List<ItemBacklog> selectAllBacklog(Projeto projeto) throws SQLException {
        List<ItemBacklog> itens = new ArrayList<ItemBacklog>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM backlog where id_projeto = ? ORDER BY prioridade DESC;");
        
        statement.setLong(1, projeto.getId());

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            itens.add(getItem(projeto, result));
        }
        return itens;
    }
    
    public List<ItemBacklog> selectFinishedBacklog(Projeto projeto) throws SQLException {
        List<ItemBacklog> itens = new ArrayList<ItemBacklog>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM backlog "
                                                                                                + "join sprint_backlog on idbacklog = id_backlog "
                                                                                                + "where id_projeto = ? and status = 3 "
                                                                                                + "ORDER BY prioridade DESC;");
        
        statement.setLong(1, projeto.getId());

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            itens.add(getItem(projeto, result));
        }
        return itens;
    }

    public int update(ItemBacklog item) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("UPDATE backlog SET id_projeto = ?,prioridade=?,estimativa=?,estoria=?,cancelado=? WHERE idbacklog = ?;");
        statement.setLong(1, item.getProjeto().getId());
        statement.setInt(2, item.getPrioridade());
        statement.setInt(3, item.getEstimativa());
        statement.setString(4, item.getEstoria().toString());
        statement.setBoolean(5, item.isCancelado());
        statement.setLong(6, item.getIdbacklog());

        return statement.executeUpdate();
    }

    public Long insert(ItemBacklog item) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO backlog(id_projeto,prioridade,estimativa,estoria,cancelado,data_criacao) "
                + "VALUES(?,?,?,?,?,?);");
        statement.setLong(1, item.getProjeto().getId());
        statement.setInt(2, item.getPrioridade());
        statement.setInt(3, item.getEstimativa());
        statement.setString(4, item.getEstoria().toString());
        statement.setBoolean(5, item.isCancelado());
        statement.setDate(6, new Date(Calendar.getInstance().getTime().getTime()));
        if (statement.executeUpdate() > 0) {
            ResultSet last = statement.getGeneratedKeys();
            return last.getLong(1);
        }
        return null;
    }

    public int delete(ItemBacklog item) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM backlog  WHERE idbacklog = ?;");
        statement.setLong(1, item.getIdbacklog());

        return statement.executeUpdate();
    }

    private ItemBacklog getItem(Projeto projeto, ResultSet result) throws SQLException {
        ItemBacklog item = new ItemBacklog(projeto);
        item.setIdbacklog(result.getLong("idbacklog"));
        item.setPrioridade(result.getInt("prioridade"));
        item.setEstimativa(result.getInt("estimativa"));
        item.setEstoria(new StringBuffer(result.getString("estoria")));
        item.setData_criacao(result.getDate("data_criacao"));
        item.setCancelado(result.getBoolean("cancelado"));
        return item;
    }
}
