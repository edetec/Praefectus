/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.view.model;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import praefectus.model.Sprint;
import praefectus.util.DateLocalFormater;

/**
 *
 * @author soares
 */
public class SprintTableModel extends AbstractTableModel {

    private List<Sprint> sprints;
    private List<String> colunas;

    public SprintTableModel(List<Sprint> sprints) {
        this.sprints = sprints;
        this.colunas = Arrays.asList("Indice", "Início","Fim");
    }

    public String getColumnName(int i) {
        return this.colunas.get(i);
    }

    @Override
    public int getRowCount() {
        return sprints.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Sprint sprint = sprints.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return sprints.size() - rowIndex;
            case 1:
                return DateLocalFormater.dateToString(sprint.getInicio());
            case 2:
                return DateLocalFormater.dateToString(sprint.getFim());
        }
        return null;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setProjetos(List<Sprint> sprints) {
        this.sprints = sprints;
        super.fireTableDataChanged();
    }

    public List<String> getColunas() {
        return colunas;
    }
}
