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
import praefectus.model.Sprint;
import praefectus.model.dao.SprintDao;

/**
 *
 * @author soares
 */
public class SprintApp {
    
    private SprintDao daoSprint;
    
    public SprintApp() {
        daoSprint = new SprintDao();
    }
    
    public List<Sprint> listaSprints(Projeto projeto) {
        try {
            return daoSprint.selectSprints(projeto);
        } catch (SQLException ex) {
            Logger.getLogger(SprintApp.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Sprint>();
        }
    }
    
    public boolean salvar(Sprint sprint) {
        try {
            if (sprint.getId() != null && sprint.getId() > 0) {
                daoSprint.update(sprint);
                System.out.println("id sprint: "  + sprint.getId());
            } else {
                sprint.setId(daoSprint.insert(sprint));
                System.out.println("id sprint: " + sprint.getId());
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
