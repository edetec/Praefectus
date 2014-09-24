/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.util;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Soares
 */
public class Mascaras {

    public static MaskFormatter mascaraData(JFormattedTextField textfield) {
        MaskFormatter mask = null;

        try {
            mask = new MaskFormatter("##/##/####");
            mask.setOverwriteMode(true);
            mask.setValidCharacters("0123456789");
            mask.setPlaceholderCharacter('_');
            mask.install(textfield);
        } catch (ParseException ex) {
            Logger.getLogger(Mascaras.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mask;
    }

    public static MaskFormatter mascaraNumeros(JFormattedTextField textfield) {
        MaskFormatter mask = null;
        try {

            mask = new MaskFormatter("#########");
            mask.setOverwriteMode(true);
            mask.setPlaceholder("");
            mask.setValidCharacters("0123456789");
            mask.install(textfield);

        } catch (ParseException ex) {
            Logger.getLogger(Mascaras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mask;
    }

}
