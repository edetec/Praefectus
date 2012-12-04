/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author soares
 */
public abstract class DateLocalFormater {

    public static String dateToString(Date date) {
        return DateLocalFormater.defaultDate().format(date);
    }

    public static DateFormat defaultDate(){
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM,new Locale("pt","BR"));
    }
    
    public static Date stringToDate(String data) {
        Date date;
        try {
            date = DateLocalFormater.defaultDate().parse(data);
        } catch (ParseException ex) {
            date = new Date();
        }
        return date;
    }
}
