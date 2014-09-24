/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import praefectus.model.Projeto;
import praefectus.model.application.ProjetoApp;
import praefectus.util.DateLocalFormater;
import praefectus.util.Mascaras;
import praefectus.view.ProjetoView;
import praefectus.view.model.ProjetoTableModel;

/**
 *
 * @author soares
 */
public class ProjetoCtrl extends Controller {

    private ProjetoView tela;
    private ProjetoApp appProjeto;
    private ProjetoTableModel tableModel;
    private String tituloTela;
    PrincipalCtrl controlePrincipal;

    public ProjetoCtrl(final PrincipalCtrl controlePrincipal) {
        this.controlePrincipal = controlePrincipal;
        appProjeto = new ProjetoApp();
        tela = new ProjetoView();
        tituloTela = tela.getTitle().concat(" - ");
        actions();
        inicializaTableModel();
        Mascaras.mascaraData(tela.getjFormattedTextFieldInicio());
        Mascaras.mascaraData(tela.getjFormattedTextFieldFim());

        this.controlePrincipal.newInternalFrame(tela);
    }

    public void inicializaTableModel() {
        tableModel = new ProjetoTableModel(appProjeto.list());
        tela.getjTableProjetos().setModel(tableModel);
        tela.getjTableProjetos().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    actProjetoBacklog();
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    Projeto proj = tableModel.getProjetos().get(tela.getjTableProjetos().getSelectedRow());
                    projetoToForm(proj);
                    tela.setTitle(tituloTela.concat(proj.getNome()));
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

    @Override
    protected void actions() {
        tela.getjButtonSalvar().addActionListener(this);
        tela.getjButtonSalvar().setActionCommand("actProjetoSalvar");
        tela.getjButtonNovo().addActionListener(this);
        tela.getjButtonNovo().setActionCommand("actProjetoNovo");
        tela.getjButtonExcluir().addActionListener(this);
        tela.getjButtonExcluir().setActionCommand("actProjetoExcluir");
        tela.getjButtonBacklog().addActionListener(this);
        tela.getjButtonBacklog().setActionCommand("actProjetoBacklog");
        tela.getjButtonSprints().addActionListener(this);
        tela.getjButtonSprints().setActionCommand("actSprintsProjeto");
    }

    public void actProjetoSalvar() {
        if (appProjeto.save(formToProjeto())) {
            JOptionPane.showMessageDialog(tela, "Projeto salvo.");
            clearForm();
            tableModel.setProjetos(appProjeto.list());
            tela.setTitle(tituloTela);
        } else {
            JOptionPane.showMessageDialog(tela, "Erro.");
        }
    }

    public void actProjetoNovo() {
        clearForm();
        tela.setTitle(tituloTela.concat("Novo Projeto"));
    }

    public void actProjetoExcluir() {
        try {
            Projeto proj = tableModel.getProjetos().get(tela.getjTableProjetos().getSelectedRow());
            if (appProjeto.delete(proj)) {
                tableModel.setProjetos(appProjeto.list());
                clearForm();
                tela.setTitle(tituloTela);
            }
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(tela, "Selecione um projeto.");
        }
    }

    public void actProjetoBacklog() {
        try {
            Projeto proj = tableModel.getProjetos().get(tela.getjTableProjetos().getSelectedRow());
            System.out.println("Backlog projeto " + proj.getId());
            new ProductBacklogCtrl(controlePrincipal,proj);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(tela, "Selecione um projeto.");
        }
    }
    
    public void actSprintsProjeto() {
        try {
            Projeto proj = tableModel.getProjetos().get(tela.getjTableProjetos().getSelectedRow());
            System.out.println("Backlog projeto " + proj.getId());
            new SprintCtrl(controlePrincipal,proj);
        } catch (IndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(tela, "Selecione um projeto.");
        }
    }

    private Projeto formToProjeto() {
        Projeto proj = new Projeto();
        proj.setNome(tela.getjTextFieldNome().getText());
        proj.setDescricao(new StringBuffer(tela.getjTextAreaDescricao().getText()));
        proj.setInicio(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldInicio().getText()));
        proj.setFim(DateLocalFormater.stringToDate(tela.getjFormattedTextFieldFim().getText()));
        try {
            proj.setDuracaoSprint(Integer.parseInt(tela.getjTextFieldDuracaoSprint().getText()));
        } catch (NumberFormatException e) {
            proj.setDuracaoSprint(0);
        }
        try {
            proj.setPontosSprint(Integer.parseInt(tela.getjTextFieldPontosSprint().getText()));
        } catch (NumberFormatException e) {
            proj.setPontosSprint(0);
        }
        try {
            proj.setId(Long.parseLong(tela.getjTextFieldId().getText()));
        } catch (NumberFormatException e) {
            System.out.println("Novo Projeto.");
        }
        return proj;
    }

    private void projetoToForm(Projeto proj) {
        tela.getjTextAreaDescricao().setText(proj.getDescricao().toString());
        tela.getjFormattedTextFieldInicio().setText(DateLocalFormater.dateToString(proj.getInicio()));
        tela.getjTextFieldNome().setText(proj.getNome());
        tela.getjTextFieldId().setText(proj.getId().toString());
        tela.getjTextFieldDuracaoSprint().setText(proj.getDuracaoSprint().toString());
        tela.getjTextFieldPontosSprint().setText(proj.getPontosSprint().toString());
    }

    private void clearForm() {
        tela.getjTextAreaDescricao().setText("");
        tela.getjFormattedTextFieldInicio().setText("");
        tela.getjTextFieldNome().setText("");
        tela.getjTextFieldId().setText("");
        tela.getjTextFieldDuracaoSprint().setText("");
        tela.getjTextFieldPontosSprint().setText("");
    }
}
