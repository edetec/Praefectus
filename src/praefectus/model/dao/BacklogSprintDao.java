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
import java.util.List;
import praefectus.model.ItemBacklog;
import praefectus.model.ItemBacklogSprint;
import praefectus.model.Projeto;
import praefectus.model.Sprint;
import praefectus.model.Status;

/**
 *
 * @author soares
 */
public class BacklogSprintDao extends ConnectionSingleton {

    public List<ItemBacklogSprint> selectBacklogSprint(Sprint sprint) throws SQLException {
        List<ItemBacklogSprint> itens = new ArrayList<ItemBacklogSprint>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM sprint_backlog"
                + " join backlog on id_backlog  = idbacklog"
                + " join status on status  = idstatus"
                + " where id_sprint  = ? ORDER BY status ASC;");
        statement.setLong(1, sprint.getId());

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            ItemBacklogSprint item = new ItemBacklogSprint();
            item.setId(result.getLong("idsprint_backlog"));
            item.setSprint(sprint);
            item.setItem(getItemBacklog(sprint.getProjeto(), result));
            item.setStatus(getStatus(result));
            
            if (result.getDate("data_concluido") != null) {
                item.setDataConclusao(new java.util.Date(result.getDate("data_concluido").getTime()));
            }

            itens.add(item);
        }
        return itens;
    }

    public int update(ItemBacklogSprint item) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("UPDATE sprint_backlog"
                + " SET id_sprint =?,id_backlog = ?,status=?,data_concluido=?"
                + " WHERE idsprint_backlog = ?;");
        statement.setLong(1, item.getSprint().getId());
        statement.setLong(2, item.getItem().getIdbacklog());
        statement.setInt(3, item.getStatus().getId());
        statement.setDate(4, new Date(item.getDataConclusao().getTime()));
        statement.setLong(5, item.getId());

        return statement.executeUpdate();
    }

    public Long insert(ItemBacklogSprint item) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO sprint_backlog(id_sprint,id_backlog,status) "
                + "VALUES(?,?,?);");
        statement.setLong(1, item.getSprint().getId());
        statement.setLong(2, item.getItem().getIdbacklog());
        statement.setInt(3, item.getStatus().getId());

        if (statement.executeUpdate() > 0) {
            ResultSet last = statement.getGeneratedKeys();
            return last.getLong(1);
        }
        return null;
    }

    public int delete(ItemBacklogSprint item) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM sprint_backlog  WHERE idsprint_backlog = ?;");
        statement.setLong(1, item.getId());

        return statement.executeUpdate();
    }

    private ItemBacklog getItemBacklog(Projeto projeto, ResultSet result) throws SQLException {
        ItemBacklog item = new ItemBacklog(projeto);
        item.setIdbacklog(result.getLong("idbacklog"));
        item.setPrioridade(result.getInt("prioridade"));
        item.setEstimativa(result.getInt("estimativa"));
        item.setEstoria(new StringBuffer(result.getString("estoria")));
        item.setData_criacao(result.getDate("data_criacao"));
        item.setCancelado(result.getBoolean("cancelado"));
        return item;
    }

    private Status getStatus(ResultSet result) throws SQLException {
        Status status = new Status();
        status.setId(result.getInt("status"));
        status.setRotulo(result.getString("rotulo"));
        return status;
    }
}
