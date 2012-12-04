/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.controller;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import praefectus.model.ItemBacklogSprint;
import praefectus.model.Projeto;
import praefectus.model.Sprint;
import praefectus.model.application.BacklogSprintApp;
import praefectus.model.application.SprintApp;
import praefectus.util.DateLocalFormater;
import praefectus.util.Mascaras;
import praefectus.view.EditJDialog;
import praefectus.view.SprintView;
import praefectus.view.model.BacklogSprintTableModel;
import praefectus.view.model.SprintTableModel;
import praefectus.view.model.StatusComboBoxEditor;

/**
 *
 * @author soares
 */
public class SprintCtrl extends Controller {

    private final PrincipalCtrl controlePrincipal;
    private final SprintView tela;
    private final String tituloTela;
    private final SprintApp appSprint;
    private final BacklogSprintApp appBacklogSprint;
    private final Projeto projeto;
    private SprintTableModel sprintsTableModel;
    private BacklogSprintTableModel backlogSprintTableModel;
    private Sprint sprintSelecionado;

    public SprintCtrl(final PrincipalCtrl controlePrincipal, Projeto proj) {
        this.controlePrincipal = controlePrincipal;
        this.appSprint = new SprintApp();
        this.appBacklogSprint = new BacklogSprintApp();
        this.tela = new SprintView();
        this.tituloTela = tela.getTitle().concat(" - ");
        this.projeto = proj;

        initcomponents();
        actions();
        inicializaTableModelSprints();
        Mascaras.mascaraData(tela.getjFormattedTextFieldInicio());
        Mascaras.mascaraData(tela.getjFormattedTextFieldFim());
        clearForm();

        this.tela.setTitle(tituloTela.concat(proj.getNome()));
        this.controlePrincipal.newInternalFrame(tela);
    }

    @Override
    protected void actions() {
        tela.getjButtonSalvar().setActionCommand("actSalvar");
        tela.getjButtonSalvar().addActionListener(this);
        tela.getjButtonNovaSprint().setActionCommand("actNovo");
        tela.getjButtonNovaSprint().addActionListener(this);
        tela.getjButtonSprintBacklog().setActionCommand("actSprintBacklog");
        tela.getjButtonSprintBacklog().addActionListener(this);
        tela.getjButtonChart().setActionCommand("actChart");
        tela.getjButtonChart().addActionListener(this);
        tela.getjButtonReview().setActionCommand("actRview");
        tela.getjButtonReview().addActionListener(this);
    }

    public void actSalvar() {
        formToSprintSelecionado();
        appSprint.salvar(sprintSelecionado);
        clearForm();
        sprintsTableModel.setProjetos(appSprint.listaSprints(projeto));
    }

    public void actNovo() {
        clearForm();
    }

    public void actChart() {
        if (!getSelecionado()) {
            return;
        }

        HashMap<Integer, Integer> valores = new HashMap<Integer, Integer>();
        LinkedList<String> rotulos = new LinkedList();
        for (int i = 0; i <= sprintSelecionado.getProjeto().getDuracaoSprint(); i++) {
            rotulos.add(i + "");
        }

        int totalEsforco = 0;
        for (ItemBacklogSprint item : appBacklogSprint.listaBacklogSprint(sprintSelecionado)) {
            totalEsforco = totalEsforco + item.getItem().getEstimativa();
            if (item.getDataConclusao() == null) {
                continue;
            } else if (item.getDataConclusao().getTime() > sprintSelecionado.getFim().getTime()) {
                item.setDataConclusao(sprintSelecionado.getFim());
            }

            long diferencaDatas = item.getDataConclusao().getTime() - sprintSelecionado.getInicio().getTime();
            Calendar calendar = Calendar.getInstance();
            Date dt = new Date(diferencaDatas);
            calendar.setTime(dt);
            
            if (calendar.get(Calendar.DAY_OF_YEAR) > sprintSelecionado.getProjeto().getDuracaoSprint()) {
                valores.put(sprintSelecionado.getProjeto().getDuracaoSprint()+1, totalEsforco);
            } else {
                valores.put(calendar.get(Calendar.DAY_OF_YEAR), totalEsforco);
            }
            
            System.out.println("inicio Sprint: " + DateLocalFormater.dateToString(sprintSelecionado.getInicio()));
            System.out.println("    conclusão item: " + DateLocalFormater.dateToString(item.getDataConclusao()));
            System.out.println("    dias conclusão item: " + calendar.get(Calendar.DAY_OF_YEAR));
            System.out.println("fim Sprint: " + DateLocalFormater.dateToString(sprintSelecionado.getFim()));
            System.out.println("data Final: " + DateLocalFormater.dateToString(dt));
            System.out.println("-");
        }

        valores.put(0, totalEsforco);

        new ChartCtrl(controlePrincipal,"Burn Down Chart - Sprint - " + DateLocalFormater.dateToString(sprintSelecionado.getInicio()), "Dias", "Esforço Restante", valores, rotulos);
    }

