/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import praefectus.model.ItemBacklog;
import praefectus.model.ItemBacklogSprint;
import praefectus.model.Sprint;
import praefectus.model.Status;
import praefectus.model.application.BacklogSprintApp;
import praefectus.model.application.ProductBacklogApp;
import praefectus.model.application.SprintApp;
import praefectus.util.DateLocalFormater;
import praefectus.util.Mascaras;
import praefectus.view.SprintBacklogView;
import praefectus.view.model.BacklogSprintTableModel;
import praefectus.view.model.BacklogTableModel;
import praefectus.view.model.StatusComboBoxEditor;

/**
 *
 * @author soares
 */
public class BacklogSprintCtrl extends Controller {

    private final PrincipalCtrl controlePrincipal;
    private final SprintBacklogView tela;
    private final String tituloTela;
    private final SprintApp appSprint;
    private final BacklogSprintApp appBacklogSprint;
    private final ProductBacklogApp appBacklog;
    private BacklogSprintTableModel backlogSprintTableModel;
    private Sprint sprint;
    private BacklogTableModel projectBacklogTableModel;
    private final LinkedList<Status> listaStatus;

    public BacklogSprintCtrl(final PrincipalCtrl controlePrincipal, Sprint sprint) {
        this.controlePrincipal = controlePrincipal;
        appSprint = new SprintApp();
        appBacklog = new ProductBacklogApp();
        appBacklogSprint = new BacklogSprintApp();
        tela = new SprintBacklogView();
        tituloTela = tela.getTitle().concat(" - ");
        this.sprint = sprint;
        listaStatus = appBacklogSprint.listaStatus();

        initcomponents();
        actions();

        this.controlePrincipal.newInternalFrame(tela);
    }

    @Override
    protected void actions() {
        tela.getjButtonSalvar().setActionCommand("actSalvar");
        tela.getjButtonSalvar().addActionListener(this);
        tela.getjButtonAdd().setActionCommand("actAddToSprint");
        tela.getjButtonAdd().addActionListener(this);
        tela.getjButtonRemove().setActionCommand("actRemoveFromSprint");
        tela.getjButtonRemove().addActionListener(this);
    }

    public void actSalvar() {
        formToSprint();
        if (appSprint.salvar(sprint)) {
            JOptionPane.showMessageDialog(tela, "Projeto salvo.");
        } else {
            JOptionPane.showMessageDialog(tela, "Erro.");
        }
    }

    public void actAddToSprint() {
        int linha = tela.getjTableBacklog().getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(tela, "Selecione um Item no Product Backlog");
            return;
        }

        ItemBacklog itemBacklog = projectBacklogTableModel.getItemBacklog().get(linha);
        ItemBacklogSprint itemBacklogSprint = new ItemBacklogSprint();
        itemBacklogSprint.setItem(itemBacklog);
        itemBacklogSprint.setStatus(listaStatus.getFirst());
        itemBacklogSprint.setSprint(sprint);

        if (appBacklogSprint.salvar(itemBacklogSprint)) {
            int novaLinha = backlogSprintTableModel.getRowCount();
            backlogSprintTableModel.getItensBacklogSprint().add(itemBacklogSprint);
            backlogSprintTableModel.fireTableRowsInserted(novaLinha, novaLinha);

            projectBacklogTableModel.getItemBacklog().remove(linha);
            projectBacklogTableModel.fireTableRowsDeleted(linha, linha);
        }
    }

    public void actRemoveFromSprint() {
        int linha = tela.getjTableSprintBacklog().getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(tela, "Selecione um Item no Sprint Backlog");
            return;
        }

        ItemBacklogSprint itemBacklogSprint = backlogSprintTableModel.getItensBacklogSprint().get(linha);
        if (appBacklogSprint.deletar(itemBacklogSprint)) {
            ItemBacklog itemBacklog = itemBacklogSprint.getItem();

            int novaLinha = projectBacklogTableModel.getRowCount();
            projectBacklogTableModel.getItemBacklog().add(itemBacklog);
            projectBacklogTableModel.fireTableRowsInserted(novaLinha, novaLinha);

            backlogSprintTableModel.getItensBacklogSprint().remove(linha);
            backlogSprintTableModel.fireTableRowsDeleted(linha, linha);
        }
    }

    private void inicializaTableModelBacklogSprint() {
        backlogSprintTableModel = new BacklogSprintTableModel(appBacklogSprint.listaBacklogSprint(sprint));
        tela.getjTableSprintBacklog().setModel(backlogSprintTableModel);
        StatusComboBoxEditor combo = new StatusComboBoxEditor(listaStatus);
        combo.addCellEditorListener(new CellEditorListener() {

            @Override
            public void editingStopped(ChangeEvent e) {
                System.out.println("datas: " + sprint.getFim().compareTo(Calendar.getInstance().getTime()));
                Date data = (sprint.getFim().compareTo(Calendar.getInstance().getTime()) < 0 ) ? sprint.getFim() : Calendar.getInstance().getTime();
                ItemBacklogSprint itemBacklogSprint = backlogSprintTableModel.getItensBacklogSprint().get(tela.getjTableSprintBacklog().getSelectedRow());
                itemBacklogSprint.setDataConclusao(data);
                appBacklogSprint.salvar(itemBacklogSprint);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });
        tela.getjTableSprintBacklog().getColumnModel().getColumn(2).setCellEditor(combo);
    }

    private void inicializaTableModelpProductBacklog() {
        projectBacklogTableModel = new BacklogTableModel(sprint.getProjeto(), appBacklog.listaBacklog(sprint.getProjeto(),0));
        projectBacklogTableModel.setEditavel(false);
        tela.getjTableBacklog().setModel(projectBacklogTableModel);
        tela.getjTableBacklog().removeEditor();
    }

    private void formToSprint() {
        sprint.setInicio(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldInicio().getText()));
        sprint.setFim(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldFim().getText()));
    }

    private void sprintToForm() {
        tela.getjFormattedTextFieldInicio().setText(DateLocalFormater.dateToString(sprint.getInicio()));
        tela.getjFormattedTextFieldFim().setText(DateLocalFormater.dateToString(sprint.getFim()));
    }

    private void initcomponents() {

        Mascaras.mascaraData(tela.getjFormattedTextFieldInicio());
        Mascaras.mascaraData(tela.getjFormattedTextFieldFim());
        sprintToForm();

        tela.setTitle(tituloTela.concat(sprint.getProjeto().getNome()
                .concat(" > " + DateLocalFormater.dateToString(sprint.getInicio()))
                .concat(" a " + DateLocalFormater.dateToString(sprint.getFim()))));

        tela.getjFormattedTextFieldInicio().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String text = evt.getNewValue() != null ? evt.getNewValue().toString() : "";
                Calendar dataFim = Calendar.getInstance();
                dataFim.setTime(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldInicio().getText()));
                dataFim.add(Calendar.DATE, sprint.getProjeto().getDuracaoSprint());
                tela.getjFormattedTextFieldFim().setText(DateLocalFormater.dateToString(dataFim.getTime()));
            }
        });

        inicializaTableModelBacklogSprint();
        inicializaTableModelpProductBacklog();
    }
}
