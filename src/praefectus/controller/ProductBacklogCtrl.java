/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.controller;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import praefectus.model.ItemBacklog;
import praefectus.model.Projeto;
import praefectus.model.application.ProductBacklogApp;
import praefectus.view.EditJDialog;
import praefectus.view.ProductBacklogView;
import praefectus.view.model.BacklogTableModel;

/**
 *
 * @author soares
 */
public class ProductBacklogCtrl extends Controller implements TableModelListener {

    private ProductBacklogView tela;
    PrincipalCtrl controlePrincipal;
    private ProductBacklogApp appProductBacklog;
    private String tituloTela;
    private BacklogTableModel tableModel;
    private final Projeto projeto;
    private JPopupMenu popup;
    private KeyboardFocusManager kfm;

    public ProductBacklogCtrl(final PrincipalCtrl controlePrincipal, Projeto proj) {
        appProductBacklog = new ProductBacklogApp();
        tela = new ProductBacklogView();
        tituloTela = tela.getTitle().concat(" - ");
        tela.setTitle(tituloTela.concat(proj.getNome()));
        tela.getjButtonSalvar().setEnabled(false);
        actions();
        projeto = proj;

        this.controlePrincipal = controlePrincipal;
        controlePrincipal.newInternalFrame(tela);
        inicializaTableModel();
        inicializarMenu();
        tela.getjComboBoxEstorias().addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                inicializaTableModel();
            }
        });
    }

    private void inicializarMenu() {
        popup = new JPopupMenu();
        tela.add(popup);
        JMenuItem menuItem;

        menuItem = new JMenuItem("Editar");
        menuItem.setActionCommand("actEditarItem");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));

        menuItem = new JMenuItem("Cancelar");
        menuItem.setActionCommand("actCancelarItem");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
    }

    public void actNovoItem() {
        tableModel.addEmptyRow();
    }

    public void actSalvarItem() {
        int row = tela.getjTableBacklog().getSelectedRow();
        appProductBacklog.save(tableModel.getItemBacklog().get(row));
        tela.getjButtonSalvar().setEnabled(false);
    }

    public void actExcluirItem() {
        int row = tela.getjTableBacklog().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(tela, "Selecione um Item");
            return;
        }

        appProductBacklog.excluir(tableModel.getItemBacklog().get(row));
        tableModel.removeRow(row);

    }

    public void actEditarItem() {
        final int col = tela.getjTableBacklog().getColumnCount() -1;
        final int linha = tela.getjTableBacklog().getSelectedRow();
        final Object valor = tableModel.getValueAt(linha, col);
        final EditJDialog editor = new EditJDialog(controlePrincipal.getTelaPincipal(), true);
        
        editor.setText(valor.toString());
        editor.getjButtonOK().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (valor != null) {
                    tela.getjTableBacklog().getCellEditor(linha, col).stopCellEditing();
                    tableModel.setValueAt(editor.getText(), linha, col);
                    tableModel.fireTableRowsUpdated(linha, col);
                    tela.getjTableBacklog().firePropertyChange("Editor de texto", true, false);
                    editor.dispose();
                }
            }
        });
        editor.setVisible(true);

    }

    public void actCancelarItem() {
        int row = tela.getjTableBacklog().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(tela, "Selecione um Item");
            return;
        }
        ItemBacklog item = tableModel.getItemBacklog().get(row);
        item.setCancelado(!item.isCancelado());
        if (appProductBacklog.update(item)) {
            tableModel.fireTableRowsUpdated(row, row);
        }
    }

    @Override
    protected void actions() {
        tela.getjButtonNovo().setActionCommand("actNovoItem");
        tela.getjButtonNovo().addActionListener(this);
        tela.getjButtonExcluir().setActionCommand("actExcluirItem");
        tela.getjButtonExcluir().addActionListener(this);
        tela.getjButtonSalvar().setActionCommand("actSalvarItem");
        tela.getjButtonSalvar().addActionListener(this);
        tela.getjButtonChart().setActionCommand("actChart");
        tela.getjButtonChart().addActionListener(this);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("Changed Table " + e.getFirstRow());
    }

    public void inicializaTableModel() {
        // TODO 
        int filter= tela.getjComboBoxEstorias().getSelectedIndex();
        tableModel = new BacklogTableModel(projeto, appProductBacklog.listaBacklog(projeto,filter));
        tableModel.addTableModelListener(this);
        tela.getjTableBacklog().setModel(tableModel);
        tela.getjTableBacklog().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tela.getjTableBacklog().setSurrendersFocusOnKeystroke(true);
        tela.getjTableBacklog().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tela.getjTableBacklog().getColumnModel().getColumn(0).setMaxWidth(80);
        tela.getjTableBacklog().getColumnModel().getColumn(1).setMaxWidth(100);
        tela.getjTableBacklog().getColumnModel().getColumn(2).setMaxWidth(80);
        if (!tableModel.hasEmptyRow()) {
            tableModel.addEmptyRow();
        }
        tela.getjTableBacklog().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getOldValue() != null) {
                    tela.getjButtonSalvar().setEnabled(true);
                }
            }
        });

        tela.getjTableBacklog().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            private int ultimoSelecionado = -1;

            public void valueChanged(ListSelectionEvent e) {
                if (tableModel.getItemBacklog().size() <= ultimoSelecionado) {
                    ultimoSelecionado = -1;
                }

                if (!e.getValueIsAdjusting()) {
                    if (tela.getjButtonSalvar().isEnabled() && ultimoSelecionado >= 0) {
                        System.out.println("ultima seleção " + ultimoSelecionado);
                        if (appProductBacklog.save(tableModel.getItemBacklog().get(ultimoSelecionado))) {
                            tela.getjButtonSalvar().setEnabled(false);
                        }
                    }
                    ultimoSelecionado = e.getFirstIndex();
                }
            }
        });

        tela.getjTableBacklog().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(e.getComponent(),
                            e.getX(), e.getY());
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
}