    public void actRview() {
        if (!getSelecionado()) {
            return;
        }
        final EditJDialog editor = new EditJDialog(controlePrincipal.getTelaPincipal(), true);

        editor.setText(sprintSelecionado.getReview().toString());
        editor.getjButtonOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sprintSelecionado.setReview(new StringBuffer(editor.getText()));
                appSprint.salvar(sprintSelecionado);
                editor.dispose();
            }
        });
        editor.setVisible(true);
    }

    public void actSprintBacklog() {
        if (getSelecionado()) {
            new BacklogSprintCtrl(controlePrincipal, sprintSelecionado);
        }
    }

    private void inicializaTableModelBacklogSprint() {
        backlogSprintTableModel = new BacklogSprintTableModel(appBacklogSprint.listaBacklogSprint(sprintSelecionado));
        tela.getjTableSprintBacklog().setModel(backlogSprintTableModel);
        StatusComboBoxEditor combo = new StatusComboBoxEditor(appBacklogSprint.listaStatus());
        combo.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                ItemBacklogSprint itemBacklogSprint = backlogSprintTableModel.getItensBacklogSprint().get(tela.getjTableSprintBacklog().getSelectedRow());
                appBacklogSprint.salvar(itemBacklogSprint);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });
        tela.getjTableSprintBacklog().getColumnModel().getColumn(2).setCellEditor(combo);
    }

    private void inicializaTableModelSprints() {
        sprintsTableModel = new SprintTableModel(appSprint.listaSprints(projeto));
        tela.getjTableSprints().setModel(sprintsTableModel);
        tela.getjTableSprints().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
//                    actProjetoBacklog();
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    sprintSelecionado = sprintsTableModel.getSprints().get(tela.getjTableSprints().getSelectedRow());
                    sprintToForm();
                    inicializaTableModelBacklogSprint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void sprintToForm() {
        if (sprintSelecionado.getInicio() == null) {
            tela.getjFormattedTextFieldInicio().setText("");
            tela.getjFormattedTextFieldFim().setText("");
        } else {
            tela.getjFormattedTextFieldInicio().setText(DateLocalFormater.dateToString(sprintSelecionado.getInicio()));

            if (sprintSelecionado.getFim() == null) {
                Calendar dataFim = Calendar.getInstance();
                dataFim.setTime(new Date());
                dataFim.add(Calendar.DATE, projeto.getDuracaoSprint());
                tela.getjFormattedTextFieldFim().setText(DateLocalFormater.dateToString(dataFim.getTime()));
            } else {
                tela.getjFormattedTextFieldFim().setText(DateLocalFormater.dateToString(sprintSelecionado.getFim()));
            }
        }
    }

    private void formToSprintSelecionado() {
        sprintSelecionado.setInicio(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldInicio().getText()));
        sprintSelecionado.setFim(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldFim().getText()));
    }

    private void clearForm() {
        sprintSelecionado = new Sprint(projeto);
        sprintToForm();
    }

    private void initcomponents() {
        tela.getjFormattedTextFieldInicio().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String text = evt.getNewValue() != null ? evt.getNewValue().toString() : "";
                Calendar dataFim = Calendar.getInstance();
                dataFim.setTime(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldInicio().getText()));
                dataFim.add(Calendar.DATE, projeto.getDuracaoSprint());
                tela.getjFormattedTextFieldFim().setText(DateLocalFormater.dateToString(dataFim.getTime()));
            }
        });

    }

    private boolean getSelecionado() throws HeadlessException {
        int linha = tela.getjTableSprints().getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(tela, "Selecione uma Sprint");
            return false;
        }
        sprintSelecionado = sprintsTableModel.getSprints().get(linha);
        return true;
    }
}
