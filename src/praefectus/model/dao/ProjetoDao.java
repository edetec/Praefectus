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

/**
 *
 * @author soares
 */
public class ProjetoDao extends ConnectionSingleton{
    
    public int insert(Projeto proj) throws SQLException{
        PreparedStatement statment = getConn().prepareStatement("INSERT INTO projeto(nome, descricao, inicio,fim,duracao_sprint,horas_sprint) VALUES(?,?,?,?,?,?);");
        
        statment.setString(1, proj.getNome());
        statment.setString(2, proj.getDescricao().toString());
        statment.setDate(3, new Date(proj.getInicio().getTime()));
        statment.setDate(4, new Date(proj.getFim().getTime()));
        statment.setInt(5, proj.getDuracaoSprint());
        statment.setInt(6, proj.getPontosSprint());
        return statment.executeUpdate();
    }
    
    public List<Projeto> select() throws SQLException{
        List<Projeto> projetos= new ArrayList<Projeto>();
        PreparedStatement statement = getConn().prepareStatement("SELECT * FROM projeto LIMIT ?;");
        statement.setInt(1, 20);
        
        ResultSet result = statement.executeQuery();
        while(result.next()){
            Projeto proj = new Projeto();
            proj.setId(result.getLong("idprojeto"));
            proj.setNome(result.getString("nome"));
            proj.setDescricao(new StringBuffer(result.getString("descricao")));
            proj.setInicio(new java.util.Date(result.getDate("inicio").getTime()));
            proj.setFim(new java.util.Date(result.getDate("fim").getTime()));
            proj.setDuracaoSprint(result.getInt("duracao_sprint"));
            proj.setPontosSprint(result.getInt("horas_sprint"));
            projetos.add(proj);
        }
        return projetos;
    }

    public Projeto getById(Projeto proj) throws SQLException {
         PreparedStatement statement = getConn().prepareStatement("SELECT * FROM projeto where id = ?;");
        statement.setLong(1,proj.getId());
        
        ResultSet result = statement.executeQuery();
        if(result.next()){
            proj.setId(result.getLong("id"));
            proj.setNome(result.getString("name"));
            proj.setDescricao(new StringBuffer(result.getString("description")));
            proj.setInicio(new java.util.Date(result.getDate("start").getTime()));
        }else{
            proj = null;
        }
        return proj;
    }

    public int update(Projeto proj) throws SQLException {
        PreparedStatement statment = getConn().prepareStatement("UPDATE projeto SET nome=?, descricao=?, inicio=?,fim=?,duracao_sprint=?,horas_sprint=? WHERE idprojeto=?;");
        
        statment.setString(1, proj.getNome());
        statment.setString(2, proj.getDescricao().toString());
        statment.setDate(3, new Date(proj.getInicio().getTime()));
        statment.setDate(4, new Date(proj.getFim().getTime()));
        statment.setInt(5, proj.getDuracaoSprint());
        statment.setInt(6, proj.getPontosSprint());
        statment.setLong(7,proj.getId());
        return statment.executeUpdate();
    }

    public int delete(Projeto proj) throws SQLException {
         PreparedStatement statment = getConn().prepareStatement("DELETE from projeto WHERE idprojeto=?;");
        statment.setLong(1,proj.getId());
        return statment.executeUpdate();
    }
    
}
