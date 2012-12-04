/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.view.model;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import praefectus.model.ItemBacklogSprint;
import praefectus.model.Status;

/**
 *
 * @author soares
 */
public class BacklogSprintTableModel extends AbstractTableModel {

    private List<ItemBacklogSprint> itens;
    private List<String> colunas;

    public BacklogSprintTableModel(List<ItemBacklogSprint> sprints) {
        this.itens = sprints;
        this.colunas = Arrays.asList("Prioridate", "Estimativa", "Status", "Estoria");
    }

    public String getColumnName(int i) {
        return this.colunas.get(i);
    }

    @Override
    public int getRowCount() {
        return itens.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemBacklogSprint item = itens.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getItem().getPrioridade();
            case 1:
                return item.getItem().getEstimativa();
            case 2:
                return item.getStatus();
            case 3:
                return item.getItem().getEstoria();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return Integer.class;
            case 2:
                return Status.class;
            case 3:
                return StringBuffer.class;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ItemBacklogSprint item = itens.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.getItem().setPrioridade((int) aValue);
                break;
            case 1:
                item.getItem().setEstimativa((int) aValue);
                break;
            case 2:
                item.setStatus((Status) aValue);
                break;
            case 3:
                item.getItem().setEstoria((StringBuffer) aValue);
        }
    }

    public List<ItemBacklogSprint> getItensBacklogSprint() {
        return itens;
    }

    public void setItensBacklogSprint(List<ItemBacklogSprint> itens) {
        this.itens = itens;
        super.fireTableDataChanged();
    }

    public List<String> getColunas() {
        return colunas;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            return true;
        }

        return false;
    }
}
