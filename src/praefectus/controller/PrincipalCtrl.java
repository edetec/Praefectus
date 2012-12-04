/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.controller;

import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import praefectus.Praefectus;
import praefectus.view.PrincipalView;
import sun.tools.jar.JarImageSource;

/**
 *
 * @author soares
 */
public class PrincipalCtrl extends Controller {

    private PrincipalView telaPincipal;

    public PrincipalView getTelaPincipal() {
        return telaPincipal;
    }

    public PrincipalCtrl() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(PrincipalCtrl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Image iconeTitulo = Toolkit.getDefaultToolkit()
                        .getImage(Praefectus.class.getClassLoader().getResource("praefectus/view/icons/logo32.png"));
                telaPincipal = new PrincipalView();
                telaPincipal.setExtendedState(telaPincipal.MAXIMIZED_BOTH);
                telaPincipal.setIconImage(iconeTitulo);

                telaPincipal.setVisible(true);
                actions();
                actProjetoNovo();
            }
        });
    }

    public void actProjetoNovo() {
        new ProjetoCtrl(this);
    }

    public void newInternalFrame(final JInternalFrame tela) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                telaPincipal.getjDesktopPane().add(tela);
                try {
                    tela.setMaximum(true);
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(PrincipalCtrl.class.getName()).log(Level.SEVERE, null, ex);
                }
                tela.setVisible(true);
            }
        });
    }

    @Override
    protected void actions() {
        telaPincipal.getjMenuProjetoNovo().addActionListener(this);
        telaPincipal.getjMenuProjetoNovo().setActionCommand("actProjetoNovo");
        telaPincipal.getjMenuSobre().addActionListener(this);
        telaPincipal.getjMenuSobre().setActionCommand("actSobre");
    }
}
