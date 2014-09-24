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
import praefectus.model.Projeto;
import praefectus.model.Sprint;

/**
 *
 * @author soares
 */
public class SprintDao extends ConnectionSingleton {
    
    public List<Sprint> selectSprints(Projeto projeto) throws SQLException {
        List<Sprint> sprints = new ArrayList<Sprint>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM sprint where id_projeto = ? ORDER BY inicio DESC;");
        statement.setLong(1, projeto.getId());
        
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Sprint sprint = new Sprint(projeto);
            sprint.setId(result.getLong("idsprint"));
            sprint.setProjeto(projeto);
            sprint.setInicio(result.getDate("inicio"));
            sprint.setFim(result.getDate("fim"));
            sprint.setReview(new StringBuffer(result.getString("review")== null?"":result.getString("review")));
            
            sprints.add(sprint);
        }
        return sprints;
    }
    
    public int update(Sprint sprint) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("UPDATE sprint SET inicio=?,fim=?,review=? WHERE idsprint = ?;");
        statement.setDate(1, new Date(sprint.getInicio().getTime()));
        statement.setDate(2, new Date(sprint.getFim().getTime()));
        statement.setString(3, sprint.getReview().toString());
        statement.setLong(4, sprint.getId());
        
        return statement.executeUpdate();
    }
    
    public Long insert(Sprint sprint) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("INSERT INTO sprint(id_projeto,inicio,fim) "
                + "VALUES(?,?,?);");
        statement.setLong(1, sprint.getProjeto().getId());
        statement.setDate(2, new Date(sprint.getInicio().getTime()));
        statement.setDate(3, new Date(sprint.getFim().getTime()));
        
        if(statement.executeUpdate()>0){
            ResultSet last = statement.getGeneratedKeys();  
            return last.getLong(1);
        }
        return null;
    }

    public int delete(Sprint sprint) throws SQLException {
        PreparedStatement statement = getConn().prepareStatement("DELETE FROM sprint WHERE idsprint = ?;");
        statement.setLong(1, sprint.getId());

        return statement.executeUpdate();
    }
}
