/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.view.model;

import java.awt.Component;
import java.util.LinkedList;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import praefectus.model.Status;

/**
 *
 * @author soares
 */
public class StatusComboBoxEditor extends AbstractCellEditor implements TableCellEditor {

    private JComboBox combo;
    private LinkedList<Status> lista;

    public StatusComboBoxEditor(LinkedList<Status> lista) {
        this.lista = lista;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        combo = new JComboBox(new StatusComboBoxModel(lista));
        combo.setSelectedItem(value); //Deixa o editor pré-selecionado com o valor da célula
        return combo;
    }

    @Override
    public Object getCellEditorValue() {
        return combo.getSelectedItem();
    }
}
