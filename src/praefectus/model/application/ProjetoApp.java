/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import praefectus.model.Projeto;
import praefectus.model.dao.ProjetoDao;

/**
 *
 * @author soares
 */
public class ProjetoApp {

    ProjetoDao dao;

    public ProjetoApp() {
        dao = new ProjetoDao();
    }

    public boolean save(Projeto proj) {

        try {
            if (proj.getId() != null && proj.getId() > 0) {
                int n = dao.update(proj);
                System.out.println("id projeto: " + n);
            } else {
                int n = dao.insert(proj);
                System.out.println("id projeto: " + n);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Projeto> list() {
        try {
            return dao.select();
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Projeto>();
        }


    }
    
    public Projeto getById(Projeto proj){
        try {
            proj = dao.getById(proj);
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proj;
    }

    public boolean delete(Projeto proj) {
        boolean result;
        try {
             result = dao.delete(proj)>0;
        } catch (SQLException ex) {
            result = false;
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
