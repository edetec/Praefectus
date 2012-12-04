/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import praefectus.model.ItemBacklogSprint;
import praefectus.model.Sprint;
import praefectus.model.Status;
import praefectus.model.dao.BacklogSprintDao;
import praefectus.model.dao.StatusDao;

/**
 *
 * @author soares
 */
public class BacklogSprintApp {
    
    private BacklogSprintDao daoSprintBacklog;
    private StatusDao daoStatus;
    
    public BacklogSprintApp() {
        daoSprintBacklog = new BacklogSprintDao();
        daoStatus = new StatusDao();
    }

    public List<ItemBacklogSprint> listaBacklogSprint(Sprint sprint) {
        try {
            return daoSprintBacklog.selectBacklogSprint(sprint);
        } catch (SQLException ex) {
            Logger.getLogger(BacklogSprintApp.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<ItemBacklogSprint>();
        }
    }

    public List<ItemBacklogSprint> listaBurnDownSprint(Sprint sprint) {
        List<ItemBacklogSprint> itensSprint = new ArrayList<>(); 
        try {
            itensSprint =  daoSprintBacklog.selectBacklogSprint(sprint);
            
        } catch (SQLException ex) {
            Logger.getLogger(BacklogSprintApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return itensSprint;
    }
    
    public boolean salvar(ItemBacklogSprint item) {
        try {
            if (item.getId() != null && item.getId() > 0) {
                daoSprintBacklog.update(item);
                System.out.println("id backlog sprint: "  + item.getId());
            } else {
                item.setId(daoSprintBacklog.insert(item));
                System.out.println("id backlog sprint: " + item.getId());
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean deletar(ItemBacklogSprint item){
        try {
                daoSprintBacklog.delete(item);
                System.out.println("id backlog sprint: "  + item.getId());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProjetoApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public LinkedList<Status> listaStatus() {
         try {
            return daoStatus.selectStatus();
        } catch (SQLException ex) {
            Logger.getLogger(BacklogSprintApp.class.getName()).log(Level.SEVERE, null, ex);
            return new LinkedList<Status>();
        }
    }
}
