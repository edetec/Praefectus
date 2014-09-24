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
import praefectus.model.ItemBacklog;
import praefectus.model.Projeto;
import praefectus.model.dao.BacklogDao;

/**
 *
 * @author soares
 */
public class ProductBacklogApp {

    BacklogDao dao;

    public ProductBacklogApp() {
        this.dao = new BacklogDao();
    }

    public List<ItemBacklog> listaBacklog(Projeto projeto, int filter) {
        try {
            switch (filter) {
                case 0:
                    return dao.selectBacklog(projeto);
                case 1:
                    return dao.selectFinishedBacklog(projeto);
                case 2:
                    return dao.selectAllBacklog(projeto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductBacklogApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<ItemBacklog>();
    }

    public boolean save(ItemBacklog item) {
        try {
            if (item.getIdbacklog() != null && item.getIdbacklog() > 0) {
                int n = dao.update(item);
                System.out.print("Update ");
            } else {
                item.setIdbacklog(dao.insert(item));
                System.out.print("Insert " + item.getIdbacklog());
            }
            System.out.println("id backlog: " + item.getIdbacklog());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductBacklogApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean excluir(ItemBacklog item) {
        try {
            if (item.getIdbacklog() != null && item.getIdbacklog() > 0) {
                return dao.delete(item) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductBacklogApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean update(ItemBacklog item) {
        try {
            if (item.getIdbacklog() != null && item.getIdbacklog() > 0) {
                return dao.update(item) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductBacklogApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
