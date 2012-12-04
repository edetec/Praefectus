/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.view.model;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import praefectus.model.ItemBacklog;
import praefectus.model.Projeto;

/**
 *
 * @author soares
 */
public class BacklogTableModel extends AbstractTableModel {

    private List<ItemBacklog> itensBacklog;
    private List<String> colunas;
    private Projeto projeto;
    private boolean editavel;

    public BacklogTableModel(Projeto projeto, List<ItemBacklog> itens) {
        this.itensBacklog = itens;
        this.projeto = projeto;
        this.colunas = Arrays.asList("Prioridade", "Estimativa", "Cancelado", "Estória de usuário");
        this.editavel = true;
    }

    public String getColumnName(int i) {
        return this.colunas.get(i);
    }

    @Override
    public int getRowCount() {
        return itensBacklog.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemBacklog item = itensBacklog.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getPrioridade();
            case 1:
                return item.getEstimativa();
            case 2:
                return item.isCancelado();
            case 3:
                return item.getEstoria().toString();
            default:
                return null;
        }
    }

    public List<ItemBacklog> getItemBacklog() {
        return itensBacklog;
    }

    public void setItemBacklogs(List<ItemBacklog> itens) {
        this.itensBacklog = itens;
        super.fireTableDataChanged();
    }

    public List<String> getColunas() {
        return colunas;
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editavel;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ItemBacklog item = itensBacklog.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setPrioridade(Integer.parseInt(aValue.toString()));
                break;
            case 1:
                item.setEstimativa(Integer.parseInt(aValue.toString()));
                break;
            case 2:
                item.setCancelado(Boolean.parseBoolean(aValue.toString()));
                break;
            case 3:
                item.setEstoria(new StringBuffer(aValue.toString()));
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
                return Boolean.class;
            case 3:
                return StringBuffer.class;
            default:
                return null;
        }
    }

    public boolean hasEmptyRow() {
        if (itensBacklog.isEmpty()) {
            return false;
        }
        ItemBacklog item = itensBacklog.get(itensBacklog.size() - 1);
        if (item.getEstimativa() < 1 && item.getEstoria().toString().trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void addEmptyRow() {
        itensBacklog.add(new ItemBacklog(projeto));
        fireTableRowsInserted(
                itensBacklog.size() - 1,
                itensBacklog.size() - 1);
    }

    public void removeRow(int row) {
        itensBacklog.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
