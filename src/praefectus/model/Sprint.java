/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package praefectus.model;

import java.util.Date;

/**
 *
 * @author soares
 */
public class Sprint {
    private Long id;
    private Projeto projeto;
    private Date inicio;
    private Date fim;
    private StringBuffer review;

    public StringBuffer getReview() {
        return review;
    }

    public void setReview(StringBuffer review) {
        this.review = review;
    }

    public Sprint(Projeto projeto) {
        this.projeto = projeto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }
}
