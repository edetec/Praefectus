/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soares
 */
public abstract class Controller implements ActionListener {

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(e.getActionCommand());
            Method mtd = this.getClass().getMethod(e.getActionCommand());
            mtd.invoke(this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(PrincipalCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void actions();
}
