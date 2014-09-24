/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model;

/**
 *
 * @author soares
 */
public class Status {
    private int id;
    private String rotulo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final Status other = (Status) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }    

    @Override
    public String toString() {
        return getRotulo();
    }
    
}
