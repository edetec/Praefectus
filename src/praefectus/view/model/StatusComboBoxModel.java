/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.view.model;

import java.util.LinkedList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import praefectus.model.Status;

/**
 *
 * @author soares
 */
public class StatusComboBoxModel implements ComboBoxModel {
    
    private LinkedList<Status> lista;
    private int selectedItem = 0;
    
    public StatusComboBoxModel(LinkedList<Status> lista) {
        this.lista = lista;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        Status item = (Status) anItem;
                
        selectedItem = lista.indexOf(anItem);
    }
    
    @Override
    public Object getSelectedItem() {
        return lista.get(selectedItem);
    }
    
    @Override
    public int getSize() {
        return lista.size();
    }
    
    @Override
    public Status getElementAt(int index) {
        return lista.get(index);
    }
    
    @Override
    public void addListDataListener(ListDataListener l) {
    }
    
    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
