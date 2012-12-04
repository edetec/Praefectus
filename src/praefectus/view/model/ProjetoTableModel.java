/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.view.model;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import praefectus.model.Projeto;
import praefectus.util.DateLocalFormater;

/**
 *
 * @author soares
 */
public class ProjetoTableModel extends AbstractTableModel {

    private List<Projeto> projetos;
    private List<String> colunas;

    public ProjetoTableModel(List<Projeto> projetos) {
        this.projetos = projetos;
        this.colunas = Arrays.asList("Id", "Projeto", "Início");
    }

    public String getColumnName(int i) {
        return this.colunas.get(i);
    }

    @Override
    public int getRowCount() {
        return projetos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Projeto proj = projetos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return proj.getId();
            case 1:
                return proj.getNome();
            case 2:
                return DateLocalFormater.dateToString(proj.getInicio());
        }
        return null;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
        super.fireTableDataChanged();
    }

    public List<String> getColunas() {
        return colunas;
    }
}
